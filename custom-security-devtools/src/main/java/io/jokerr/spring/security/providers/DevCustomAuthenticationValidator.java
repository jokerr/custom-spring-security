package io.jokerr.spring.security.providers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import javax.annotation.PostConstruct;

public class DevCustomAuthenticationValidator implements CustomAuthenticationValidator {
    private static final Logger LOGGER = LoggerFactory.getLogger(DevCustomAuthenticationValidator.class);

    @Value("${io.jokerr.custom.enable-dev-validator:false}")
    private Boolean isEnabled;

    @PostConstruct
    public void init() {
        if (!isEnabled) {
            throw new IllegalStateException("DevCustomAuthenticationValidator is not enabled!");
        }
        LOGGER.warn("Dev Custom Authentication Validator is ACTIVE!!!!");
    }

    @Override
    public void validate(Authentication authentication) throws AuthenticationException {
        LOGGER.info("Validating Authentication");

        if (authentication.getCredentials() == null) {
            throw new BadCredentialsException("No credential found");
        }

        // need at least an ID....
        HttpHeaders creds = ((HttpHeaders) authentication.getCredentials());
        if (!creds.containsKey(CustomAuthenticationProvider.HEADER_ID) ||
                creds.getFirst(CustomAuthenticationProvider.HEADER_ID).trim().isEmpty()) {
            throw new BadCredentialsException("Invalid credential, no ID!");
        }
    }
}
