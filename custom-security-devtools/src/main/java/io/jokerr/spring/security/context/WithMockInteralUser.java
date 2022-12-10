package io.jokerr.spring.security.context;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockInternalUserSecurityContext.class)
public @interface WithMockInteralUser {
    String value() default "internalID";

    String[] authorities() default { "ROLE", "AUTHORITY" };
}
