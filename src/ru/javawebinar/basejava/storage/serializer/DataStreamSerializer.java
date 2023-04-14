package ru.javawebinar.basejava.storage.serializer;

import ru.javawebinar.basejava.model.*;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
                switch (entry.getKey()) {
                    case PERSONAL, OBJECTIVE -> {
                        dos.writeUTF(serializeTextSection((TextSection) entry.getValue()));
                    }
                    case ACHIEVEMENT, QUALIFICATIONS -> {
                        dos.writeUTF(serializeListSection((ListSection) entry.getValue()));
                    }
                    case EXPERIENCE, EDUCATION -> {
                        dos.writeUTF(serializeOrganizationSection((OrganizationSection) entry.getValue()));
                    }
                }
//                dos.writeUTF(entry.getValue().toString());
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
                        TextSection ts = deserializeTextSection(dis.readUTF());
                        resume.addSection(sectionType, ts);
                    }
                    case ACHIEVEMENT, QUALIFICATIONS -> {
                        ListSection ls = deserializeListSection(dis.readUTF());
                        resume.addSection(sectionType, ls);
                    }
                    case EXPERIENCE, EDUCATION -> {
                        OrganizationSection os = deserializeOrganizationSection(dis.readUTF());
                        resume.addSection(sectionType, os);
                    }
                }
//                if (sectionType.equals(SectionType.PERSONAL) || sectionType.equals(SectionType.OBJECTIVE)) {
//                    TextSection ts = deserializeTextSection(dis.readUTF());
//                    resume.addSection(sectionType, ts);
//                } else if (sectionType.equals(SectionType.ACHIEVEMENT) || sectionType.equals(SectionType.QUALIFICATIONS)) {
//                    ListSection ls = new ListSection(dis.readUTF());
//                    resume.addSection(sectionType, ls);
//                } else if (sectionType.equals(SectionType.EXPERIENCE) || sectionType.equals(SectionType.EDUCATION)) {
////                    OrganizationSection os = new OrganizationSection(dis.readUTF());
//                    resume.addSection(sectionType, null);
//                }
            }
            return resume;

        }
    }

    private String serializeTextSection(TextSection ts) {
        return ts.toString();
    }

    private TextSection deserializeTextSection(String s) {
        return new TextSection(s);
    }

    private String serializeListSection(ListSection ls) {
        String str = null;
        for (String s : ls.getItems()) {
            str = (str == null) ? s : str.concat(";").concat(s);
        }
        return str;
    }

    private ListSection deserializeListSection(String s) {
        String[] strings = s.split(";");
        return new ListSection(Arrays.asList(strings));
    }

    private String serializeOrganizationSection(OrganizationSection os) {
        String str = null;
        for (Organization org : os.getOrganizations()) {
            str = org.getName() + "$" + org.getUrl() + "@";
            for (Organization.Position ops : org.getPositions()) {
                str = str + ops.getStartDate() + '$' + ops.getEndDate() + "$" + ops.getTitle() + "$" + ops.getDescription() + "$";
            }
            str += "#";
        }
        return str;
    }

    private OrganizationSection deserializeOrganizationSection(String s) {
        List<Organization> org = new ArrayList<>();
        List<Organization.Position> lp = new ArrayList<>();
        String name = null;
        String url = null;
        String positions = null;
        String[] organizations = s.split("#");
        for (int i = 0; i < organizations.length; i++) {
            String[] header = organizations[i].split("@");
            if (header.length == 1) {
                name = header[0];
            } else if (header.length == 2) {
                name = header[0];
                positions = header[1];
            } else if (header.length == 3) {
                name = header[0];
                url = header[1];
                positions = header[2];
            }
            if (positions != null) {
                String[] strings = positions.split("\\$");
                for (int j = 0; j < strings.length; ) {
                    LocalDate startDate = LocalDate.parse(strings[j]);
                    LocalDate endDate = LocalDate.parse(strings[j + 1]);
                    String title = strings[j + 2];
                    String description;
                    try {
                        description = strings[j + 3];
                        j += 4;
                    } catch (ArrayIndexOutOfBoundsException e) {
                        description = null;
                        j += 3;
                    }
                    if (lp != null) {
                        lp.add(new Organization.Position(startDate, endDate, title, description));
                    }
                }
            }
            org.add(new Organization(name, url, lp));
        }
        return new OrganizationSection(org);
    }
}
