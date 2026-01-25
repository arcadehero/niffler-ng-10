package guru.qa.niffler.service.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.Databases.XaConsumer;
import guru.qa.niffler.data.dao.impl.AuthAuthorityDaoJdbc;
import guru.qa.niffler.data.dao.impl.AuthUserDaoJdbc;
import guru.qa.niffler.data.entity.auth.AuthAuthorityEntity;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.model.AuthUserJson;
import guru.qa.niffler.service.AuthClient;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;

import static guru.qa.niffler.data.Databases.xaTransaction;

public class AuthDbClient implements AuthClient {

    private static final Config CFG = Config.getInstance();
    private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    @Override
    public AuthUserJson create(AuthUserJson user) {
        AuthUserEntity authUserEntity = AuthUserEntity.fromJson(user);
        xaTransaction(
                new XaConsumer(connection -> {
                    AuthUserDaoJdbc dao = new AuthUserDaoJdbc(connection);
                    authUserEntity.setPassword(pe.encode(user.getPassword()));
                    AuthUserEntity createdUser = dao.create(authUserEntity);
                    authUserEntity.setId(createdUser.getId());
                }, CFG.authJdbcUrl()
                ),
                new XaConsumer(connection -> {
                    AuthAuthorityDaoJdbc dao = new AuthAuthorityDaoJdbc(connection);
                    dao.create(Arrays.stream((Authority.values()))
                            .map(a -> {
                                AuthAuthorityEntity authAuthorityEntity = new AuthAuthorityEntity();
                                authAuthorityEntity.setAuthority(a);
                                authAuthorityEntity.setUserId(authUserEntity.getId());
                                return authAuthorityEntity;
                            })
                            .toArray(AuthAuthorityEntity[]::new));
                }, CFG.authJdbcUrl())
        );
        return AuthUserJson.fromEntity(authUserEntity);
    }

    @Override
    public void delete(AuthUserJson user) {
        xaTransaction(
                new XaConsumer(
                        connection -> {
                            AuthAuthorityDaoJdbc authAuthorityDaoJdbc = new AuthAuthorityDaoJdbc(connection);
                            authAuthorityDaoJdbc.delete(
                                    authAuthorityDaoJdbc.findByUserId(user.getId())
                                            .toArray(AuthAuthorityEntity[]::new));
                        },
                        CFG.authJdbcUrl()
                ),
                new XaConsumer(
                        connection -> {
                            AuthUserDaoJdbc dao = new AuthUserDaoJdbc(connection);
                            dao.deleteById(user.getId());
                        },
                        CFG.authJdbcUrl()

                )
        );

    }
}
