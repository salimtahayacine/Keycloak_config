package com.stronglover.demo.test.keycloak.keycloak;

import com.stronglover.demo.test.keycloak.ResetPassword;
import com.stronglover.demo.test.keycloak.UserRegistrationRecord;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.List;

public interface KeycloakUserService {

    UserRegistrationRecord createUser(UserRegistrationRecord userRegistrationRecord);

    void assignRoles(String userId, List<String> roles);

    void assignClientRoles(String userId, String clientId, List<String> roles);

    UserRepresentation getUserById(String userId);
    void deleteUserById(String userId);
    void emailVerification(String userId);
    UserResource getUserResource(String userId);
    void updatePassword(String userId);
    void updatePassword(ResetPassword resetPassword,String userId);

}
