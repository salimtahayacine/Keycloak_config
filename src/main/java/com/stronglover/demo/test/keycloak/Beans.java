package com.stronglover.demo.test.keycloak;

import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Beans {

    @Value("${keycloak.urls.auth}")
    private String keycloakUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.admin-client-id}")
    private String client;

    @Value("${keycloak.admin-client-secret}")
    private String admintClientSecret;

//    @Value("${keycloak.credentials.secret}")
//    private String admintClientSecret;

    @Bean
    public Keycloak keycloak() {
        //KeycloakBuilder keycloakBuilder = keycloakBuilder().builder();

        return KeycloakBuilder.builder()
                .serverUrl(keycloakUrl)
                .realm(realm)
                .clientId(client)
                .clientSecret(admintClientSecret)
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
//                .resteasyClient(
//                        new ResteasyClientBuilder()
//                                .connectionPoolSize(10)
//                                .build()
//                )
                .build();
    }
}
