package pro.akosarev.sandbox;



import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@CrossOrigin
@RestController
@RequestMapping("/auth")
public class TokenController {

	private static final Logger log = LoggerFactory.getLogger(TokenController.class);

	@GetMapping("/token")
public ResponseEntity<?> getToken() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication != null) {
        log.info("Пользователь аутентифицирован");

        if (authentication.getPrincipal() instanceof OidcUser) {
            OidcUser oidcUser = (OidcUser) authentication.getPrincipal();
            // Получаем токен доступа из OIDC пользователя
            String accessToken = oidcUser.getIdToken().getTokenValue();

            log.info("Access Token: {}", accessToken); // Выводим токен в лог

            return ResponseEntity.ok().body(Collections.singletonMap("access_token", accessToken));
        } else {
            log.info("Polzovatel ne yavlyaetsya ekzemplyarom OidcUser");
        }
    } else {
        log.info("Authentication is null");
    }

    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
}
}

