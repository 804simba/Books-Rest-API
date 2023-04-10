package com.timolisa.booksapi.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

/**
 * This class is used to implement the authentication logic for the API Key-based authentication
 * It takes in the header key that contains the API Key as an argument in its constructor
 * */

public class APIKeyAuthFilter extends AbstractPreAuthenticatedProcessingFilter {
    // setting up api keys
    private final String principalRequestHeader;

    public APIKeyAuthFilter(String principalRequestHeader) {
        this.principalRequestHeader = principalRequestHeader;
    }

    @Override
    protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
        return request.getHeader(principalRequestHeader);
    }

    @Override
    protected Object getPreAuthenticatedCredentials(HttpServletRequest request) {
        return null;
    }
}
