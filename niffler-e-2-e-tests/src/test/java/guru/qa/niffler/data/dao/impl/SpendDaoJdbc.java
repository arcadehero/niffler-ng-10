package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.SpendDao;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.model.CurrencyValues;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class SpendDaoJdbc implements SpendDao {

    private static final Config CFG = Config.getInstance();
    private final Connection connection;

    public SpendDaoJdbc(Connection connection) {
        this.connection = connection;
    }

    @Override
    public SpendEntity create(SpendEntity spend) {
        try (PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO spend (username, spend_date, currency, amount, description, category_id) " +
                        "VALUES ( ?, ?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
        )) {
            ps.setString(1, spend.getUsername());
            ps.setDate(2, spend.getSpendDate());
            ps.setString(3, spend.getCurrency().name());
            ps.setDouble(4, spend.getAmount());
            ps.setString(5, spend.getDescription());
            ps.setObject(6, spend.getCategory().getId());

            ps.executeUpdate();

            final UUID generatedKey;
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    generatedKey = rs.getObject("id", UUID.class);
                } else {
                    throw new SQLException("Can`t find id in ResultSet");
                }
            }
            spend.setId(generatedKey);
            return spend;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<SpendEntity> findSpendById(UUID spend) {
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT " +
                        "s.id, " +
                        "s.username, " +
                        "s.spend_date, " +
                        "s.currency, " +
                        "s.amount, " +
                        "s.description, " +
                        "c.id as c_id, " +
                        "c.name as c_name, " +
                        "c.username as c_username, " +
                        "c.archived as c_archived " +
                        "FROM spend s " +
                        "LEFT JOIN category c ON s.category_id = c.id WHERE s.id = ?"
        )) {
            ps.setObject(1, spend);
            ps.execute();
            try (ResultSet rs = ps.getResultSet()) {
                if (rs.next()) {
                    return Optional.of(mapSpendEntity(rs));
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<SpendEntity> findAllByUserName(String username) {
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT " +
                        "s.id, " +
                        "s.username, " +
                        "s.spend_date, " +
                        "s.currency, " +
                        "s.amount, " +
                        "s.description, " +
                        "c.id as c_id, " +
                        "c.name as c_name, " +
                        "c.username as c_username, " +
                        "c.archived as c_archived " +
                        "FROM spend s " +
                        "LEFT JOIN category c ON s.category_id = c.id WHERE s.username = ?"
        )) {
            ps.setObject(1, username);
            ps.execute();
            try (ResultSet rs = ps.getResultSet()) {
                List<SpendEntity> spendEntities = new ArrayList<>();
                while (rs.next()) {
                    spendEntities.add(mapSpendEntity(rs));
                }
                return spendEntities;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteSpend(SpendEntity spend) {
        try (PreparedStatement ps = connection.prepareStatement(
                "DELETE * FROM spend WHERE id = ?"
        )) {
            ps.setObject(1, spend.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private SpendEntity mapSpendEntity(ResultSet rs) throws SQLException {
        CategoryEntity ce = new CategoryEntity();
        ce.setId(rs.getObject("c_id", UUID.class));
        ce.setName(rs.getString("c_name"));
        ce.setUsername(rs.getString("c_username"));
        ce.setArchived(rs.getBoolean("c_archived"));

        SpendEntity se = new SpendEntity();
        se.setId(rs.getObject("id", UUID.class));
        se.setUsername(rs.getString("username"));
        se.setSpendDate(rs.getDate("spend_date"));
        se.setCurrency(CurrencyValues.valueOf(rs.getString("currency")));
        se.setAmount(rs.getDouble("amount"));
        se.setDescription(rs.getString("description"));
        se.setCategory(ce);
        return se;
    }
}
