package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.data.dao.AuthAuthorityDao;
import guru.qa.niffler.data.entity.auth.AuthAuthorityEntity;
import guru.qa.niffler.data.entity.auth.Authority;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AuthAuthorityDaoJdbc implements AuthAuthorityDao {

    private final Connection connection;

    public AuthAuthorityDaoJdbc(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void create(AuthAuthorityEntity... authority) {
        try (PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO \"authority\" (user_id, authority)" +
                        "VALUES (?, ?)",
                Statement.RETURN_GENERATED_KEYS
        )) {
            for (AuthAuthorityEntity authAuthorityEntity : authority) {
                ps.setObject(1, authAuthorityEntity.getUserId());
                ps.setObject(2, authAuthorityEntity.getAuthority().name());
                ps.addBatch();
                ps.clearParameters();
            }
            ps.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<AuthAuthorityEntity> findByUserId(UUID userId) {
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT * FROM \"authority\" WHERE user_id = ?"
        )) {
            ps.setObject(1, userId);
            ps.executeQuery();
            try (ResultSet rs = ps.getResultSet()) {
                List<AuthAuthorityEntity> authAuthorityEntities = new ArrayList<>();
                while (rs.next()) {
                    authAuthorityEntities.add(mapAuthorityEntity(rs));
                }
                return authAuthorityEntities;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<AuthAuthorityEntity> findAll() {
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT * FROM authority"
        )) {
            ps.execute();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                List<AuthAuthorityEntity> createdAuthorities = new ArrayList<>();
                while (rs.next()) {
                    createdAuthorities.add(mapAuthorityEntity(rs));
                }
                return createdAuthorities;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(AuthAuthorityEntity... authority) {
        try (PreparedStatement ps = connection.prepareStatement(
                "DELETE * FROM \"authority\" WHERE id = ?"
        )) {
            for (AuthAuthorityEntity authAuthorityEntity : authority) {
                ps.setObject(1, authAuthorityEntity.getId());
                ps.addBatch();
                ps.clearParameters();
            }
            ps.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private AuthAuthorityEntity mapAuthorityEntity(ResultSet rs) throws SQLException {
        AuthAuthorityEntity ae = new AuthAuthorityEntity();
        ae.setId(rs.getObject("id", UUID.class));
        ae.setUserId(rs.getObject("user_id", UUID.class));
        ae.setAuthority(Authority.valueOf(rs.getString("authority")));
        return ae;
    }
}
