package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.ContactType;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.sql.SqlHelper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
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
        sqlHelper.runRequest("UPDATE resume SET full_name = ? WHERE uuid = ?", (ps) -> {
            ps.setString(1, r.getFullName());
            ps.setString(2, r.getUuid());
            if (ps.executeUpdate() == 0) {
                throw new NotExistStorageException(r.getUuid());
            }
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
            return null;
        });
        sqlHelper.runTransaction(conn -> {
            try (PreparedStatement ps = conn.prepareStatement("INSERT INTO contact(resume_uuid, type, value) VALUES (?,?,?)")) {
                for (Map.Entry<ContactType, String> e : r.getAllContacts().entrySet()) {
                    ps.setString(1, r.getUuid());
                    ps.setString(2, e.getKey().name());
                    ps.setString(3, e.getValue());
                    ps.addBatch();
                }
                ps.executeBatch();
            }
            return null;
        });
    }

    @Override
    public Resume get(String uuid) {
        return sqlHelper.runRequest(" " +
                        "SELECT * FROM resume r " +
                        " LEFT JOIN contact c " +
                        "  ON r.uuid = c.resume_uuid " +
                        "   WHERE r.uuid = ?",
                (ps) -> {
                    ps.setString(1, uuid);
                    ResultSet rs = ps.executeQuery();
                    if (!rs.next()) {
                        throw new NotExistStorageException(uuid);
                    }
                    Resume r = new Resume(uuid, rs.getString("full_name"));
                    do {
                        if (rs.getString("type") != null) {
                            String value = rs.getString("value");
                            ContactType type = ContactType.valueOf(rs.getString("type"));
                            r.addContact(type, value);
                        }
                    } while (rs.next());
                    return r;
                });
    }

    @Override
    public void delete(String uuid) {
        sqlHelper.runRequest("DELETE FROM resume r WHERE r.uuid = ?", (ps) -> {
            ps.setString(1, uuid);
            if (ps.executeUpdate() == 0) {
                throw new NotExistStorageException(uuid);
            }
            return null;
        });
        sqlHelper.runRequest("DELETE FROM contact c WHERE c.resume_uuid = ?", (ps) -> {
            ps.setString(1, uuid);
            if (ps.executeUpdate() == 0) {
                throw new NotExistStorageException(uuid);
            }
            return null;
        });
    }

    @Override
    public List<Resume> getAllSorted() {
        return sqlHelper.runRequest(" " +
                "SELECT r.uuid, r.full_name" +
                " FROM resume AS r" +
                "   ORDER BY r.full_name, r.uuid", (ps) -> {
            ResultSet rs = ps.executeQuery();
            List<Resume> lr = new ArrayList<>();
            while (rs.next()) {
                Resume r = new Resume(rs.getString("uuid"), rs.getString("full_name"));
                Map<ContactType, String> contact = sqlHelper.runRequest(" " +
                        "SELECT c.type, c.value" +
                        " FROM contact AS c" +
                        " WHERE c.resume_uuid = ?", (ps1 -> {
                    ps1.setString(1, r.getUuid());
                    ResultSet rs1 = ps1.executeQuery();
                    Map<ContactType, String> returnMap = new HashMap<>();
                    while (rs1.next()) {
                        returnMap.put(ContactType.valueOf(rs1.getString("type")), rs1.getString("value"));
                    }
                    return returnMap;
                }));
                for (Map.Entry<ContactType, String> entry : contact.entrySet()) {
                    r.addContact(entry.getKey(), entry.getValue());
                }
                lr.add(r);
            }
            return lr;
        });
    }

    @Override
    public int size() {
        return sqlHelper.runRequest("SELECT COUNT(*) FROM resume", (ps) -> {
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;
        });
    }
}