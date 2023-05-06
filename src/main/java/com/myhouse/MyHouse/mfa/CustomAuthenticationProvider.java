package com.myhouse.MyHouse.mfa;

import com.myhouse.MyHouse.model.mfa.CustomUser;
import com.myhouse.MyHouse.service.LoginVerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationProvider extends DaoAuthenticationProvider {

    @Autowired
    private LoginVerificationService loginVerificationService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public CustomAuthenticationProvider(UserDetailsService service) {
        super.setUserDetailsService(service);
    }

    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) {
        super.additionalAuthenticationChecks(userDetails, authentication);
        CustomWebAuthenticationDetails authenticationDetails = (CustomWebAuthenticationDetails) authentication.getDetails();
        CustomUser user = (CustomUser) userDetails;
        String mfaToken = authenticationDetails.getToken();
        if (!loginVerificationService.verifyTotp(mfaToken, user.getSecret())) {
            throw new BadCredentialsException(messages.getMessage(
                    "AbstractUserDetailsAuthenticationProvider.badCredentials",
                    "Bad credentials"));
        }
    }
}
