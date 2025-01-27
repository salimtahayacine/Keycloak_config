package com.stronglover.demo.test.keycloak;

public record UserRegistrationRecord(String username,
                                     String email,
                                     String password,
                                     String firstName,
                                     String lastName) {
}
