package com.esprit.ms.getway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/**
 * CORS Configuration for Spring Cloud Gateway (Reactive)
 * Allows cross-origin requests from React frontend
 * 
 * IMPORTANT: For JWT authentication, we DON'T need allowCredentials
 * because JWT is sent in Authorization header, not cookies.
 */
@Configuration
public class CorsConfig {

    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        
        // Allow specific origins (React app URLs)
        // Note: Cannot use "*" when allowCredentials is true
        corsConfig.setAllowedOriginPatterns(Arrays.asList("*"));  // Allow all origins for development
        
        // OR use specific origins for production:
        // corsConfig.setAllowedOrigins(Arrays.asList(
        //     "http://localhost:3000",
        //     "http://localhost:3001"
        // ));
        
        // Allow all HTTP methods
        corsConfig.setAllowedMethods(Arrays.asList("*"));
        
        // Allow all headers
        corsConfig.setAllowedHeaders(Arrays.asList("*"));
        
        // Expose these headers to the frontend
        corsConfig.setExposedHeaders(Arrays.asList(
            "Authorization",
            "Content-Type",
            "Accept"
        ));
        
        // Set to false for JWT-based auth (we're using Authorization header, not cookies)
        corsConfig.setAllowCredentials(false);
        
        // Cache preflight response for 1 hour
        corsConfig.setMaxAge(3600L);
        
        // Apply CORS configuration to all routes
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);
        
        return new CorsWebFilter(source);
    }
}

