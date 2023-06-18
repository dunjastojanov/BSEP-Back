package com.myhouse.MyHouse.service;

import com.myhouse.MyHouse.dto.UserTokenState;
import com.myhouse.MyHouse.dto.user.LoginDTO;
import com.myhouse.MyHouse.dto.user.UserDTO;
import com.myhouse.MyHouse.model.mfa.CustomUser;
import com.myhouse.MyHouse.util.TokenUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private InvalidTokenService invalidTokenService;


    public UserTokenState createAuthenticationToken(LoginDTO authenticationRequest, HttpServletResponse response) {
        // Ukoliko kredencijali nisu ispravni, logovanje nece biti uspesno, desice se
        // AuthenticationException
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authenticationRequest.getEmail(), authenticationRequest.getPassword()));
            // Ukoliko je autentifikacija uspesna, ubaci korisnika u trenutni security kontekst
            SecurityContextHolder.getContext().setAuthentication(authentication);
            // Kreiraj token za tog korisnika
            CustomUser user = (CustomUser) authentication.getPrincipal();
            //TODO Odkomentarisi
            if (userService.verifyTotp(authenticationRequest.getToken(), user.getSecret())) {
                throw new RuntimeException("Token nije dobar");
            }
            String fingerprint = tokenUtils.generateFingerprint();
            String jwt = tokenUtils.generateToken(user.getUsername(), fingerprint);
            int expiresIn = tokenUtils.getExpiredIn();

            // Kreiraj cookie
            Cookie cookie = new Cookie("Fingerprint", fingerprint);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            response.addCookie(cookie);

            // Vrati token kao odgovor na uspesnu autentifikaciju
            UserDTO userDTO = new UserDTO(userService.getUserByEmail(user.getUsername()));

            return new UserTokenState(jwt, expiresIn, userDTO);
        } catch (AuthenticationException exception) {
            userService.disableUser(authenticationRequest.getEmail());
            return null;
        }
    }

    public void storeJwtAsInvalid(HttpServletRequest request) {
        String token = tokenUtils.getToken(request);
        invalidTokenService.addInvalidToken(token);
        SecurityContextHolder.clearContext();
    }
}
