package com.grd.gradingbe.utilities;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grd.gradingbe.dto.response.ErrorResponse;
import com.grd.gradingbe.dto.enums.AuthenticationType;
import com.grd.gradingbe.dto.enums.Role;
import com.grd.gradingbe.model.User;
import com.grd.gradingbe.repository.UserRepository;
import com.grd.gradingbe.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

@Component
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler
{

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    public OAuth2LoginSuccessHandler(JwtService jwtService, UserRepository userRepository, ObjectMapper objectMapper)
    {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws  IOException
    {
        Map<String, Object> attributes = getStringObjectMap(authentication);

        String email = (String) attributes.get("email");
        if (email == null || email.isEmpty())
        {
            sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "ID token is not containing email");
            return;
        }
        String name = (String) attributes.getOrDefault("name", "");
        String picture = (String) attributes.getOrDefault("picture", "");

        User user;
        try
        {
            user = userRepository.findByEmail(email)
                    .orElseGet(() -> userRepository.save(
                            User.builder()
                                    .email(email)
                                    .role(Role.USER)
                                    .avatar_url(picture)
                                    .full_name(name)
                                    .updated_at(LocalDateTime.now())
                                    .created_at(LocalDateTime.now())
                                    .authType(AuthenticationType.GOOGLE)
                                    .verified(true)
                                    .is_active(true)
                                    .build()));
        }
        catch (DataAccessException e)
        {
            sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, String.format("Can not get or save user with email: %s", email));
            return;
        }

        String accessToken = jwtService.generateAuthenticationToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);


        response.setContentType("application/json");
        response.getWriter().write("{\"accessToken\": \""
                + accessToken + "\", "
                + "\"refreshToken\": \""
                + refreshToken + "\"}");
    }

    private void sendErrorResponse(HttpServletResponse response, int status, String message) throws IOException {
        ErrorResponse errorResponse = new ErrorResponse(
                "uri=/api/request",
                HttpStatus.valueOf(status),
                message,
                LocalDateTime.now()
        );

        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        objectMapper.writeValue(response.getWriter(), errorResponse);
    }

    private static Map<String, Object> getStringObjectMap(Authentication authentication) {
        Object principal = authentication.getPrincipal();

        Map<String, Object> attributes;
        if (principal instanceof OidcUser)
        {
            attributes = ((OidcUser) principal).getClaims();
        }
        else if (principal instanceof DefaultOAuth2User) {
            attributes = ((DefaultOAuth2User) principal).getAttributes();
        }
        else
        {
            attributes = Map.of("name", principal.toString());
        }
        return attributes;
    }
}

