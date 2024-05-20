package pro.akosarev.sandbox;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;

@Configuration
@EnableScheduling
@SpringBootApplication
public class SpringSecurityKeycloakSandbox {

    public static void main(String[] args) {
        
        SpringApplication.run(SpringSecurityKeycloakSandbox.class, args);
        
        
    }

    @Bean
public OidcClientInitiatedLogoutSuccessHandler oidcLogoutSuccessHandler(ClientRegistrationRepository clientRegistrationRepository) {
    OidcClientInitiatedLogoutSuccessHandler logoutSuccessHandler = new OidcClientInitiatedLogoutSuccessHandler(clientRegistrationRepository);
    logoutSuccessHandler.setPostLogoutRedirectUri("http://localhost:8081/homepage.html");
    return logoutSuccessHandler;
}

@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http, OidcClientInitiatedLogoutSuccessHandler oidcClientInitiatedLogoutSuccessHandler) throws Exception {
    return http
        .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
        .oauth2Login(Customizer.withDefaults())
        .authorizeHttpRequests(c -> c
            .requestMatchers("/error").permitAll()
            .requestMatchers("/homepage.html").permitAll()
            .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
            .requestMatchers("/Admin.html", "/AdminUserReg.html" ).hasRole("MANAGER")
            .anyRequest().authenticated())
        .csrf(AbstractHttpConfigurer::disable)
        .exceptionHandling(exceptionHandling -> exceptionHandling
            .accessDeniedHandler((request, response, accessDeniedException) -> {
                response.sendRedirect("/access-denied.html");
            }))
        .logout(logout -> logout.
        logoutUrl("/logout")
        .logoutSuccessHandler(oidcClientInitiatedLogoutSuccessHandler))
    .build();
}

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        var converter = new JwtAuthenticationConverter();
        var jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        converter.setPrincipalClaimName("preferred_username");
        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            var authorities = jwtGrantedAuthoritiesConverter.convert(jwt);
            var roles = jwt.getClaimAsStringList("spring_sec_roles");

            return Stream.concat(authorities.stream(),
                            roles.stream()
                                    .filter(role -> role.startsWith("ROLE_"))
                                    .map(SimpleGrantedAuthority::new)
                                    .map(GrantedAuthority.class::cast))
                    .toList();
        });

        return converter;
    }

    @Bean
    public OAuth2UserService<OidcUserRequest, OidcUser> oAuth2UserService() {
        var oidcUserService = new OidcUserService();
        return userRequest -> {
            var oidcUser = oidcUserService.loadUser(userRequest);
            var roles = oidcUser.getClaimAsStringList("spring_sec_roles");
            var authorities = Stream.concat(oidcUser.getAuthorities().stream(),
                            roles.stream()
                                    .filter(role -> role.startsWith("ROLE_"))
                                    .map(SimpleGrantedAuthority::new)
                                    .map(GrantedAuthority.class::cast))
                    .toList();

            return new DefaultOidcUser(authorities, oidcUser.getIdToken(), oidcUser.getUserInfo());
            
        };
    }

 
    
    
    
    @Autowired
    private UserRepository userRepository;

    @Transactional
    @Scheduled(fixedRate = 10000)
    public void synchronizeUsers() {
        Keycloak keycloak = KeycloakBuilder.builder()
            .serverUrl("http://localhost:8080/")
            .realm("eselpo")
            .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
            .clientId("springsecurity")
            .clientSecret("yJC7CbTdxjIx24RzmFsYONYqCVH9Cl4Q")
            .build();
        
        List<UserRepresentation> keycloakUsers = keycloak.realm("eselpo").users().list();
        for (UserRepresentation keycloakUser : keycloakUsers) {
            String keycloakUserId = keycloakUser.getId();
            Optional<User> existingUser = userRepository.findById(keycloakUserId);
            if (!existingUser.isPresent()) {
                User newUser = new User();
                newUser.setId(keycloakUserId);
                userRepository.save(newUser);
            }
        }
    }

    @PostConstruct
    public void init() {
        synchronizeUsers();
    }
    
    
}


    

