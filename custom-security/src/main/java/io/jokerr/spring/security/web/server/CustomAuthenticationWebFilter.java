package io.jokerr.spring.security.web.server;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

public class CustomAuthenticationWebFilter implements WebFilter {
    private final ReactiveAuthenticationManager authenticationManager;
    private final ServerSecurityContextRepository securityContextRepository;
    private final ServerAuthenticationConverter authenticationConverter;
    private ServerAuthenticationSuccessHandler successHandler;
    private ServerAuthenticationFailureHandler failureHandler;

    public CustomAuthenticationWebFilter(ReactiveAuthenticationManager authenticationManager,
                                         ServerSecurityContextRepository securityContextRepository,
                                         ServerAuthenticationConverter authenticationConverter,
                                         ServerAuthenticationSuccessHandler successHandler,
                                         ServerAuthenticationFailureHandler failureHandler) {
        this.authenticationManager = authenticationManager;
        this.securityContextRepository = securityContextRepository;
        this.authenticationConverter = authenticationConverter;
        this.successHandler = successHandler;
        this.failureHandler = failureHandler;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        return this.authenticationConverter.convert(exchange)
                .switchIfEmpty(chain.filter(exchange).then(Mono.empty()))
                .flatMap(token -> authenticate(exchange, chain, token));
    }

    private Mono<Void> authenticate(ServerWebExchange exchange, WebFilterChain chain, Authentication token) {
        WebFilterExchange webFilterExchange = new WebFilterExchange(exchange, chain);
        return this.authenticationManager.authenticate(token)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new BadCredentialsException("No provider found for " + token.getClass()))))
                .flatMap(authentication -> onAuthenticationSuccess(authentication, webFilterExchange))
                .onErrorResume(AuthenticationException.class, e ->
                        this.failureHandler.onAuthenticationFailure(webFilterExchange, e));
    }

    private Mono<Void> onAuthenticationSuccess(Authentication authentication, WebFilterExchange webFilterExchange) {
        SecurityContextImpl securityContext = new SecurityContextImpl(authentication);
        return this.securityContextRepository.save(webFilterExchange.getExchange(), securityContext)
                .then(this.successHandler.onAuthenticationSuccess(webFilterExchange, authentication))
                .subscriberContext(ReactiveSecurityContextHolder.withSecurityContext(Mono.just(securityContext)));
    }

//    public void setSuccessHandler(ServerAuthenticationSuccessHandler successHandler) {
//        this.successHandler = successHandler;
//    }
//
//    public void setFailureHandler(ServerAuthenticationFailureHandler failureHandler) {
//        this.failureHandler = failureHandler;
//    }
}
