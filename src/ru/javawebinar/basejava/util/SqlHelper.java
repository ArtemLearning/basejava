package ru.javawebinar.basejava.util;

import ru.javawebinar.basejava.sql.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SqlHelper {
    public final Connection connection;
    private PreparedStatement preparedStatement;

//    public interface Request{
//        AnEntity execute(EntityManager em);
//    }
    public SqlHelper(ConnectionFactory connectionFactory) throws SQLException {
        connection = connectionFactory.getConnection();
    }

    public void doRequest(String request, String str1, String str2) throws SQLException {
        prepare(request, str1, str2);
        preparedStatement.execute();
    }

    public ResultSet doRequestQuery(String request, String str1, String str2) throws SQLException {
        prepare(request, str1, str2);
        return preparedStatement.executeQuery();
    }

    public int doRequestUpdate(String request, String str1, String str2) throws SQLException {
        prepare(request, str1, str2);
        return preparedStatement.executeUpdate();
    }

    private void prepare(String request, String str1, String str2) throws SQLException {
        preparedStatement = connection.prepareStatement(request);
        if (str1 != null) {
            preparedStatement.setString(1, str1);
        }
        if (str2 != null) {
            preparedStatement.setString(2, str2);
        }
    }
}
