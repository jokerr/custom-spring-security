package io.jokerr.spring.security.context;

import io.jokerr.spring.security.subject.CustomUser;
import io.jokerr.spring.security.subject.InternalUser;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithMockInternalUserSecurityContext
        extends AbstractMockSecurityContextFactory
        implements WithSecurityContextFactory<WithMockInteralUser> {

    @Override
    public SecurityContext createSecurityContext(WithMockInteralUser annotation) {
        CustomUser user = new InternalUser(annotation.value().trim());
        return setSecurityContext(user, buildAuthorities(annotation.authorities()));
    }
}
