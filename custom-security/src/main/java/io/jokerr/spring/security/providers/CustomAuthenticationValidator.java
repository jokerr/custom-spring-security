package io.jokerr.spring.security.providers;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public interface CustomAuthenticationValidator {
    default void validate(Authentication authentication) throws AuthenticationException {
        if (authentication.getCredentials() == null) {
            throw new BadCredentialsException("No credential found");
        }
        // do some actual validation here
        HttpHeaders creds = ((HttpHeaders) authentication.getCredentials());
        if (!creds.containsKey(CustomAuthenticationProvider.HEADER_ID) ||
                creds.getFirst(CustomAuthenticationProvider.HEADER_ID).trim().isEmpty()) {
            throw new BadCredentialsException("Invalid credential, no ID!");
        }

        String id = creds.getFirst(CustomAuthenticationProvider.HEADER_ID).trim();
        if (!id.startsWith("X-")) {
            throw new BadCredentialsException("Invalid credential, must start with X-");
        }
    }
}
