package io.jokerr.spring.security.providers;

import io.jokerr.spring.security.authentication.CustomAuthentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.util.Assert;
import reactor.core.publisher.Mono;

public class CustomReactiveAuthenticationManager implements ReactiveAuthenticationManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomReactiveAuthenticationManager.class);

    private CustomAuthenticationProvider delegate;

    public CustomReactiveAuthenticationManager(CustomAuthenticationProvider provider) {
        if (provider == null) {
            throw new IllegalArgumentException("Provider cannot be null");
        }
        this.delegate = provider;
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        LOGGER.debug("Attempting Authentication {} with Delegate {}", authentication, delegate);
        if (authentication instanceof CustomAuthentication) {
            LOGGER.debug("Authentication is instance of CustomAuthentication");
            return Mono.fromSupplier(() -> delegate.authenticate(authentication));
        }
        return Mono.empty();
    }
}
