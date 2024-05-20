package pro.akosarev.sandbox;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.
RestController;
@RestController
@RequestMapping("/api/user")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    private final UserRepository userRepository;

    
    @Autowired
    public UserController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }



    public String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            log.info("Authentication object is not null");
            if (authentication.getPrincipal() instanceof Jwt) {
                Jwt jwt = (Jwt) authentication.getPrincipal();
                String id = jwt.getSubject();
                log.info("User id from JWT: {}", id);
                return id;
            } else {
                log.info("Principal is not an instance of JWT. Actual class: {}", authentication.getPrincipal().getClass());
            }
        } else {
            log.info("Authentication object is null");
        }
        return null;
    }

    @PostMapping("/save")
public ResponseEntity<?> saveUser(@RequestBody UserDto userDto) {
    log.info("Received user DTO: {}", userDto);
    String id = getCurrentUserId();
    if (id == null) {
        log.info("User ID is null, returning UNAUTHORIZED");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    try {
        User user = userService.saveOrUpdateUserData(id, userDto);
        log.info("User data saved successfully: {}", user);
        return ResponseEntity.ok(user);
    } catch (Exception e) {
        log.error("Error saving user data", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }


}
@GetMapping("/profile")
public ResponseEntity<?> getUserProfile() {
    String id = getCurrentUserId();
    if (id == null) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    Optional<User> user = userRepository.findById(id);
    if (user.isPresent()) {
        return ResponseEntity.ok(user.get());
    } else {
        return ResponseEntity.notFound().build();
    }
}
}