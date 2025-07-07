package com.grd.gradingbe.utilities;

import com.grd.gradingbe.enums.Role;
import com.grd.gradingbe.model.User;
import com.grd.gradingbe.repository.UserRepository;
import com.grd.gradingbe.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.dao.DataAccessException;
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

    public OAuth2LoginSuccessHandler(JwtService jwtService, UserRepository userRepository)
    {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
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
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"errorMessage\": \"ID token is not containing email\"}");
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
                                    .role(userRepository.findUserByRole(Role.ADMIN).isPresent()
                                            ? Role.USER : Role.ADMIN)
                                    .avatar_url(picture)
                                    .full_name(name)
                                    .updated_at(LocalDateTime.now())
                                    .created_at(LocalDateTime.now())
                                    .build()));
        }
        catch (DataAccessException e)
        {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write(String.format("{\"errorMessage\": \"Can not get or save user with email: %s\"}", email));
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

