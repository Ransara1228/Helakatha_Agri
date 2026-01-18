package com.helaketha.agri_new.agri.service;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.ws.rs.core.Response;
import java.util.Collections;
import java.util.List;

@Service
public class KeycloakAdminService {

    @Value("${keycloak.server-url:http://localhost:8090}")
    private String serverUrl;

    @Value("${keycloak.realm:helakatha-agri-realm}")
    private String realm;

    @Value("${keycloak.admin-client-id:helaketha-agri-admin}")
    private String clientId;

    @Value("${keycloak.admin-client-secret:admin-client-secret-change-me}")
    private String clientSecret;

    /**
     * Get Keycloak admin client instance
     */
    private Keycloak getKeycloakAdminClient() {
        return KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(realm)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .grantType("client_credentials")
                .build();
    }

    /**
     * Create a new user in Keycloak
     * @param username Username for the user
     * @param email Email address
     * @param password Password for the user
     * @param firstName First name
     * @param lastName Last name
     * @param role Role to assign (e.g., "FARMER", "TRACTOR_DRIVER", etc.)
     * @return Keycloak user ID if successful, null otherwise
     */
    public String createUser(String username, String email, String password, String firstName, String lastName, String role) {
        try {
            Keycloak keycloak = getKeycloakAdminClient();
            RealmResource realmResource = keycloak.realm(realm);
            UsersResource usersResource = realmResource.users();

            // Check if user already exists
            List<UserRepresentation> existingUsers = usersResource.search(username, true);
            if (!existingUsers.isEmpty()) {
                throw new RuntimeException("User with username '" + username + "' already exists in Keycloak");
            }

            // Create user representation
            UserRepresentation user = new UserRepresentation();
            user.setUsername(username);
            user.setEmail(email);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setEnabled(true);
            user.setEmailVerified(true);

            // Create user
            Response response = usersResource.create(user);

            if (response.getStatus() == Response.Status.CREATED.getStatusCode()) {
                // Get the user ID from the location header
                String userId = getUserIdFromLocation(response.getLocation().getPath());

                // Set password
                CredentialRepresentation credential = new CredentialRepresentation();
                credential.setType(CredentialRepresentation.PASSWORD);
                credential.setValue(password);
                credential.setTemporary(false);

                usersResource.get(userId).resetPassword(credential);

                // Assign role
                if (role != null && !role.isEmpty()) {
                    assignRoleToUser(userId, role);
                }

                return userId;
            } else {
                String errorMessage = response.readEntity(String.class);
                throw new RuntimeException("Failed to create user in Keycloak: " + errorMessage);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error creating user in Keycloak: " + e.getMessage(), e);
        }
    }

    /**
     * Assign a realm role to a user
     */
    private void assignRoleToUser(String userId, String roleName) {
        try {
            Keycloak keycloak = getKeycloakAdminClient();
            RealmResource realmResource = keycloak.realm(realm);
            
            // Get the role
            var role = realmResource.roles().get(roleName).toRepresentation();
            
            // Assign role to user
            realmResource.users().get(userId).roles().realmLevel()
                    .add(Collections.singletonList(role));
        } catch (Exception e) {
            // Log error but don't fail user creation
            System.err.println("Warning: Failed to assign role '" + roleName + "' to user: " + e.getMessage());
        }
    }

    /**
     * Extract user ID from Keycloak location header
     */
    private String getUserIdFromLocation(String location) {
        // Location format: /admin/realms/{realm}/users/{userId}
        String[] parts = location.split("/");
        return parts[parts.length - 1];
    }

    /**
     * Delete a user from Keycloak
     */
    public void deleteUser(String keycloakUserId) {
        try {
            Keycloak keycloak = getKeycloakAdminClient();
            RealmResource realmResource = keycloak.realm(realm);
            realmResource.users().get(keycloakUserId).remove();
        } catch (Exception e) {
            throw new RuntimeException("Error deleting user from Keycloak: " + e.getMessage(), e);
        }
    }

    /**
     * Update user password in Keycloak
     */
    public void updateUserPassword(String keycloakUserId, String newPassword) {
        try {
            Keycloak keycloak = getKeycloakAdminClient();
            RealmResource realmResource = keycloak.realm(realm);
            
            CredentialRepresentation credential = new CredentialRepresentation();
            credential.setType(CredentialRepresentation.PASSWORD);
            credential.setValue(newPassword);
            credential.setTemporary(false);

            realmResource.users().get(keycloakUserId).resetPassword(credential);
        } catch (Exception e) {
            throw new RuntimeException("Error updating user password in Keycloak: " + e.getMessage(), e);
        }
    }
}

