package io.jokerr.spring.security;

import io.jokerr.spring.security.context.WithMockExternalUser;
import org.junit.Test;
import org.springframework.core.annotation.AnnotatedElementUtils;

import static org.junit.Assert.assertEquals;

public class WithMockExternalUserSecuritContextTest {

    @Test
    public void defaults() {
        WithMockExternalUser user = AnnotatedElementUtils.findMergedAnnotation(Annotated.class, WithMockExternalUser.class);
        assertEquals("externalID", user.value());
    }

    @WithMockExternalUser
    private class Annotated {
        // no-op
    }
}
