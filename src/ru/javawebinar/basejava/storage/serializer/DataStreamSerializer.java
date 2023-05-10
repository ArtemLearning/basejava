package ru.javawebinar.basejava.storage.serializer;

import ru.javawebinar.basejava.model.*;

import java.io.*;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;

public class DataStreamSerializer implements StreamSerializer {

    @Override
    public void doWrite(Resume r, OutputStream os) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(os)) {
            dos.writeUTF(r.getUuid());
            dos.writeUTF(r.getFullName());
            Map<ContactType, String> contacts = r.getAllContacts();
            writeCollection(dos, contacts.entrySet(), entry -> {
                dos.writeUTF(entry.getKey().name());
                dos.writeUTF(entry.getValue());
            });
            // TODO: Implement sections
            writeCollection(dos,
                    r.getAllSections().entrySet(),
                    entry -> {
                        SectionType sectionType = entry.getKey();
                        Section section = entry.getValue();
                        dos.writeUTF(sectionType.name());
                        switch (sectionType) {
                            case PERSONAL, OBJECTIVE -> dos.writeUTF(((TextSection) section).toString());
                            case ACHIEVEMENT, QUALIFICATIONS ->
                                    writeCollection(dos, ((ListSection) section).getItems(), dos::writeUTF);
                            case EXPERIENCE, EDUCATION ->
                                    writeCollection(dos, ((OrganizationSection) section).getOrganizations(), organization -> {
                                        dos.writeUTF(organization.getName());
                                        dos.writeUTF(organization.getUrl());
                                        writeCollection(dos, organization.getPositions(), position -> {
                                            dos.writeUTF(getLocalDate(position.getStartDate()));
                                            dos.writeUTF(getLocalDate(position.getEndDate()));
                                            dos.writeUTF(position.getTitle());
                                            dos.writeUTF(position.getDescription());
                                        });
                                    });
                            default -> throw new IllegalStateException("Unexpected value: " + sectionType.name());
                        }
                    });
        }
    }

    private String getLocalDate(LocalDate date) {
        return date.getYear() + date.getMonth().name();
    }

    @Override
    public Resume doRead(InputStream is) throws IOException {
        try (DataInputStream dis = new DataInputStream(is)) {
            String uuid = dis.readUTF();
            String fullName = dis.readUTF();
            Resume resume = new Resume(uuid, fullName);
            readCollection(dis, () -> {
                resume.addContact(ContactType.valueOf(dis.readUTF()), dis.readUTF());
            });
            // TODO: Implement sections
            readCollection(dis, () -> {
                SectionType sectionType = SectionType.valueOf(dis.readUTF());
                resume.addSection(sectionType, readSingleSection(dis, sectionType));
            });
            return resume;
        }
    }

    private Section readSingleSection(DataInputStream dis, SectionType sectionType) throws IOException {
        switch (sectionType) {
            case PERSONAL, OBJECTIVE -> {
                return new TextSection(dis.readUTF());
            }
            case ACHIEVEMENT, QUALIFICATIONS -> {
                return new ListSection();
            }
            case EXPERIENCE, EDUCATION -> {
                return new OrganizationSection();
            }
            default -> throw new IllegalStateException("Unexpected value: " + sectionType.name());
        }
    }


    public interface ActionWrite<T> {
        void write(T t) throws IOException;
    }

    public interface ActionRead<T> {
        T read() throws IOException;

    }

    public interface Action<T> {
        void operate() throws IOException;

    }

    private <T> void writeCollection(DataOutputStream dos, Collection<T> collection, ActionWrite<T> element) throws IOException {
        dos.writeInt(collection.size());
        for (T t : collection) {
            element.write(t);
        }
    }

    private <T> void readCollection(DataInputStream dis, Action<T> element) throws IOException {
        int records = dis.readInt();
        for (int i = 0; i < records; i++) {
            element.operate();
        }
    }
}
