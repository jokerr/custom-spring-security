package io.jokerr.spring.security.subject;

public class InternalUser extends AbstractCustomUser implements CustomUser {
    public InternalUser(String id) {
        super(id, "Internal");
    }
}
