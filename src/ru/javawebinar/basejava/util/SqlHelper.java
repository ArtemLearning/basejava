package ru.javawebinar.basejava.util;

import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.sql.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SqlHelper {
    public final Connection connection;

    @FunctionalInterface
    public interface Request<T> {
        T execute(PreparedStatement ps) throws SQLException;
    }

    public SqlHelper(ConnectionFactory connectionFactory) throws SQLException {
        connection = connectionFactory.getConnection();
    }

    public <T> T runRequest(String statement, String str1, String str2, Request<T> t) {
        try {
            PreparedStatement ps = connection.prepareStatement(statement);
            if (str1 != null) {
                ps.setString(1, str1);
            }
            if (str2 != null) {
                ps.setString(2, str2);
            }
            return t.execute(ps);
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }
}
