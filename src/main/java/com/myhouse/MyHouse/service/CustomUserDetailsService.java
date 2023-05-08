package com.myhouse.MyHouse.service;

import com.myhouse.MyHouse.model.User;
import com.myhouse.MyHouse.model.mfa.CustomUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public CustomUser loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.getUserByEmail(username);
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(permissions -> permissions.getPermissions()
                .forEach(permission -> authorities.add(new SimpleGrantedAuthority(permission.name()))));
        CustomUser customUser = new CustomUser();
        customUser.setUsername(user.getEmail());
        customUser.setPassword(user.getPassword());
        customUser.setSecret(user.getSecret());
        customUser.setEnabled(user.isEnabled());
        customUser.setAuthorities(authorities);
        return customUser;
    }
}
