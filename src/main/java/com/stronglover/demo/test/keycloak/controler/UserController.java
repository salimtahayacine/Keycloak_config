package com.stronglover.demo.test.keycloak.controler;

import com.stronglover.demo.test.keycloak.UserRegistrationRecord;
import com.stronglover.demo.test.keycloak.keycloak.KeycloakUserService;
import lombok.AllArgsConstructor;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

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

    // Assign roles to a user
    @PostMapping("/{userId}/assign-roles")
    public ResponseEntity<Void> assignRoles(@PathVariable String userId, @RequestBody List<String> roles){
        keycloakUserService.assignRoles(userId, roles);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // Assign client roles to a user
    @PostMapping("/{userId}/assign-client-roles")
    public ResponseEntity<Void> assignClientRoles(
            @PathVariable String userId,
            @RequestParam String clientId,
            @RequestBody List<String> roles) {
        keycloakUserService.assignClientRoles(userId, clientId, roles);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
