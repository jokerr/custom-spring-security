package io.jokerr.spring.sample;

import io.jokerr.spring.security.subject.CustomUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class HelloService {
    private static final Logger LOGGER = LoggerFactory.getLogger(HelloService.class);

    public String getGreeting() {
        CustomUser user = (CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        LOGGER.info("User {} in method getGreeting", user);
        return "Greeting";
    }
}
