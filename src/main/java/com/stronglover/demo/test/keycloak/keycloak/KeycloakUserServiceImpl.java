package com.stronglover.demo.test.keycloak.keycloak;


import com.stronglover.demo.test.keycloak.ResetPassword;
import com.stronglover.demo.test.keycloak.UserRegistrationRecord;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.*;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class KeycloakUserServiceImpl implements KeycloakUserService {
    //private final KeycloakUserService keycloakUserService;

    @Value("${keycloak.realm}")
    private String realm;
    private Keycloak keycloak;

    public KeycloakUserServiceImpl(Keycloak keycloak) {
        this.keycloak = keycloak;
    }


    @Override
    public UserRegistrationRecord createUser(UserRegistrationRecord userRegistrationRecord) {

        try{

        UserRepresentation user=new UserRepresentation();
        user.setEnabled(true);
        user.setUsername(userRegistrationRecord.username());
        user.setEmail(userRegistrationRecord.email());
        user.setFirstName(userRegistrationRecord.firstName());
        user.setLastName(userRegistrationRecord.lastName());
        user.setEmailVerified(false);

        CredentialRepresentation credentialRepresentation=new CredentialRepresentation();
        credentialRepresentation.setValue(userRegistrationRecord.password());
        credentialRepresentation.setTemporary(false);
        credentialRepresentation.setType(CredentialRepresentation.PASSWORD);

        List<CredentialRepresentation> list = new ArrayList<>();
        list.add(credentialRepresentation);
        user.setCredentials(list);


        //RealmResource realmResource = keycloak.realm(realm);
        UsersResource usersResource = getUsersResource();

      //  Response response =  usersResource.create(user);
       Response response = usersResource.create(user);
        log.info("Status code: {}", response.getStatus());


        if(Objects.equals(201,response.getStatus())){

            List<UserRepresentation> representationList = usersResource
                    .searchByUsername(userRegistrationRecord.username(), true);
            if(!CollectionUtils.isEmpty(representationList)){
                UserRepresentation userRepresentation1 = representationList
                        .stream().filter(userRepresentation -> Objects
                                .equals(false, userRepresentation.isEmailVerified()))
                        .findFirst().orElse(null);
                assert userRepresentation1 != null;

                // Assign roles to the user
                if (userRegistrationRecord.roles() != null && !userRegistrationRecord.roles().isEmpty()) {
                    assignRoles(userRepresentation1.getId(), userRegistrationRecord.roles());
                }
               // emailVerification(userRepresentation1.getId());
            }
            return  userRegistrationRecord;
        }
            if (response.getStatus() != 201) {
                log.error("Failed to create user. Status: {}", response.getStatus());
                // You might want to read the error response
                String error = response.readEntity(String.class);
                log.error("Error response: {}", error);
            }



        return null;

        } catch (Exception e) {
            log.error("Error creating user", e);
            throw e;
        }

    }

    private UsersResource getUsersResource() {
        RealmResource realm1 = keycloak.realm(realm);
        return realm1.users();
    }

    @Override
    public void assignRoles(String userId, List<String> roles) {
        UserResource userResource = getUserResource(userId);
        RealmResource realmResource = keycloak.realm(realm);

        // Get the realm roles
        RoleScopeResource roleScopeResource = userResource.roles().realmLevel();
        List<RoleRepresentation> roleRepresentations = realmResource.roles().list().stream()
                .filter(role -> roles.contains(role.getName()))
                .collect(Collectors.toList());

        // Assign the roles to the user
        roleScopeResource.add(roleRepresentations);
    }
    @Override
    public void assignClientRoles(String userId, String clientId, List<String> roles) {
        UserResource userResource = getUserResource(userId);
        RealmResource realmResource = keycloak.realm(realm);

        // Get the client representation
        ClientsResource clientsResource = realmResource.clients();
        ClientRepresentation clientRepresentation = clientsResource.findByClientId(clientId).stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Client not found"));

        // Get the client roles
        RoleScopeResource roleScopeResource = userResource.roles().clientLevel(clientRepresentation.getId());
        List<RoleRepresentation> roleRepresentations = realmResource
                .clients().get(clientRepresentation.getId()).roles().list().stream()
                .filter(role -> roles.contains(role.getName()))
                .collect(Collectors.toList());

        // Assign the roles to the user
        roleScopeResource.add(roleRepresentations);
    }


//    @Override
//    public UserRepresentation getUser(String username) {
//        return null;
//    }

    @Override
    public UserRepresentation getUserById(String userId) {
        return  getUsersResource().get(userId).toRepresentation();
    }

    @Override
    public void deleteUserById(String userId) {
        getUsersResource().delete(userId);
    }
    @Override
    public void emailVerification(String userId){

        UsersResource usersResource = getUsersResource();
        usersResource.get(userId).sendVerifyEmail();
    }

    public UserResource getUserResource(String userId){
        UsersResource usersResource = getUsersResource();
        return usersResource.get(userId);
    }

    @Override
    public void updatePassword(String userId) {

        UserResource userResource = getUserResource(userId);
        List<String> actions= new ArrayList<>();
        actions.add("UPDATE_PASSWORD");
        userResource.executeActionsEmail(actions);

    }

    @Override
    public void updatePassword(ResetPassword resetPassword, String userId) {


        UserResource userResource = getUserResource(userId);
        CredentialRepresentation credentialRepresentation=new CredentialRepresentation();
        credentialRepresentation.setValue(resetPassword.password());
        credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
        credentialRepresentation.setTemporary(false);
        userResource.resetPassword(credentialRepresentation);
    }

}
