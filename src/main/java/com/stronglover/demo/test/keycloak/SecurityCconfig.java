package com.stronglover.demo.test.keycloak;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityCconfig {

    private final JwtAuthConverter jwtAuthConverter = new JwtAuthConverter();
    @Bean
    SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
//        http
//                .csrf().disable()
//                //.requestHttpMatchers("/users/**").permitAll()
//                .authorizeHttpRequests()
//                .requestMatchers("/users/**").permitAll()
//                //.antMatchers("/demo/**").authenticated()
//                .anyRequest().authenticated();
//
//        http
//                .oauth2ResourceServer()
//                .jwt()
//                .jwtAuthenticationConverter(jwtAuthConverter);
//        http
//                .sessionManagement()
//
//                .sessionCreationPolicy(STATELESS);
//        return http.build();
        return httpSecurity
                .csrf(csrf -> csrf.disable())  // disable CSRF
                .cors(cors -> cors.disable())  // if you need CORS, configure it properly
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/users/**").permitAll()
                        .requestMatchers("/error").permitAll()  // Allow error endpoints
                        .requestMatchers("/actuator/**").permitAll()  // If you're using actuator
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .jwtAuthenticationConverter(jwtAuthConverter)
                        )
                )
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .build();

    }

//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer() {
//
//        return (web) -> {
//            web.ignoring().requestMatchers(
//                    HttpMethod.POST,
//                    //"/public/**",
//                    "/users"
//            );
//            web.ignoring().requestMatchers(
//                    HttpMethod.GET,
//                    "/public/**"
//            );
////            web.ignoring().requestMatchers(
////                    HttpMethod.DELETE,
////                    "/public/**"
////            );
////            web.ignoring().requestMatchers(
////                    HttpMethod.PUT,
////                    "/public/**"
////            );
////            web.ignoring().requestMatchers(
////                            HttpMethod.OPTIONS,
////                            "/**"
////                    )
////                    .requestMatchers("/v3/api-docs/**", "/configuration/**", "/swagger-ui/**",
////                            "/swagger-resources/**", "/swagger-ui.html", "/webjars/**", "/api-docs/**");
//
//        };
//    }

}
