package io.jokerr.spring.security.subject;

import java.util.Objects;

public abstract class AbstractCustomUser implements CustomUser {
    private String id;
    private String type;

    public AbstractCustomUser(String id, String type) {
        this.id = id;
        this.type = type;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getType() {
        return this.type;
    }

    @Override
    public String getName() {
        return id;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("AbstractCustomUser{");
        sb.append("id='").append(id).append('\'');
        sb.append(", type='").append(type).append('\'');
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractCustomUser that = (AbstractCustomUser) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type);
    }
}
