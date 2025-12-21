package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.Databases;
import guru.qa.niffler.data.dao.UserdataUserDao;
import guru.qa.niffler.data.entity.UserEntity;
import guru.qa.niffler.model.CurrencyValues;

import java.sql.*;
import java.util.Optional;
import java.util.UUID;

public class UserdataUserDaoJdbc implements UserdataUserDao {

    private static final Config CFG = Config.getInstance();

    @Override
    public UserEntity createUser(UserEntity user) {
        try (Connection connection = Databases.connection(CFG.userDataUrl())) {
            try (PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO user (username, currency, firstname, surname, photo, photo_small, full_name) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            )) {
                ps.setString(1, user.getUsername());
                ps.setString(2, user.getCurrency().toString());
                ps.setString(3, user.getFirstname());
                ps.setString(4, user.getSurname());
                ps.setObject(5, user.getPhoto());
                ps.setObject(6, user.getPhotoSmall());
                ps.setString(7, user.getFullname());

                ps.executeUpdate();
                final UUID generatedKey;
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        generatedKey = rs.getObject("id", UUID.class);
                    } else {
                        throw new SQLException("Can`t find id in ResultSet");
                    }
                }
                user.setId(generatedKey);
                return user;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<UserEntity> findById(UUID id) {
        try (Connection connection = Databases.connection(CFG.userDataUrl())) {
            try (PreparedStatement ps = connection.prepareStatement(
                    "SELECT * FROM user WHERE id = ?"
            )) {
                ps.setObject(1, id);
                ps.executeQuery();
                try (ResultSet rs = ps.getResultSet()) {
                    if (rs.next()) {
                        UserEntity user = new UserEntity();
                        user.setId(rs.getObject("id", UUID.class));
                        user.setUsername(rs.getString("username"));
                        user.setCurrency(CurrencyValues.valueOf(rs.getString("currency")));
                        user.setFirstname(rs.getString("firstname"));
                        user.setSurname(rs.getString("surname"));
                        user.setPhoto(rs.getObject("photo", byte[].class));
                        user.setPhotoSmall(rs.getObject("photo_small", byte[].class));
                        user.setFullname(rs.getString("fullname"));
                        return Optional.of(user);
                    } else {
                        return Optional.empty();
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<UserEntity> findByUsername(String username) {
        try (Connection connection = Databases.connection(CFG.userDataUrl())) {
            try (PreparedStatement ps = connection.prepareStatement(
                    "SELECT * FROM user WHERE username = ? "
            )) {
                ps.setObject(1, username);
                ps.executeQuery();
                try (ResultSet rs = ps.getResultSet()) {
                    if (rs.next()) {
                        UserEntity user = new UserEntity();
                        user.setId(rs.getObject("id", UUID.class));
                        user.setUsername(rs.getString("username"));
                        user.setCurrency(CurrencyValues.valueOf(rs.getString("currency")));
                        user.setFirstname(rs.getString("firstname"));
                        user.setSurname(rs.getString("surname"));
                        user.setPhoto(rs.getObject("photo", byte[].class));
                        user.setPhotoSmall(rs.getObject("photo_small", byte[].class));
                        user.setFullname(rs.getString("fullname"));
                        return Optional.of(user);
                    } else {
                        return Optional.empty();
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(UserEntity user) {
        try (Connection connection = Databases.connection(CFG.userDataUrl())) {
            try (PreparedStatement ps = connection.prepareStatement(
                    "DELETE * FROM user WHERE id = ? "
            )) {
                ps.setObject(1, user.getId());
                ps.execute();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
