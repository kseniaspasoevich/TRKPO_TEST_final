package documents.jpa.entity.user;

import org.springframework.security.core.GrantedAuthority;

public enum UserRolesEnum implements GrantedAuthority {
    USER, ADMIN;

    @Override
    public String getAuthority() {
        return name();
    }
}
