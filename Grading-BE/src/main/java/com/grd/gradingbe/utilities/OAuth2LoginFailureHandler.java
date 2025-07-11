package com.grd.gradingbe.utilities;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
public class OAuth2LoginFailureHandler implements AuthenticationFailureHandler {

    @Value("${env.app.frontend.base-url}")
    private String frontendURL;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                      HttpServletResponse response,
                                      AuthenticationException exception) throws IOException {
        
        String errorMessage = "OAuth2 authentication failed: " + exception.getMessage();
        
        String redirectUrl = UriComponentsBuilder.fromHttpUrl(frontendURL)
                .path("/login")
                .queryParam("error", URLEncoder.encode(errorMessage, StandardCharsets.UTF_8))
                .build()
                .toUriString();
                
        response.sendRedirect(redirectUrl);
    }
}
