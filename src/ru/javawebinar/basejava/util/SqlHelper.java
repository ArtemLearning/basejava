package ru.javawebinar.basejava.util;

import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.sql.ConnectionFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SqlHelper {
    private final ConnectionFactory connectionFactory;

    @FunctionalInterface
    public interface Request<T> {
        T execute(PreparedStatement ps) throws SQLException;
    }

    public SqlHelper(String dbUrl, String dbUser, String dbPassword) throws SQLException {
        connectionFactory = () -> DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }

    public <T> T runRequest(String statement, Request<T> t) {
        try (Connection connection = connectionFactory.getConnection();
             PreparedStatement ps = connection.prepareStatement(statement)) {
            return t.execute(ps);
        } catch (SQLException e) {
            if (e.getSQLState().equals("23505")) {  // Already exists
                throw new ExistStorageException(null);
            }
            throw new StorageException(e);
        }
    }
}
