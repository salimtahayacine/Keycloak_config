package com.stronglover.demo.test.keycloak;

import java.util.List;

public record UserRegistrationRecord(String username,
                                     String email,
                                     String password,
                                     String firstName,
                                     String lastName,
                                     List<String> roles // Add this field
) {
}
