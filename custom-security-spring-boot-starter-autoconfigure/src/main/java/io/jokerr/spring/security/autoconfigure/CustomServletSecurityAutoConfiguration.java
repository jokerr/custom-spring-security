package io.jokerr.spring.security.autoconfigure;

import io.jokerr.spring.security.providers.CustomAuthenticationProvider;
import io.jokerr.spring.security.providers.CustomAuthenticationValidator;
import io.jokerr.spring.security.web.CustomAuthenticationConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationConverter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Configuration
@ConditionalOnClass(HttpServletRequest.class)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class CustomServletSecurityAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public AuthenticationEntryPoint unauthorizedEntryPoint() {
        return (request, response, authException) ->
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication not found");
    }

    @Bean
    @ConditionalOnMissingBean
    public CustomAuthenticationProvider authenticationProvider() {
        return new CustomAuthenticationProvider(customAuthenticationValidator());
    }

    @Bean
    @ConditionalOnMissingBean
    public CustomAuthenticationValidator customAuthenticationValidator() {
        return new CustomAuthenticationValidator() {};
    }

    @Bean
    @ConditionalOnMissingBean
    public AuthenticationConverter authenticationConverter() {
        return new CustomAuthenticationConverter();
    }
}
