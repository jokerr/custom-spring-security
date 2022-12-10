package io.jokerr.spring.security.authorities;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.stream.Collectors;

public interface AuthorityConverter {
    default List<GrantedAuthority> transform(List<String> groups) {
        if (groups == null || groups.isEmpty()) {
            return AuthorityUtils.NO_AUTHORITIES;
        }

        return groups.stream()
                .map(g -> new SimpleGrantedAuthority(g))
                .collect(Collectors.toList());
    }
}
