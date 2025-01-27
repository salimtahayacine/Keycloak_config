package com.stronglover.demo.test.keycloak.controler;

import com.stronglover.demo.test.keycloak.UserRegistrationRecord;
import com.stronglover.demo.test.keycloak.keycloak.KeycloakUserService;
import lombok.AllArgsConstructor;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

    private final KeycloakUserService keycloakUserService;

//    @PostMapping
//    public UserRegistrationRecord createUser(@RequestBody UserRegistrationRecord userRegistrationRecord) {
//
//        return keycloakUserService.createUser(userRegistrationRecord);
//    }

    @PostMapping
    public ResponseEntity<UserRegistrationRecord> createUser(@RequestBody UserRegistrationRecord userRegistrationRecord) {
        UserRegistrationRecord createdUser = keycloakUserService.createUser(userRegistrationRecord);
        return ResponseEntity.ok(createdUser);
    }

    @GetMapping
    public UserRepresentation getUser(Principal principal) {

        return keycloakUserService.getUserById(principal.getName());
    }

//    @GetMapping("/{userId}")
//    public ResponseEntity<?> getUserByID(@PathVariable String userId) {
//        return keycloakUserService.getUserResource(userId);
//    }


    @DeleteMapping("/{userId}")
    public void deleteUserById(@PathVariable String userId) {
        keycloakUserService.deleteUserById(userId);
    }

}
