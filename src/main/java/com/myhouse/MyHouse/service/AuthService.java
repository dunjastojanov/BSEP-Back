package com.myhouse.MyHouse.service;

import com.myhouse.MyHouse.dto.UserTokenState;
import com.myhouse.MyHouse.dto.user.LoginDTO;
import com.myhouse.MyHouse.model.mfa.CustomUser;
import com.myhouse.MyHouse.util.TokenUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
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


    public ResponseEntity<?> createAuthenticationToken(LoginDTO authenticationRequest, HttpServletResponse response) {
        // Ukoliko kredencijali nisu ispravni, logovanje nece biti uspesno, desice se
        // AuthenticationException
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authenticationRequest.getEmail(), authenticationRequest.getPassword()));
            // Ukoliko je autentifikacija uspesna, ubaci korisnika u trenutni security kontekst
            SecurityContextHolder.getContext().setAuthentication(authentication);
            // Kreiraj token za tog korisnika
            CustomUser user = (CustomUser) authentication.getPrincipal();
            if (userService.verifyTotp(authenticationRequest.getToken(), user.getSecret())) {
                throw new RuntimeException("Token nije dobar");
            }
            String fingerprint = tokenUtils.generateFingerprint();
            String jwt = tokenUtils.generateToken(user.getUsername(), fingerprint);
            int expiresIn = tokenUtils.getExpiredIn();

            // Kreiraj cookie
//         String cookie = "__Secure-Fgp=" + fingerprint + "; SameSite=Strict; HttpOnly; Path=/; Secure";  // kasnije mozete probati da postavite i ostale atribute, ali tek nakon sto podesite https
            String cookie = "Fingerprint=" + fingerprint + "; HttpOnly; Path=/";

            HttpHeaders headers = new HttpHeaders();
            headers.add("Set-Cookie", cookie);

            // Vrati token kao odgovor na uspesnu autentifikaciju
            return ResponseEntity.ok().headers(headers).body(new UserTokenState(jwt, expiresIn));
        } catch (AuthenticationException exception) {
            userService.disableUser(authenticationRequest.getEmail());
            return ResponseEntity.badRequest().build();
        }
    }

    public void storeJwtAsInvalid(HttpServletRequest request) {
        String token = tokenUtils.getToken(request);
        invalidTokenService.addInvalidToken(token);
        SecurityContextHolder.clearContext();
    }
}
