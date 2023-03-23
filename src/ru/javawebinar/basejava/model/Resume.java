package ru.javawebinar.basejava.model;

import java.util.EnumMap;
import java.util.Objects;
import java.util.UUID;

/**
 * Initial resume class
 */
public class Resume extends AbstractSection {
    // Unique identifier
    private final String uuid;
    private final String fullName;
    private EnumMap<ContactType, String> contacts = new EnumMap<>(ContactType.class);
    private EnumMap<SectionType, AbstractSection> sections = new EnumMap<>(SectionType.class);

    public Resume() {
        this(null);
    }

    public Resume(String fullName) {
        this((UUID.randomUUID().toString()), fullName);
    }

    public Resume(String uuid, String fullName) {
        Objects.requireNonNull(uuid, "Uuid должен быть заполнен");
        Objects.requireNonNull(fullName, "fullName должен быть заполнен");
        this.uuid = uuid;
        this.fullName = fullName;
    }

    public String getUuid() {
        return uuid;
    }

    public String getFullName() {
        return fullName;
    }

    public EnumMap<ContactType, String> getContacts() {
        return contacts;
    }

    public EnumMap<SectionType, AbstractSection> getSections() {
        return sections;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Resume resume = (Resume) o;

        if (!uuid.equals(resume.uuid)) return false;
        return fullName.equals(resume.fullName);
    }

    @Override
    public int hashCode() {
        int result = uuid.hashCode();
        result = 31 * result + fullName.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Resume{" +
                "uuid='" + uuid + '\'' +
                ", fullName='" + fullName + '\'' +
                ", contacts=" + contacts +
                ", sections=" + sections +
                '}';
    }
}
