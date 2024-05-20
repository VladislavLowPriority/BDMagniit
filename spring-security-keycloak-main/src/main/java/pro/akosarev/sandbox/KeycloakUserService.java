package pro.akosarev.sandbox;

import java.util.Collections;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import jakarta.ws.rs.core.Response;
@Service
public class KeycloakUserService {

    private static final Logger logger = LoggerFactory.getLogger(KeycloakUserService.class);

    private final Keycloak keycloak;

    public KeycloakUserService() {
        this.keycloak = KeycloakBuilder.builder()
                .serverUrl("http://localhost:8080/auth")
                .realm("eselpo")
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .clientId("springsecurity")
                .clientSecret("yJC7CbTdxjIx24RzmFsYONYqCVH9Cl4Q")
                .build();
    }

    public boolean createUser(String username, String email, String firstName, String lastName, String password) {
        UsersResource usersResource = keycloak.realm("eselpo").users();

        UserRepresentation user = new UserRepresentation();
        user.setUsername(username);
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);

        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);
        credential.setTemporary(false);
        user.setCredentials(Collections.singletonList(credential));

        Response response = null;
        try {
            response = usersResource.create(user);
            boolean userCreated = response.getStatus() == 201;

            if (userCreated) {
                logger.info("User {} created successfully", username);
            } else {
                logger.error("Failed to create user {}: status {}", username, response.getStatus());
                if (response.hasEntity()) {
                    logger.error("Response entity: {}", response.readEntity(String.class));
                }
            }
            return userCreated;
        } catch (Exception e) {
            logger.error("Error creating user {}", username, e);
            return false;
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }
}