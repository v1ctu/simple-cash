package com.github.v1ctu.storage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class MySQL {

    private final String host;
    private final String user;
    private final String password;
    private final String database;
    private final int port;

    private Connection connection;

    public MySQL(String host, String user, String password, String database, int port) {
        this.host = host;
        this.user = user;
        this.password = password;
        this.database = database;
        this.port = port;
    }

    public void openConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connect();
            createTable();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed())
                connect();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return connection;
    }

    private void connect() throws SQLException {
        connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, user, password);
    }

    private void createTable() {
        try (Statement statement = connection.createStatement()) {
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
