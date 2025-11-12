package com.esprit.ms.avis.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Custom JWT converter for Keycloak tokens
 * Extracts roles from both realm_access.roles and resource_access.{client}.roles
 */
@Component
public class KeycloakJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final JwtGrantedAuthoritiesConverter defaultGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        Collection<GrantedAuthority> authorities = Stream.concat(
                defaultGrantedAuthoritiesConverter.convert(jwt).stream(),
                extractResourceRoles(jwt).stream()
        ).collect(Collectors.toSet());

        return new JwtAuthenticationToken(jwt, authorities, getPrincipalClaimName(jwt));
    }

    /**
     * Extract roles from Keycloak token
     * Checks both realm_access.roles and resource_access.{client-id}.roles
     */
    private Collection<? extends GrantedAuthority> extractResourceRoles(Jwt jwt) {
        Set<String> roles = new HashSet<>();

        // Extract realm roles from realm_access.roles
        Map<String, Object> realmAccess = jwt.getClaim("realm_access");
        if (realmAccess != null && realmAccess.get("roles") != null) {
            Collection<String> realmRoles = (Collection<String>) realmAccess.get("roles");
            roles.addAll(realmRoles);
        }

        // Extract client roles from resource_access.{client-id}.roles
        Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
        if (resourceAccess != null) {
            resourceAccess.values().forEach(resource -> {
                if (resource instanceof Map) {
                    Map<String, Object> resourceMap = (Map<String, Object>) resource;
                    if (resourceMap.get("roles") != null) {
                        Collection<String> clientRoles = (Collection<String>) resourceMap.get("roles");
                        roles.addAll(clientRoles);
                    }
                }
            });
        }

        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toSet());
    }

    /**
     * Get the principal name from JWT token
     * Uses preferred_username if available, otherwise falls back to sub
     */
    private String getPrincipalClaimName(Jwt jwt) {
        String claimName = jwt.getClaim("preferred_username");
        return claimName == null ? jwt.getSubject() : claimName;
    }
}
