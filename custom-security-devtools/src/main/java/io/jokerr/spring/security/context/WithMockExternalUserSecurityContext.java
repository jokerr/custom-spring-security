package io.jokerr.spring.security.context;

import io.jokerr.spring.security.subject.CustomUser;
import io.jokerr.spring.security.subject.ExternalUser;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithMockExternalUserSecurityContext
        extends AbstractMockSecurityContextFactory
        implements WithSecurityContextFactory<WithMockExternalUser> {

    @Override
    public SecurityContext createSecurityContext(WithMockExternalUser annotation) {
        CustomUser user = new ExternalUser(annotation.value().trim(), annotation.nickname().trim());
        return setSecurityContext(user, buildAuthorities(annotation.authorities()));
    }
}
