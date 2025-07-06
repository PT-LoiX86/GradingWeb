package com.grd.gradingbe.utilities;

import com.grd.gradingbe.enums.Role;
import com.grd.gradingbe.model.User;
import com.grd.gradingbe.repository.UserRepository;
import com.grd.gradingbe.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
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
        String name = (String) attributes.get("name");
        String picture = (String) attributes.get("picture");

        User user = userRepository.findByEmail(email)
                .orElseGet(() -> userRepository.save(
                        User.builder()
                                .email(email)
                                .role(Role.USER)
                                .avatar_url(picture)
                                .full_name(name)
                                .build()));
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

