package guru.qa.niffler.data.entity.auth;

import guru.qa.niffler.model.AuthUserJson;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Getter
@Setter
@ToString
public class AuthUserEntity {

    private UUID id;
    private String username;
    private String password;
    private Boolean enabled;
    private Boolean accountNonExpired;
    private Boolean accountNonLocked;
    private Boolean credentialsNonExpired;

    public static AuthUserEntity fromJson(AuthUserJson user) {
        AuthUserEntity ue = new AuthUserEntity();
        if (user.getId() != null) {
            ue.setId(user.getId());
        }
        ue.setUsername(user.getUsername());
        ue.setPassword(user.getPassword());
        ue.setEnabled(user.isEnabled());
        ue.setAccountNonExpired(user.isAccountNonExpired());
        ue.setAccountNonLocked(user.isAccountNonLocked());
        ue.setCredentialsNonExpired(user.isCredentialsNonExpired());

        return ue;
    }
}
