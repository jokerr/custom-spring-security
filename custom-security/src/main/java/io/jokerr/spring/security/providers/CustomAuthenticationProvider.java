package io.jokerr.spring.security.providers;

import io.jokerr.spring.security.authentication.CustomAuthentication;
import io.jokerr.spring.security.authorities.AuthorityConverter;
import io.jokerr.spring.security.subject.CustomUser;
import io.jokerr.spring.security.subject.ExternalUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CustomAuthenticationProvider implements AuthenticationProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomAuthenticationProvider.class);

    public static final String HEADER_ID = "x-user-id";
    public static final String HEADER_ROLES = "x-user-roles";
    public static final String HEADER_NAME = "x-user-name";

    public static final List<String> SECURITY_HEADERS = Collections.unmodifiableList(Arrays.asList(
            HEADER_ID, HEADER_NAME, HEADER_ROLES
    ));

    private final CustomAuthenticationValidator validator;

    public CustomAuthenticationProvider(CustomAuthenticationValidator validator) {
        this.validator = validator;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        LOGGER.debug("Validating Authentication with {}", validator);
        try {
            validator.validate(authentication);
        }
        catch (AuthenticationException e) {
            // do cool logging here
            LOGGER.info("Failed to authenticate user: {}", e.getMessage());
            throw e;
        }

        // create principal
        HttpHeaders headers = (HttpHeaders)authentication.getCredentials();
        CustomUser user = buildUser(headers);

        // map roles
        AuthorityConverter authorityConverter = new AuthorityConverter() {};

        Authentication validAuth = new CustomAuthentication(
                headers,
                user,
                authorityConverter.transform(headers.getValuesAsList(HEADER_ROLES))
        );

        // set authenticated
        validAuth.setAuthenticated(true);
        LOGGER.debug("User {} Authenticated", user);
        return validAuth;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return CustomAuthentication.class.isAssignableFrom(authentication);
    }

    public static boolean isSecurityHeader(String header) {
        return SECURITY_HEADERS.contains(header.toLowerCase());
    }

    private CustomUser buildUser(HttpHeaders headers) {
        String id = headers.getFirst(HEADER_ID);
        return new ExternalUser(id);
    }
}
