package guru.qa.niffler.service;

import guru.qa.niffler.model.AuthUserJson;

public interface AuthClient {
    AuthUserJson create(AuthUserJson user);

    void delete(AuthUserJson user);
}
