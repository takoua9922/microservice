package com.esprit.ms.getway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.http.HttpMethod;
import org.springframework.security.oauth2.client.oidc.web.server.logout.OidcClientInitiatedServerLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler;

/**
 * Security configuration for Spring Cloud Gateway (WebFlux)
 * Configures OAuth2 login and JWT token relay to downstream services
 */
@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    /**
     * Configure security for the gateway
     * - Allows public access to actuator endpoints
     * - Protects all other routes with OAuth2 authentication
     * - Enables OAuth2 client for login
     * - Enables resource server for JWT validation
     */
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(
            ServerHttpSecurity http,
            ReactiveClientRegistrationRepository clientRegistrationRepository) {
        
        http
            .csrf(ServerHttpSecurity.CsrfSpec::disable)
            // Enable CORS processing before auth (CorsWebFilter bean handles policies)
            .cors(ServerHttpSecurity.CorsSpec::and)
            .authorizeExchange(exchange -> exchange
                // Allow preflight requests universally
                .pathMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                // Public endpoints - no authentication required
                .pathMatchers("/actuator/**").permitAll()
                .pathMatchers("/oauth2/**").permitAll()
                .pathMatchers("/login/**").permitAll()
                // Auth endpoints - public for registration and login
                .pathMatchers("/api/auth/register").permitAll()
                .pathMatchers("/api/auth/login").permitAll()
                .pathMatchers("/api/auth/public/**").permitAll()
                // All other requests require JWT authentication
                .anyExchange().authenticated()
            )
            // Remove interactive oauth2Login to avoid 302 redirects for SPA; rely solely on bearer JWT
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> {
                    // JWT decoder/config via application properties
                })
            )
            // Logout configuration (OIDC) retained if needed
            .logout(logout -> logout
                .logoutSuccessHandler(oidcLogoutSuccessHandler(clientRegistrationRepository))
            );
        
        return http.build();
    }

    /**
     * Configure OIDC logout handler to properly logout from Keycloak
     */
    private ServerLogoutSuccessHandler oidcLogoutSuccessHandler(
            ReactiveClientRegistrationRepository clientRegistrationRepository) {
        OidcClientInitiatedServerLogoutSuccessHandler successHandler = 
            new OidcClientInitiatedServerLogoutSuccessHandler(clientRegistrationRepository);
        successHandler.setPostLogoutRedirectUri("{baseUrl}");
        return successHandler;
    }
}
