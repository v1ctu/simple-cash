package com.github.v1ctu.storage;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class MySQL {

    private final String host;
    private final String user;
    private final String password;
    private final String database;
    private final int port;

    private HikariDataSource hikariDataSource;

    public MySQL(String host, String user, String password, String database, int port) {
        this.host = host;
        this.user = user;
        this.password = password;
        this.database = database;
        this.port = port;
    }

    public void openConnection() {
        try {
            HikariConfig hikariConfig = new HikariConfig();
            hikariConfig.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + database);
            hikariConfig.setDriverClassName("com.mysql.jdbc.Driver");
            hikariConfig.setUsername(user);
            hikariConfig.setPassword(password);
            hikariConfig.setMaximumPoolSize(8);
            hikariConfig.setMinimumIdle(2);
            hikariConfig.addDataSourceProperty("cachePrepStmts", "true");
            hikariConfig.addDataSourceProperty("prepStmtCacheSize", "250");
            hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

            hikariDataSource = new HikariDataSource(hikariConfig);

            createTable();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public Connection getConnection() throws SQLException {
        return hikariDataSource.getConnection();
    }

    private void createTable() {
        try (Statement statement = getConnection().createStatement()) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS accounts (" +
                    "uuid VARCHAR(36) NOT NULL, " +
                    "name VARCHAR(16) NOT NULL, " +
                    "cash DOUBLE NOT NULL," +
                    "primary key (uuid)" +
                    ")");
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
