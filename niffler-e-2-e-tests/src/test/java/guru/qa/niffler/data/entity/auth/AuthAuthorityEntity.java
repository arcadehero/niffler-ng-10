package guru.qa.niffler.data.entity.auth;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Getter
@Setter
@ToString
public class AuthAuthorityEntity {

    private UUID id;
    private UUID userId;
    private Authority authority;
}
