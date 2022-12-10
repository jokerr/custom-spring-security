package io.jokerr.spring.security.autoconfigure;

import io.jokerr.spring.security.providers.CustomAuthenticationProvider;
import io.jokerr.spring.security.providers.CustomAuthenticationValidator;
import io.jokerr.spring.security.providers.CustomReactiveAuthenticationManager;
import io.jokerr.spring.security.web.server.CustomAuthenticationWebFilter;
import io.jokerr.spring.security.web.server.CustomServerAuthenticationConverter;
import io.jokerr.spring.security.web.server.CustomServerAuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.*;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;

@Configuration
@ConditionalOnClass(WebFilterExchange.class)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
public class CustomServerSecurityAutoConfiguration {
    @Autowired
    private ReactiveAuthenticationManager authenticationManager;
    @Autowired
    private ServerAuthenticationSuccessHandler successHandler;
    @Autowired
    private ServerAuthenticationFailureHandler failureHandler;
    @Autowired
    private ServerSecurityContextRepository securityContextRepository;
    @Autowired
    private ServerAuthenticationConverter authenticationConverter;

    @Bean
    @ConditionalOnMissingBean
    public CustomAuthenticationWebFilter authenticationWebFilter() {
        return new CustomAuthenticationWebFilter(authenticationManager,
                securityContextRepository,
                authenticationConverter,
                successHandler,
                failureHandler);
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
    public CustomReactiveAuthenticationManager authenticationManager() {
        return new CustomReactiveAuthenticationManager(authenticationProvider());
    }

    @Bean
    @ConditionalOnMissingBean
    public CustomServerAuthenticationConverter authenticationConverter() {
        return new CustomServerAuthenticationConverter();
    }

    @Bean
    @ConditionalOnMissingBean
    public ServerAuthenticationSuccessHandler successHandler() {
        return new CustomServerAuthenticationSuccessHandler();
    }

    @Bean
    @ConditionalOnMissingBean
    public ServerAuthenticationFailureHandler failureHandler() {
        return new ServerAuthenticationEntryPointFailureHandler(new HttpStatusServerEntryPoint(HttpStatus.UNAUTHORIZED));
    }

    @Bean
    @ConditionalOnMissingBean
    public ServerSecurityContextRepository securityContextRepository() {
        // Default to a stateless repository
        return NoOpServerSecurityContextRepository.getInstance();
    }

    @Bean
    @ConditionalOnMissingBean
    public ServerAuthenticationEntryPoint authenticationEntryPoint() {
        return new HttpStatusServerEntryPoint(HttpStatus.UNAUTHORIZED);
    }
}
