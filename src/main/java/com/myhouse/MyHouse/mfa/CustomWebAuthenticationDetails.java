package com.myhouse.MyHouse.mfa;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

public class CustomWebAuthenticationDetails extends WebAuthenticationDetails {
    private String token;

    public CustomWebAuthenticationDetails(HttpServletRequest request) {
        super(request);
        this.token = request.getParameter("jdjCustomToken");
    }

    @Override
    public String toString() {
        return "CustomWebAuthenticationDetails{" +
                "token='" + token + '\'' +
                '}';
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
