package com.timolisa.booksapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

// This is a configuration class for enabling API Key authentication in a Spring Boot application
// using Spring Security.

@Configuration // tells Spring that this is a configuration class for configuring the Spring Security
@EnableWebSecurity // used to enable web Security in Spring Security.
public class SecurityConfig {
    // @Value is used to inject values from a properties file into variables.
    @Value("${timolisa.booksAPI.api-key.key}")
    private String principalRequestHeader; // the key of the header to look for.

    @Value("${timolisa.booksAPI.api-key.value}")
    private String principalRequestValue;
    // what we are expecting the value of that header to be.
    // in order for that user to be authenticated.

    // we are going to look for the header using the principalRequestHeader
    // and then match it against the expected value of the principalRequestValue
    // and if they match, then that user is authenticated, or not.

    // the userDetailsService method returns an "InMemoryUserDetailsManager" object
    // which is used to define the user details for authentication.

    @Bean
    public UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User.withDefaultPasswordEncoder()
                .username("user")
                .password("password")
                .roles("USER")
                .build());
        return manager;
    }

    // The SecurityFilterChain bean is defined in the filterChain() method.
    // This bean is responsible for creating the authentication filter chain
    // that will be used to secure incoming requests.

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        final APIKeyAuthFilter filter = new APIKeyAuthFilter(principalRequestHeader);

        // the filter.setAuthenticationManager() method is used to define the authentication manager
        // which checks if the API Key provided by the user in the request header matches the expected
        // API Key value. If they match, then the user is authenticated and the request is allowed to
        // proceed.

        filter.setAuthenticationManager((Authentication authentication) -> {
            final String principal = (String) authentication.getPrincipal();
            if (!principalRequestValue.equals(principal)) {
                throw new BadCredentialsException("User did not provide valid API Key");
            }
            authentication.setAuthenticated(true);
            return authentication;
        });

        // the http.sessionManagement() is used to configure the session management and sets
        // the session creation policy to "STATELESS" which means that the application will not
        // create a session and will not use it to store any authentication data.

        // addFilter() is used to add the "APIKeyAuthFilter" to the filter chain and the
        // "authorizeHttpRequests()" is used to authorize HTTPRequests.

        // finally anyRequest().authenticated() is used to ensure that the user can access
        // any endpoint in the application.

        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .csrf().disable()
                .addFilter(filter).authorizeHttpRequests()
                .anyRequest().authenticated();

        return http.build();
    }
}
