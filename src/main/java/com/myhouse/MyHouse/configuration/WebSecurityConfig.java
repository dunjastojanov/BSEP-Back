package com.myhouse.MyHouse.configuration;

import com.myhouse.MyHouse.model.Role;
import com.myhouse.MyHouse.security.auth.RestAuthenticationEntryPoint;
import com.myhouse.MyHouse.security.auth.TokenAuthorizationFilter;
import com.myhouse.MyHouse.service.CustomUserDetailsService;
import com.myhouse.MyHouse.util.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Autowired
    private TokenUtils tokenUtils;

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(customUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors();
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.exceptionHandling().authenticationEntryPoint(restAuthenticationEntryPoint);
        http.authorizeHttpRequests()
                .requestMatchers("/api/login","/api/user/register/verification/**").permitAll()
                .requestMatchers(HttpMethod.POST,"/api/user/register").permitAll()
                .requestMatchers(HttpMethod.GET,"/api/user/mfa/setup/**").permitAll()
                .requestMatchers("/api/logout").hasAnyRole(Role.OWNER.name(), Role.ADMINISTRATOR.name(), Role.RESIDENT.name())
                .requestMatchers("/api/certificate").hasRole(Role.ADMINISTRATOR.name())
                .requestMatchers(HttpMethod.POST, "/api/request").hasAnyRole(Role.OWNER.name(), Role.RESIDENT.name())
                .requestMatchers(HttpMethod.GET, "/api/request").hasRole(Role.ADMINISTRATOR.name())
                .requestMatchers(HttpMethod.PUT, "/api/request").hasRole(Role.ADMINISTRATOR.name())
                .requestMatchers(HttpMethod.GET,"/api/user/**").hasAnyRole(Role.OWNER.name(), Role.ADMINISTRATOR.name(), Role.RESIDENT.name())
                .requestMatchers(HttpMethod.GET,"/api/user").hasRole(Role.ADMINISTRATOR.name())
                .requestMatchers(HttpMethod.DELETE,"/api/user/**").hasRole(Role.ADMINISTRATOR.name())
                .requestMatchers(HttpMethod.PUT,"/api/user/roles/**").hasRole(Role.ADMINISTRATOR.name())
                .requestMatchers(HttpMethod.PUT,"/api/user/realestates/**").hasRole(Role.ADMINISTRATOR.name())
                .requestMatchers("/api/realestate").hasRole(Role.ADMINISTRATOR.name())
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(new TokenAuthorizationFilter(tokenUtils, customUserDetailsService), UsernamePasswordAuthenticationFilter.class);
        http.headers().xssProtection().and().contentSecurityPolicy("script-src 'self'");
        return http.build();
    }


}
