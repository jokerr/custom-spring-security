package io.jokerr.spring.security.web.server;

import io.jokerr.spring.security.authentication.CustomAuthentication;
import io.jokerr.spring.security.providers.CustomAuthenticationProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class CustomServerAuthenticationConverter implements ServerAuthenticationConverter {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomServerAuthenticationConverter.class);
    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {
//        String id = exchange.getRequest().getHeaders().getFirst("X-custom-id");
//        if (id != null && !id.trim().isEmpty() ) {
//            return Mono.just(new CustomAuthentication(id.trim()));
//        }
//        return Mono.empty();

        HttpHeaders headers = new HttpHeaders();
        exchange.getRequest().getHeaders()
                .entrySet().stream()
                .filter(entry -> CustomAuthenticationProvider.isSecurityHeader(entry.getKey()))
                .forEach(entry -> headers.addAll(entry.getKey(), entry.getValue()));

        LOGGER.info("Converted Authentication {}", headers.size());
        if (!headers.isEmpty()) { 
            return Mono.just(new CustomAuthentication(headers));
        }
        return Mono.empty();
    }
}
