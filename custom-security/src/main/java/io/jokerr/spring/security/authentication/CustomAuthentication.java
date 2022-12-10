package io.jokerr.spring.security.authentication;

import io.jokerr.spring.security.subject.CustomUser;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class CustomAuthentication implements Authentication {
    private CustomUser principal;
    private List<GrantedAuthority> authorities;
    private boolean authenticated = false;
    private Objects details = null;
    private HttpHeaders credentials;

    public CustomAuthentication(HttpHeaders credentials) {
        this.credentials = credentials;
    }

    public CustomAuthentication(HttpHeaders credentials, CustomUser principal, List<GrantedAuthority> authorities) {
        this.credentials = credentials;
        this.principal = principal;
        setAuthorities(authorities);
    }

    private void setAuthorities(List<GrantedAuthority> authorities) {
        if (authorities == null || authorities.stream().anyMatch(a -> a == null)) {
            throw new IllegalArgumentException("Authorities cannot be null or contain any null elements");
        }
        this.authorities = Collections.unmodifiableList(authorities);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public HttpHeaders getCredentials() {
        return credentials;
    }

    @Override
    public Object getDetails() {
        return details;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.authenticated = isAuthenticated;
    }

    @Override
    public String getName() {
        return principal == null ? "" : principal.getName();
    }
}
