package io.jokerr.spring.security.web;

import io.jokerr.spring.security.authentication.CustomAuthentication;
import io.jokerr.spring.security.providers.CustomAuthenticationProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationConverter;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class CustomAuthenticationConverter implements AuthenticationConverter {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomAuthenticationConverter.class);
    
    @Override
    public Authentication convert(HttpServletRequest request) {
//        return Optional.ofNullable(request.getHeader("X-user-id"))
//                .map(String::trim)
//                .map(CustomAuthentication::new)
//                .orElse(null);

        // extract the headers we care about
        HttpHeaders headers = extractHeaders(request);

        LOGGER.info("Converted Authentication {}", headers.size());
        if (!headers.isEmpty()) {
            return new CustomAuthentication(headers);
        }
        return null;
    }

    private HttpHeaders extractHeaders(HttpServletRequest request) {
        HttpHeaders headers = new HttpHeaders();
        stream(request.getHeaderNames())
                .filter(CustomAuthenticationProvider::isSecurityHeader) // only security headers
                .forEach(headerName -> stream(request.getHeaders(headerName))
                    .forEach(headerValue -> headers.add(headerName, headerValue)));
        return headers;
    }

    public static <T> Stream<T> stream(Enumeration<T> e) {
        return StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(
                        new Iterator<T>() {
                            @Override
                            public boolean hasNext() {
                                return e.hasMoreElements();
                            }

                            @Override
                            public T next() {
                                return e.nextElement();
                            }
                        },
                        Spliterator.ORDERED),
        false);
    }
}
