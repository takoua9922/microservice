package com.esprit.ms.auth.service;

import com.esprit.ms.auth.dto.UserRegistrationRequest;
import com.esprit.ms.auth.dto.UserRegistrationResponse;
import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Service for managing user registration with Keycloak
 */
@Service
public class UserRegistrationService {

    private static final Logger logger = LoggerFactory.getLogger(UserRegistrationService.class);

    private final Keycloak keycloak;
    private final String realm;

    public UserRegistrationService(Keycloak keycloak, @Value("${keycloak.realm}") String realm) {
        this.keycloak = keycloak;
        this.realm = realm;
    }

    /**
     * Register a new user in Keycloak
     */
    public UserRegistrationResponse registerUser(UserRegistrationRequest request) {
        try {
            // Get the realm and users resource
            RealmResource realmResource = keycloak.realm(realm);
            UsersResource usersResource = realmResource.users();

            // Check if user already exists
            if (userExists(usersResource, request.getUsername())) {
                logger.warn("User with username {} already exists", request.getUsername());
                return UserRegistrationResponse.error("User with this username already exists");
            }

            // Create user representation
            UserRepresentation user = new UserRepresentation();
            user.setUsername(request.getUsername());
            user.setEmail(request.getEmail());
            user.setFirstName(request.getFirstName());
            user.setLastName(request.getLastName());
            user.setEnabled(true);
            user.setEmailVerified(false); // You can set this to true if email verification is not required

            // Create the user in Keycloak
            Response response = usersResource.create(user);
            
            if (response.getStatus() != 201) {
                logger.error("Failed to create user. Status: {}", response.getStatus());
                return UserRegistrationResponse.error("Failed to create user: " + response.getStatusInfo());
            }

            // Extract user ID from location header
            String userId = extractUserIdFromResponse(response);
            logger.info("User created successfully with ID: {}", userId);

            // Set password
            setUserPassword(usersResource, userId, request.getPassword());

            // Assign roles if provided
            if (request.getRoles() != null && !request.getRoles().isEmpty()) {
                assignRolesToUser(realmResource, userId, request.getRoles());
            } else {
                // Assign default role (e.g., "user")
                assignRolesToUser(realmResource, userId, Set.of("user"));
            }

            response.close();
            return UserRegistrationResponse.success(userId);

        } catch (Exception e) {
            logger.error("Error registering user: {}", e.getMessage(), e);
            return UserRegistrationResponse.error("Error registering user: " + e.getMessage());
        }
    }

    /**
     * Check if a user with the given username already exists
     */
    private boolean userExists(UsersResource usersResource, String username) {
        return !usersResource.search(username, true).isEmpty();
    }

    /**
     * Set password for the user
     */
    private void setUserPassword(UsersResource usersResource, String userId, String password) {
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);
        credential.setTemporary(false); // Set to true if you want to force password change on first login

        usersResource.get(userId).resetPassword(credential);
        logger.info("Password set for user ID: {}", userId);
    }

    /**
     * Assign roles to the user
     * Note: This assumes realm-level roles. Adjust if you need client-level roles.
     */
    private void assignRolesToUser(RealmResource realmResource, String userId, Set<String> roleNames) {
        try {
            var availableRoles = realmResource.roles().list();
            var rolesToAssign = availableRoles.stream()
                    .filter(role -> roleNames.contains(role.getName()))
                    .toList();

            if (!rolesToAssign.isEmpty()) {
                realmResource.users().get(userId).roles().realmLevel().add(rolesToAssign);
                logger.info("Assigned roles {} to user ID: {}", roleNames, userId);
            } else {
                logger.warn("No matching roles found for: {}", roleNames);
            }
        } catch (Exception e) {
            logger.error("Error assigning roles to user: {}", e.getMessage(), e);
        }
    }

    /**
     * Extract user ID from the Location header of the response
     */
    private String extractUserIdFromResponse(Response response) {
        String location = response.getHeaderString("Location");
        if (location != null) {
            String[] parts = location.split("/");
            return parts[parts.length - 1];
        }
        return null;
    }
}
