package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.util.SqlHelper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SqlStorage implements Storage {
    public final SqlHelper helper;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) throws SQLException {
        helper = new SqlHelper(dbUrl, dbUser, dbPassword);
    }

    @Override
    public void clear() {
        String str = "DELETE FROM resume";
        helper.runRequest(str, PreparedStatement::execute);
    }

    @Override
    public void update(Resume r) {
        String str = "UPDATE resume SET full_name = ? WHERE uuid = ?";
        helper.runRequest(str, (ps) -> {
            ps.setString(1, r.getFullName());
            ps.setString(2, r.getUuid());
            return ps.executeUpdate();
        });
    }

    @Override
    public void save(Resume r) {
        String str = "INSERT INTO resume(uuid, full_name) VALUES (?,?)";
        helper.runRequest(str, (ps) -> {
            ps.setString(1, r.getUuid());
            ps.setString(2, r.getFullName());
            ps.execute();
            return null;
        });
    }

    @Override
    public Resume get(String uuid) {
        String str = "SELECT * FROM resume r WHERE r.uuid = ?";
        return helper.runRequest(str, (ps) -> {
            ps.setString(1, uuid);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                throw new NotExistStorageException(uuid);
            }
            return new Resume(uuid, rs.getString("full_name"));
        });
    }

    @Override
    public void delete(String uuid) {
        String str = "DELETE FROM resume r WHERE r.uuid = ?";
        helper.runRequest(str, (ps) -> {
            ps.setString(1, uuid);
            if (ps.executeUpdate() == 0) {
                throw new NotExistStorageException(uuid);
            }
            return null;
        });
    }

    @Override
    public List<Resume> getAllSorted() {
        String str = "SELECT * FROM resume ORDER BY full_name, uuid";
        return helper.runRequest(str, (ps) -> {
            ResultSet rs = ps.executeQuery();
            List<Resume> lr = new ArrayList<>();
            while (rs.next()) {
                Resume r = new Resume(rs.getString("uuid"), rs.getString("full_name"));
                lr.add(r);
            }
            return lr;
        });
    }

    @Override
    public int size() {
        String str = "SELECT COUNT(*) FROM resume";
        return helper.runRequest(str, (ps) -> {
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;
        });
    }
}
