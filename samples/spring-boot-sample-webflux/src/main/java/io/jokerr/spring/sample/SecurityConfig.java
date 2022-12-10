package io.jokerr.spring.sample;

import io.jokerr.spring.security.web.server.CustomAuthenticationWebFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;

@EnableWebFluxSecurity
// Enables Pre/PostAuthorize but not JSR250 just yet
// see https://docs.spring.io/spring-security/site/docs/current/reference/html5/#jc-erms
// see https://github.com/spring-projects/spring-security/issues/5103
@EnableReactiveMethodSecurity
public class SecurityConfig {
    @Autowired
    private ServerAuthenticationEntryPoint entryPoint;

    @Autowired
    private CustomAuthenticationWebFilter filter;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity httpSecurity) {
        return httpSecurity
                .csrf().disable()
                .httpBasic().disable()
                .formLogin().disable()
                .logout().disable()
                .exceptionHandling().authenticationEntryPoint(entryPoint)
                .and()
                .requestCache().disable()
                .authorizeExchange()
                // permit actuator
//                .matchers(EndpointRequest.toAnyEndpoint()).permitAll()
                // whitelist a sample
                .pathMatchers("/hello/unsecured").permitAll()
                .anyExchange().authenticated()
                .and()
                .addFilterAt(filter, SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }
}
