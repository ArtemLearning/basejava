package ru.javawebinar.basejava.storage.serializer;

import ru.javawebinar.basejava.model.*;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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
            writeCollection(dos, r.getAllSections().entrySet(), entry -> {
                SectionType sectionType = entry.getKey();
                Section section = entry.getValue();
                dos.writeUTF(sectionType.name());
                switch (sectionType) {
                    case PERSONAL, OBJECTIVE -> dos.writeUTF(((TextSection) section).getContent());
                    case ACHIEVEMENT, QUALIFICATIONS ->
                            writeCollection(dos, ((ListSection) section).getItems(), dos::writeUTF);
                    case EXPERIENCE, EDUCATION ->
                            writeCollection(dos, ((OrganizationSection) section).getOrganizations(), organization -> {
                                dos.writeUTF(organization.getName());
                                dos.writeUTF(organization.getUrl());
                                writeCollection(dos, organization.getPositions(), position -> {
                                    writeLocalDate(dos, (position.getStartDate()));
                                    writeLocalDate(dos, (position.getEndDate()));
                                    dos.writeUTF(position.getTitle());
                                    dos.writeUTF(position.getDescription());
                                });
                            });
                    default -> throw new IllegalStateException("Unexpected value: " + sectionType.name());
                }
            });
        }
    }

    @Override
    public Resume doRead(InputStream is) throws IOException {
        try (DataInputStream dis = new DataInputStream(is)) {
            String uuid = dis.readUTF();
            String fullName = dis.readUTF();
            Resume resume = new Resume(uuid, fullName);
            readCollection(dis, () -> resume.addContact(ContactType.valueOf(dis.readUTF()), dis.readUTF()));
            // TODO: Implement sections
            readCollection(dis, () -> {
                SectionType sectionType = SectionType.valueOf(dis.readUTF());
                resume.addSection(sectionType, readSingleSection(dis, sectionType));
            });
            return resume;
        }
    }

    @FunctionalInterface
    public interface ActionWrite<T> {
        void write(T t) throws IOException;
    }

    @FunctionalInterface
    public interface ActionRead<T> {
        T read() throws IOException;

    }

    @FunctionalInterface
    public interface Action<T> {
        void operate() throws IOException;

    }

    private Section readSingleSection(DataInputStream dis, SectionType sectionType) throws IOException {
        switch (sectionType) {
            case PERSONAL, OBJECTIVE -> {
                return new TextSection(dis.readUTF());
            }
            case ACHIEVEMENT, QUALIFICATIONS -> {
                return new ListSection(readListCollection(dis, dis::readUTF));
            }
            case EXPERIENCE, EDUCATION -> {
                return new OrganizationSection(readListCollection(dis, () ->
                        new Organization(new Link(dis.readUTF(), dis.readUTF()),
                                readListCollection(dis, () ->
                                        new Organization.Position(
                                                readLocalDate(dis), readLocalDate(dis),
                                                dis.readUTF(), dis.readUTF())))));
            }
            default -> throw new IllegalStateException("Unexpected value: " + sectionType.name());
        }
    }

    private void writeLocalDate(DataOutputStream dos, LocalDate date) throws IOException {
        dos.writeInt(date.getYear());
        dos.writeInt(date.getMonthValue());
    }

    private LocalDate readLocalDate(DataInputStream dis) throws IOException {
        return LocalDate.of(dis.readInt(), dis.readInt(), 1);
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

    private <T> List<T> readListCollection(DataInputStream dis, ActionRead<T> element) throws IOException {
        final List<T> list = new ArrayList<>();
        int records = dis.readInt();
        for (int i = 0; i < records; i++) {
            list.add(element.read());
        }
        return list;
    }
}
