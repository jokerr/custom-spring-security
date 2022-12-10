package io.jokerr.spring.security.subject;

import org.springframework.security.core.AuthenticatedPrincipal;

import java.security.Principal;

public interface CustomUser extends AuthenticatedPrincipal, Principal {

    String getId();

    String getType();
}
