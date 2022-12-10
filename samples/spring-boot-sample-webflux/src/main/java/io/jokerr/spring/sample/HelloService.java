package io.jokerr.spring.sample;

import io.jokerr.spring.security.subject.CustomUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class HelloService {
    private static final Logger LOGGER = LoggerFactory.getLogger(HelloService.class);

    public Mono<String> getGreeting() {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .map(Authentication::getPrincipal)
                .cast(CustomUser.class)
                .map(user -> {
                    LOGGER.info("User {} in getGreeting", user);
                    return "Hello " + user.getName();
                });
    }
}
