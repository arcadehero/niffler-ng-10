package guru.qa.niffler.test.jdbc;

import guru.qa.niffler.model.AuthUserJson;
import guru.qa.niffler.service.impl.AuthDbClient;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AuthTest {

    @Test
    void createUser() {
        AuthUserJson authUserJson = new AuthUserJson();
        authUserJson.setUsername("it_test_authUserJson");
        authUserJson.setPassword("secret");
        authUserJson.setEnabled(true);
        authUserJson.setAccountNonExpired(true);
        authUserJson.setAccountNonLocked(true);
        authUserJson.setCredentialsNonExpired(true);

        AuthDbClient authDbClient = new AuthDbClient();
        AuthUserJson createdAuthUser = authDbClient.create(authUserJson);

        assertNotNull(createdAuthUser.getId());


    }
}
