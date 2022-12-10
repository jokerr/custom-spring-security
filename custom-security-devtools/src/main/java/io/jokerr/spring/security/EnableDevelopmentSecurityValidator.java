package io.jokerr.spring.security;

import io.jokerr.spring.security.providers.CustomAuthenticationValidator;
import io.jokerr.spring.security.providers.DevCustomAuthenticationValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
@Documented
@Import(EnableDevelopmentSecurityValidator.DevelopmentValidatorConfiguration.class)
public @interface EnableDevelopmentSecurityValidator {

    class DevelopmentValidatorConfiguration {
        @Bean
        CustomAuthenticationValidator customAuthenticationValidator() {
            return new DevCustomAuthenticationValidator();
        }
    }
}
