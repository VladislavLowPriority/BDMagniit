package pro.akosarev.sandbox;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);
    private final KeycloakUserService keycloakUserService;

    @Autowired
    public AdminController(KeycloakUserService keycloakUserService) {
        this.keycloakUserService = keycloakUserService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRegistrationDto registrationDto) {
        try {
            boolean success = keycloakUserService.createUser(
                    registrationDto.getUsername(),
                    registrationDto.getEmail(),
                    registrationDto.getFirstName(),
                    registrationDto.getLastName(),
                    registrationDto.getPassword()
            );

            if (success) {
                logger.info("User {} created successfully", registrationDto.getUsername());
                return ResponseEntity.ok().body("User created successfully");
            } else {
                logger.error("Failed to create user {}", registrationDto.getUsername());
                return ResponseEntity.badRequest().body("User creation failed");
            }
        } catch (Exception e) {
            logger.error("Exception occurred when trying to create user {}: {}", registrationDto.getUsername(), e.getMessage());
            return ResponseEntity.status(500).body("An error occurred while creating the user");
        }
    }
}