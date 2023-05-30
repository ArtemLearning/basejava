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
            PreparedStatement ps = conn.prepareStatement("UPDATE resume SET full_name = ? WHERE uuid = ?"); //Обновление резюме
            ps.setString(1, r.getFullName());
            ps.setString(2, r.getUuid());
            if (ps.executeUpdate() == 0) {
                throw new NotExistStorageException(r.getUuid());
            }
            if (r.getAllContacts().size() != 0 && !contactsExist(conn, r.getUuid())) {  // Добавление контактов в таблицу
                PreparedStatement psInsert = conn.prepareStatement("INSERT INTO contact(resume_uuid, type, value) VALUES (?,?,?)");
                insertContact(psInsert, r).executeBatch();
            }
            if (r.getAllContacts().size() != 0 && contactsExist(conn, r.getUuid())) { // Изменение контактов в таблице
                PreparedStatement psUpdate = conn.prepareStatement("UPDATE contact SET value = ? WHERE resume_uuid = ? AND type = ?");
                for (Map.Entry<ContactType, String> e : r.getAllContacts().entrySet()) {
                    psUpdate.setString(1, e.getValue());
                    psUpdate.setString(2, r.getUuid());
                    psUpdate.setString(3, e.getKey().toString());
                    psUpdate.addBatch();
                }
                psUpdate.executeBatch();
            }
            if (r.getAllContacts().size() == 0 && contactsExist(conn, r.getUuid())) { // Удаление контактов из таблицы
                PreparedStatement psDelete = conn.prepareStatement("DELETE FROM contact WHERE resume_uuid = ?");
                psDelete.setString(1, r.getUuid());
                psDelete.execute();
            }
            return null;
        });
    }

    @Override
    public void save(Resume r) {
        sqlHelper.runTransaction(conn -> {
            try (PreparedStatement ps = resource(conn, "INSERT INTO resume(uuid, full_name) VALUES (?,?)")) {
                ps.setString(1, r.getUuid());
                ps.setString(2, r.getFullName());
                ps.execute();
            }
            try (PreparedStatement ps = resource(conn, "INSERT INTO contact(resume_uuid, type, value) VALUES (?,?,?)")) {
                insertContact(ps, r).executeBatch();
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
        sqlHelper.runTransaction(conn -> {
            try (PreparedStatement ps = resource(conn, "DELETE FROM resume r WHERE r.uuid = ?")) {
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
        return sqlHelper.runRequest(" " +
                "SELECT r.uuid, r.full_name" +
                " FROM resume AS r" +
                "   ORDER BY r.full_name, r.uuid", psResume -> {
            ResultSet rsResume = psResume.executeQuery();
            return sqlHelper.runRequest(" " +
                    "SELECT c.type, c.value, c.resume_uuid" +
                    " FROM contact AS c" +
                    "  ORDER BY c.resume_uuid", psResume1 -> {
                ResultSet rsContact = psResume1.executeQuery();
                Map<String, Resume> map = new LinkedHashMap<>();
                while (rsResume.next()) {
                    Resume r = new Resume(rsResume.getString("uuid"), rsResume.getString("full_name"));
                    rsContact.beforeFirst();
                    while (rsContact.next()) {
                        if (rsContact.getString("resume_uuid").equals(rsResume.getString("uuid"))) {
                            r.addContact(ContactType.valueOf(rsContact.getString("type")), rsContact.getString("value"));
                        }
                    }
                    map.put(rsResume.getString("full_name"), r);
                }
                return new ArrayList<>(map.values());
            });
        });
    }

    @Override
    public int size() {
        return sqlHelper.runRequest("SELECT COUNT(*) FROM resume", (ps) -> {
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;
        });
    }

    private PreparedStatement resource(Connection conn, String statement) throws SQLException {
        return conn.prepareStatement(statement);
    }

    private PreparedStatement insertContact(PreparedStatement ps, Resume r) throws SQLException {
        for (Map.Entry<ContactType, String> e : r.getAllContacts().entrySet()) {
            ps.setString(1, r.getUuid());
            ps.setString(2, e.getKey().name());
            ps.setString(3, e.getValue());
            ps.addBatch();
        }
        return ps;
    }

    private boolean contactsExist(Connection conn, String uuid) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("SELECT resume_uuid FROM contact WHERE resume_uuid = ?");
        ps.setString(1, uuid);
        return ps.executeQuery().isBeforeFirst();
    }
}