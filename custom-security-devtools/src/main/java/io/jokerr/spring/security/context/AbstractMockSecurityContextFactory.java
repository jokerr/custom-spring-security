package io.jokerr.spring.security.context;

import io.jokerr.spring.security.authentication.CustomAuthentication;
import io.jokerr.spring.security.subject.CustomUser;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractMockSecurityContextFactory {

    protected SecurityContext setSecurityContext(CustomUser principal, List<GrantedAuthority> authorities) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication auth = new CustomAuthentication(new HttpHeaders(), principal, authorities);
        auth.setAuthenticated(true);
        context.setAuthentication(auth);
        return context;
    }

    protected List<GrantedAuthority> buildAuthorities(String... roles) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for(String authority : roles) {
            authorities.add(new SimpleGrantedAuthority(authority));
        }
        return authorities;
    }
}
