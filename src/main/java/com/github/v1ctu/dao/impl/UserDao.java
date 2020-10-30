package com.github.v1ctu.dao.impl;

import com.github.v1ctu.CashPlugin;
import com.github.v1ctu.dao.Dao;
import com.github.v1ctu.entity.UserEntity;
import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collection;
import java.util.UUID;

@AllArgsConstructor
public class UserDao implements Dao<UUID, UserEntity> {

    private static final String REPLACE_STATEMENT = "REPLACE INTO accounts VALUES (?, ?, ?);";
    private static final String FIND_STATEMENT = "SELECT * FROM accounts WHERE uuid = ? LIMIT 1;";
    private static final String DELETE_STATEMENT = "DELETE FROM accounts WHERE uuid = ?;";

    private final CashPlugin plugin;

    @Override
    public void replace(UserEntity value) {
        try (Connection connection = plugin.getMySql().getConnection()) {
            PreparedStatement statement = connection.prepareStatement(REPLACE_STATEMENT);
            statement.setString(1, value.getUuid().toString());
            statement.setString(2, value.getName());
            statement.setDouble(3, value.getQuantity());
            statement.execute();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void delete(UserEntity value) {
        try (Connection connection = plugin.getMySql().getConnection()) {
            PreparedStatement statement = connection.prepareStatement(DELETE_STATEMENT);
            statement.setString(1, value.getUuid().toString());
            statement.executeUpdate();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public UserEntity find(UUID key) {
        try (Connection connection = plugin.getMySql().getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_STATEMENT);
            preparedStatement.setString(1, key.toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            if(!resultSet.next()) return null;
            return UserEntity.builder()
                    .uuid(UUID.fromString(resultSet.getString("uuid")))
                    .name(resultSet.getString("name"))
                    .quantity(resultSet.getDouble("cash"))
                    .build();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }

    @Override
    public Collection<UserEntity> find() {
        return null;
    }
}
