package com.grd.gradingbe.configuration;

import com.grd.gradingbe.utilities.JwtFilter;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.oauth2.server.resource.authentication.JwtIssuerAuthenticationManagerResolver;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableWebSecurity
public class SecurityConfig
{
    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter)
    {
        this.jwtFilter = jwtFilter;
    }

    @Value("${jwt.secret}")
    private String serverJwtSecret;

    @Value("${app.jwt.issuer}")
    private String serverJwtIssuer;

    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String googleJwtIssuer;

    public JwtDecoder googleJwtDecoder()
    {
        return JwtDecoders.fromIssuerLocation(googleJwtIssuer);
    }

    public JwtDecoder serverJwtDecoder()
    {
        SecretKey key = Keys.hmacShaKeyFor(serverJwtSecret.getBytes(StandardCharsets.UTF_8));
        return NimbusJwtDecoder.withSecretKey(key).build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception
    {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public JwtIssuerAuthenticationManagerResolver authenticationManagerResolver()
    {
        Map<String, AuthenticationManager> authenticationManagers = new HashMap<>();

        JwtAuthenticationProvider googleAuthProvider = new JwtAuthenticationProvider(googleJwtDecoder());
        authenticationManagers.put(googleJwtIssuer, googleAuthProvider::authenticate);

        JwtAuthenticationProvider serverAuthProvider = new JwtAuthenticationProvider(serverJwtDecoder());
        authenticationManagers.put(serverJwtIssuer, serverAuthProvider::authenticate);

        return new JwtIssuerAuthenticationManagerResolver(authenticationManagers::get);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource()
    {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("${app.base-url}");
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
    {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/auth/login", "/api/auth/register", "/api/auth/reset-password", "/api/auth/forgot-password")
                        .permitAll()

                        .requestMatchers("")
                        .hasRole("ADMIN")

                        .requestMatchers("")
                        .hasRole("USER")

                        .anyRequest().authenticated())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .oauth2ResourceServer(oauth2 -> oauth2
                        .authenticationManagerResolver(authenticationManagerResolver()))
                .cors(cors -> cors.configurationSource(corsConfigurationSource()));

        return http.build();
    }
}
