package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.ExistStorageException;
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
        helper.runRequest(str, null, null, PreparedStatement::execute);
    }

    @Override
    public void update(Resume r) {
        String str = "UPDATE resume SET full_name = ? WHERE uuid = ?";
        helper.runRequest(str, r.getFullName(), r.getUuid(), PreparedStatement::execute);
    }

    @Override
    public void save(Resume r) {
        String str = "SELECT r.uuid FROM resume r WHERE uuid = ?";
        helper.runRequest(str, r.getUuid(), null,
                (ps) -> {
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        throw new ExistStorageException(r.getUuid());
                    }
                    return null;
                });
        str = "INSERT INTO resume(uuid, full_name) VALUES (?,?)";
        helper.runRequest(str, r.getUuid(), r.getFullName(), PreparedStatement::execute);
    }

    @Override
    public Resume get(String uuid) {
        String str = "SELECT * FROM resume r WHERE r.uuid = ?";
        return helper.runRequest(str, uuid, null,
                (ps) -> {
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        return new Resume(uuid, rs.getString("full_name"));
                    } else {
                        throw new NotExistStorageException(uuid);
                    }
                });
    }

    @Override
    public void delete(String uuid) {
        String str = "SELECT r.uuid FROM resume r WHERE r.uuid = ?";
        helper.runRequest(str, uuid, null, (ps) -> {
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                throw new NotExistStorageException(uuid);
            }
            return null;
        });
        str = "DELETE FROM resume r WHERE r.uuid = ?";
        helper.runRequest(str, uuid, null, PreparedStatement::execute);
    }

    @Override
    public List<Resume> getAllSorted() {
        String str = "SELECT * FROM resume ORDER BY uuid, full_name";
        return helper.runRequest(str, null, null, (ps) -> {
            ResultSet rs = ps.executeQuery();
            List<Resume> lr = new ArrayList<>();
            while (rs.next()) {
                Resume r = new Resume(rs.getString("uuid").trim(), rs.getString("full_name").trim());
                lr.add(r);
            }
            return lr;
        });
    }

    @Override
    public int size() {
        String str = "SELECT COUNT(*) FROM resume";
        return helper.runRequest(str, null, null, (ps) -> {
            ResultSet rs = ps.executeQuery();
            int num = 0;
            while (rs.next()) {
                num = (rs.getInt(1));
            }
            return num;
        });
    }
}
