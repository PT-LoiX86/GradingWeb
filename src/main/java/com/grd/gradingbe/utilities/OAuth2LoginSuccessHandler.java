package com.grd.gradingbe.utilities;

import com.grd.gradingbe.dto.enums.AuthenticationType;
import com.grd.gradingbe.dto.enums.Role;
import com.grd.gradingbe.model.RefreshToken;
import com.grd.gradingbe.model.User;
import com.grd.gradingbe.repository.UserRepository;
import com.grd.gradingbe.service.JwtService;
import com.grd.gradingbe.service.RefreshTokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler
{

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final RefreshTokenService refreshTokenService;
    
    @Value("${env.app.frontend.base-url}")
    private String frontendURL;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws  IOException
    {
        Map<String, Object> attributes = getStringObjectMap(authentication);

        String email = (String) attributes.get("email");
        if (email == null || email.isEmpty())
        {
            String errorRedirectUrl = UriComponentsBuilder.fromHttpUrl(frontendURL)
                    .path("/login")
                    .queryParam("error", URLEncoder.encode("ID token is not containing email", StandardCharsets.UTF_8))
                    .build()
                    .toUriString();
            response.sendRedirect(errorRedirectUrl);
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
            String errorRedirectUrl = UriComponentsBuilder.fromHttpUrl(frontendURL)
                    .path("/login")
                    .queryParam("error", URLEncoder.encode(String.format("Can not get or save user with email: %s", email), StandardCharsets.UTF_8))
                    .build()
                    .toUriString();
            response.sendRedirect(errorRedirectUrl);
            return;
        }

        String accessToken = jwtService.generateAuthenticationToken(user);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

        String successRedirectUrl = UriComponentsBuilder.fromHttpUrl(frontendURL)
                .path("/oauth2/callback")
                .queryParam("accessToken", accessToken)
                .queryParam("refreshToken", refreshToken.getToken())
                .build()
                .toUriString();
                
        response.sendRedirect(successRedirectUrl);
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

