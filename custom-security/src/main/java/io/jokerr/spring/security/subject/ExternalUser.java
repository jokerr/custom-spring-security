package io.jokerr.spring.security.subject;

public class ExternalUser extends AbstractCustomUser implements CustomUser {
    private String nickname;

    public ExternalUser(String id) {
        super(id, "External");
    }

    public ExternalUser(String id, String nickname) {
        this(id);
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname == null ? "" : nickname;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ExternalUser{");
        sb.append("id='").append(getId()).append('\'');
        sb.append(", type='").append(getType()).append('\'');
        sb.append(", nickname='").append(nickname).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
