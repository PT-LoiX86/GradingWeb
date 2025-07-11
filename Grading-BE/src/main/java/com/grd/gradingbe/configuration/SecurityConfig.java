package com.grd.gradingbe.configuration;

import com.grd.gradingbe.utilities.CustomAccessDeniedHandler;
import com.grd.gradingbe.utilities.JwtFilter;
import com.grd.gradingbe.utilities.OAuth2LoginSuccessHandler;

import lombok.RequiredArgsConstructor;

import com.grd.gradingbe.utilities.OAuth2LoginFailureHandler;
import com.grd.gradingbe.service.impl.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig
{
    private final JwtFilter jwtFilter;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final OAuth2LoginFailureHandler oAuth2LoginFailureHandler;
    private final CustomUserDetailsService userDetailsService;

    @Value("${env.app.front-end.base-url}")
    private String frontendURL;

    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception
    {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler()
    {
        return new CustomAccessDeniedHandler();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource()
    {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin(frontendURL);
        configuration.addAllowedMethod("GET");
        configuration.addAllowedMethod("POST");
        configuration.addAllowedMethod("PUT");
        configuration.addAllowedMethod("DELETE");
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
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/auth/**",
                                        "/oauth2/**",
                                        "/api/public/**",
                                        "/actuator/**",
                                        "/webjars/**",
                                        "/swagger-resources/**",
                                        "/v3/api-docs/**",
                                        "/swagger-ui/**",
                                        "/swagger-ui.html")
                        .permitAll()

                        .requestMatchers("/api/admin/**")
                        .hasRole("ADMIN")

                        .anyRequest().hasAnyRole("USER", "ADMIN"))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .oauth2Login(oauth2 -> oauth2
                        .successHandler(oAuth2LoginSuccessHandler)
                        .failureHandler(oAuth2LoginFailureHandler)
                )
                .exceptionHandling((exception)-> exception.accessDeniedHandler(accessDeniedHandler()))
                .cors(cors -> cors.configurationSource(corsConfigurationSource()));

        return http.build();
    }
}
