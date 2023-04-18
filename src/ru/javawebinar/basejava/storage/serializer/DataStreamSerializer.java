package ru.javawebinar.basejava.storage.serializer;

import ru.javawebinar.basejava.model.ContactType;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.model.Section;
import ru.javawebinar.basejava.model.SectionType;

import java.io.*;
import java.util.Map;

public class DataStreamSerializer implements StreamSerializer {

    @Override
    public void doWrite(Resume r, OutputStream os) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(os)) {
            dos.writeUTF(r.getUuid());
            dos.writeUTF(r.getFullName());
            Map<ContactType, String> contacts = r.getAllContacts();
            dos.writeInt(contacts.size());
            for (Map.Entry<ContactType, String> entry : contacts.entrySet()) {
                dos.writeUTF(entry.getKey().name());
                dos.writeUTF(entry.getValue());
            }
            Map<SectionType, Section> sections = r.getAllSections();
            // TODO: Implement sections
            dos.writeInt(sections.size());
            for (Map.Entry<SectionType, Section> entry : sections.entrySet()) {
                dos.writeUTF(entry.getKey().name());
//                switch (entry.getKey()) {
//                    case PERSONAL, OBJECTIVE -> dos.writeUTF(serializeTextSection((TextSection) entry.getValue()));
//                    case ACHIEVEMENT, QUALIFICATIONS -> serializeListSection(dos, (ListSection) entry.getValue());
//                    case EXPERIENCE, EDUCATION ->
//                            serializeOrganizationSection(dos, (OrganizationSection) entry.getValue());
//                }
            }
        }
    }

    @Override
    public Resume doRead(InputStream is) throws IOException {
        try (DataInputStream dis = new DataInputStream(is)) {
            String uuid = dis.readUTF();
            String fullName = dis.readUTF();
            Resume resume = new Resume(uuid, fullName);
            int size = dis.readInt();
            for (int i = 0; i < size; i++) {
                resume.addContact(ContactType.valueOf(dis.readUTF()), DataInputStream.readUTF(dis));
            }
            // TODO: Implement sections
            size = dis.readInt();
            for (int i = 0; i < size; i++) {
                SectionType sectionType = SectionType.valueOf(dis.readUTF());
                switch (sectionType) {
                    case PERSONAL, OBJECTIVE -> {
//                        TextSection ts = deserializeTextSection(dis.readUTF());
//                        resume.addSection(sectionType, ts);
                    }
                    case ACHIEVEMENT, QUALIFICATIONS -> {
//                        ListSection ls = deserializeListSection(dis);
//                        resume.addSection(sectionType, ls);
                    }
                    case EXPERIENCE, EDUCATION -> {
//                        OrganizationSection os = deserializeOrganizationSection(dis);
//                        resume.addSection(sectionType, os);
                    }
                }
            }
            return resume;

        }
    }

    /* private String serializeTextSection(TextSection ts) {
        return ts.toString();
    }

    private TextSection deserializeTextSection(String s) {
        return new TextSection(s);
    }

    private void serializeListSection(DataOutputStream dos, ListSection ls) throws IOException {
        dos.writeInt(ls.getItems().size());
        for (String s : ls.getItems()) {
            dos.writeUTF(s);
        }
    }

    private ListSection deserializeListSection(DataInputStream dis) throws IOException {
        List<String> ls = new ArrayList<>();
        int size = dis.readInt();
        for (int i = 0; i < size; i++) {
            ls.add(dis.readUTF());
        }
        return new ListSection(ls);
    }

    private void serializeOrganizationSection(DataOutputStream dos, OrganizationSection os) throws IOException {
        dos.writeInt(os.getOrganizations().size());
        for (Organization org : os.getOrganizations()) {
            dos.writeUTF(org.getName());
            String str = (org.getUrl() == null) ? "" : org.getUrl();
            dos.writeUTF(str);
            dos.writeInt(org.getPositions().size());
            for (Organization.Position ops : org.getPositions()) {
                dos.writeUTF(ops.getStartDateString());
                dos.writeUTF(ops.getEndDateString());
                dos.writeUTF(ops.getTitle());
                str = (ops.getDescription() == null) ? "" : ops.getDescription();
                dos.writeUTF(str);
            }
        }
    }

    private OrganizationSection deserializeOrganizationSection(DataInputStream dis) throws IOException {
        List<Organization> org = new ArrayList<>();
        List<Organization.Position> lp = new ArrayList<>();
        int size = dis.readInt();
        for (int i = 0; i < size; i++) {
            String name = dis.readUTF();
            String url = (dis.readUTF().equals("")) ? null : dis.readUTF();
            int positionsSize = dis.readInt();
            for (int j = 0; j < positionsSize; j++) {
                lp = new ArrayList<>();
                LocalDate startDate = LocalDate.parse(dis.readUTF());
                LocalDate endDate = LocalDate.parse(dis.readUTF());
                String title = dis.readUTF();
                String description = (dis.readUTF().equals("")) ? null : dis.readUTF();
                lp.add(new Organization.Position(startDate, endDate, title, description));
            }
            org.add(new Organization(name, url, lp));
        }
        return new OrganizationSection(org);
    } */
}
