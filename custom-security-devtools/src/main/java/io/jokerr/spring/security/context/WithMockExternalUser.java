package io.jokerr.spring.security.context;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockExternalUserSecurityContext.class)
public @interface WithMockExternalUser {
    String value() default "externalID";

    String nickname() default "nickname";

    String[] authorities() default { "ROLE", "AUTHORITY" };
}
