package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.sql.ConnectionFactory;
import ru.javawebinar.basejava.util.SqlHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SqlStorage implements Storage {
    public final ConnectionFactory connectionFactory;
    public final SqlHelper helper;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) throws SQLException {
        connectionFactory = () -> DriverManager.getConnection(dbUrl, dbUser, dbPassword);
        helper = new SqlHelper(connectionFactory);
    }

    @Override
    public void clear() {
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM resume")) {
            ps.execute();
        } catch (SQLException e) {
            throw new StorageException(e);
        }

//        try {
//            helper.doRequest("DELETE FROM resume", null, null);
//        } catch (SQLException e) {
//            throw new StorageException(e);
//        }
    }

    @Override
    public void update(Resume r) {
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement("UPDATE resume SET full_name = ? WHERE uuid = ?")) {
            ps.setString(1, r.getFullName());
            ps.setString(2, r.getUuid());
            ps.execute();
        } catch (SQLException e) {
            throw new StorageException(e);
        }
//        try {
//            int i = helper.doRequestUpdate("UPDATE resume SET full_name = ? WHERE uuid = ?", r.getFullName(), r.getUuid());
//            if (i == 0) {
//                throw new StorageException("Cannot update resume" + r);
//            }
//        } catch (SQLException e) {
//            throw new StorageException(e);
//        }
    }

    @Override
    public void save(Resume r) {
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps1 = conn.prepareStatement("SELECT r.uuid FROM resume r WHERE uuid = ?");
             PreparedStatement ps2 = conn.prepareStatement("INSERT INTO resume(uuid, full_name) VALUES (?,?)")) {
            ps1.setString(1, r.getUuid());
            ResultSet rs = ps1.executeQuery();
            if (rs.next()) {
                throw new ExistStorageException(r.getUuid());
            }
            ps2.setString(1, r.getUuid());
            ps2.setString(2, r.getFullName());
            ps2.execute();
        } catch (SQLException e) {
            throw new StorageException(e);
        }
//        try {
//            int i = helper.doRequestUpdate("INSERT INTO resume(uuid, full_name) VALUES (?,?)", r.getUuid(), r.getFullName());
//            if (i == 0) {
//                throw new StorageException("Cannot save resume" + r);
//            }
//        } catch (SQLException e) {
//            throw new StorageException(e);
//        }
    }

    @Override
    public Resume get(String uuid) {
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM resume r WHERE r.uuid = ?")) {
            ps.setString(1, uuid);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Resume(uuid, rs.getString("full_name"));
            } else {
                throw new NotExistStorageException(uuid);
            }
        } catch (SQLException e) {
            throw new StorageException(e);
        }
//        try {
//            ResultSet rs = helper.doRequestQuery("SELECT * FROM resume r WHERE r.uuid = ?", uuid, null);
//            if (!rs.next()) {
//                throw new NotExistStorageException(uuid);
//            }
//            return new Resume(uuid, rs.getString("full_name"));
//        } catch (SQLException e) {
//            throw new StorageException(e);
//        }
    }

    @Override
    public void delete(String uuid) {
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps1 = conn.prepareStatement("SELECT r.uuid FROM resume r WHERE r.uuid = ?");
             PreparedStatement ps2 = conn.prepareStatement("DELETE FROM resume r WHERE r.uuid = ?")) {
            ps1.setString(1, uuid);
            ResultSet rs = ps1.executeQuery();
            if (!rs.next()){
                throw new NotExistStorageException(uuid);
            }
            ps2.setString(1, uuid);
            ps2.execute();
        } catch (SQLException e) {
            throw new StorageException(e);
        }
//        try {
//            int i = helper.doRequestUpdate("DELETE FROM resume WHERE uuid = ?", uuid, null);
//            if (i == 0) {
//                throw new StorageException("Cannot delete resume" + uuid);
//            }
//        } catch (SQLException e) {
//            throw new StorageException(e);
//        }
    }

    @Override
    public List<Resume> getAllSorted() {
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM resume ORDER BY uuid, full_name")) {
            ResultSet rs = ps.executeQuery();
            List<Resume> lr = new ArrayList<>();
            while (rs.next()) {
                Resume r = new Resume(rs.getString("uuid").trim(), rs.getString("full_name").trim());
                lr.add(r);
            }
            return lr;
        } catch (SQLException e) {
            throw new StorageException(e);
        }
//        try {
//            ResultSet rs = helper.doRequestQuery("SELECT * FROM resume ORDER BY uuid, full_name", null, null);
//            List<Resume> lr = new ArrayList<>();
//            if (!rs.next()) {
//                throw new StorageException("Storage is empty");
//            }
//            while (rs.next()) {
//                Resume r = new Resume(rs.getString("uuid"), rs.getString("full_name"));
//                lr.add(r);
//            }
//            return lr;
//        } catch (SQLException e) {
//            throw new StorageException(e);
//        }
    }

    @Override
    public int size() {
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM resume")) {
            ResultSet rs = ps.executeQuery();
            int num = 0;
            while (rs.next()) {
                num = (rs.getInt(1));
            }
            return num;
        } catch (SQLException e) {
            throw new StorageException(e);
        }
//        try {
//            ResultSet rs = helper.doRequestQuery("SELECT COUNT(*) AS lines FROM resume", null, null);
//            if (!rs.next()) {
//                throw new StorageException("Storage is empty");
//            }
//            return rs.getInt("lines");
//        } catch (SQLException e) {
//            throw new StorageException(e);
//        }
    }
}
