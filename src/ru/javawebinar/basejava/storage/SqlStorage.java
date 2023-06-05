package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.*;
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
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new StorageException(e);
        }
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
            deleteContacts(conn, r);
            insertContacts(conn, r);
            deleteSections(conn, r);
            insertSections(conn, r);
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
            insertSections(conn, r);
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
                return addAll(conn, new Resume(uuid, rs.getString("full_name")));
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
                addSections(conn, entry.getValue());
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

    private void insertSections(Connection conn, Resume r) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO section(resume_uuid, section_type, position,  content) VALUES (?,?,?,?)")) {
            int index = 0;
            for (Map.Entry<SectionType, Section> e : r.getAllSections().entrySet()) {
                switch (e.getKey()) {
                    case PERSONAL, OBJECTIVE -> {
                        TextSection ts = (TextSection) e.getValue();
                        ps.setString(1, r.getUuid());
                        ps.setString(2, e.getKey().toString());
                        ps.setInt(3, 0);
                        ps.setString(4, ts.getContent());
                        ps.addBatch();
                    }
                    case ACHIEVEMENT, QUALIFICATIONS -> {
                        ListSection ls = (ListSection) e.getValue();
                        ps.setString(1, r.getUuid());
                        ps.setString(2, e.getKey().toString());
                        index = 0;
                        for (String str : ls.getItems()) {
                            ps.setInt(3, index);
                            ps.setString(4, str);
                            ++index;
                            ps.addBatch();
                        }
                    }

                    case EXPERIENCE, EDUCATION -> {  //TODO Add OrganizationSection
                    }
                }
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

    private Resume addSections(Connection conn, Resume r) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM section AS s WHERE s.resume_uuid = ?", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            ps.setString(1, r.getUuid());
            final ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                SectionType st = SectionType.valueOf(rs.getString("section_type"));
                switch (st) {
                    case PERSONAL, OBJECTIVE -> {
                        r.addSection(st, new TextSection(rs.getString("content")));
                    }
                    case ACHIEVEMENT, QUALIFICATIONS -> {
                        r.addSection(st, new ListSection(getListSection(rs, st)));
                    }
                    case EXPERIENCE, EDUCATION -> {
                        //TODO Add OrganizationSection
                    }
                }

            }
        }
        return r;
    }

    private void deleteContacts(Connection conn, Resume r) throws SQLException {
        final PreparedStatement ps = conn.prepareStatement("DELETE FROM contact AS c WHERE c.resume_uuid = ?");
        ps.setString(1, r.getUuid());
        ps.execute();
    }

    private void deleteSections(Connection conn, Resume r) throws SQLException {
        final PreparedStatement ps = conn.prepareStatement("DELETE FROM section AS s WHERE s.resume_uuid = ?");
        ps.setString(1, r.getUuid());
        ps.execute();
    }

    private List<String> getListSection(ResultSet rs, SectionType st) throws SQLException {
        List<String> ls = new ArrayList<>();
        do {
            if (rs.getString("section_type") != null && rs.getString("section_type").equals(st.toString())) {
                int i = rs.getInt("position");
                String content = rs.getString("content");
                ls.add(i, content);
            } else {
                rs.previous();
                break;
            }
        } while (rs.next());
        return ls;
    }

    private Resume addAll(Connection conn, Resume r) throws SQLException {
        addContacts(conn, r);
        addSections(conn, r);
        return r;
    }
}