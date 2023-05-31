package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.ContactType;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.sql.SqlHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SqlStorage implements Storage {
    public final SqlHelper sqlHelper;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) throws SQLException {
        sqlHelper = new SqlHelper(dbUrl, dbUser, dbPassword);
    }

    @Override
    public void clear() {
        sqlHelper.runRequest("DELETE FROM resume", PreparedStatement::execute);
    }

    @Override
    public void update(Resume r) {
        sqlHelper.runTransaction(conn -> {
            try (PreparedStatement ps = conn.prepareStatement("UPDATE resume SET full_name = ? WHERE uuid = ?");) {
                ps.setString(1, r.getFullName());
                ps.setString(2, r.getUuid());
                if (ps.executeUpdate() == 0) {
                    throw new NotExistStorageException(r.getUuid());
                }
            }
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM contact WHERE resume_uuid = ?");) {
                ps.setString(1, r.getUuid());
                ps.execute();
            }
            insertContacts(conn, r);
            return null;
        });
    }

    @Override
    public void save(Resume r) {
        sqlHelper.runTransaction(conn -> {
            try (PreparedStatement ps = conn.prepareStatement("INSERT INTO resume(uuid, full_name) VALUES (?,?)")) {
                ps.setString(1, r.getUuid());
                ps.setString(2, r.getFullName());
                ps.execute();
            }
            insertContacts(conn, r);
            return null;
        });
    }

    @Override
    public Resume get(String uuid) {
        return sqlHelper.runTransaction(conn -> {
            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM resume AS r WHERE r.uuid = ?")) {
                ps.setString(1, uuid);
                final ResultSet rs = ps.executeQuery();
                if (!rs.next()) {
                    throw new NotExistStorageException(uuid);
                }
                return addContacts(conn, new Resume(uuid, rs.getString("full_name")));
            }
        });
    }

    @Override
    public void delete(String uuid) {
        sqlHelper.runTransaction(conn -> {
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM resume r WHERE r.uuid = ?")) {
                ps.setString(1, uuid);
                if (ps.executeUpdate() == 0) {
                    throw new NotExistStorageException(uuid);
                }
                return null;
            }
        });
    }

    @Override
    public List<Resume> getAllSorted() {
        return sqlHelper.runTransaction(conn -> {
            final Map<String, Resume> resumesMap = new LinkedHashMap<>();
            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM resume AS r ORDER BY r.full_name ")) {
                final ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    final String uuid = rs.getString("uuid");
                    resumesMap.put(uuid, new Resume(uuid, rs.getString("full_name")));
                }
            }
            for (Map.Entry<String, Resume> entry : resumesMap.entrySet()) {
                addContacts(conn, entry.getValue());
            }
            return new ArrayList(resumesMap.values());
        });
    }

    @Override
    public int size() {
        return sqlHelper.runRequest("SELECT COUNT(*) FROM resume", (ps) -> {
            final ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;
        });
    }

    private void insertContacts(Connection conn, Resume r) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO contact(resume_uuid, type, value) VALUES (?,?,?)")) {
            for (Map.Entry<ContactType, String> e : r.getAllContacts().entrySet()) {
                ps.setString(1, r.getUuid());
                ps.setString(2, e.getKey().name());
                ps.setString(3, e.getValue());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private Resume addContacts(Connection conn, Resume r) throws SQLException {
        final PreparedStatement ps = conn.prepareStatement("SELECT * FROM contact AS c WHERE c.resume_uuid = ?");
        ps.setString(1, r.getUuid());
        final ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            r.addContact(ContactType.valueOf(rs.getString("type")), rs.getString("value"));
        }
        return r;
    }
}