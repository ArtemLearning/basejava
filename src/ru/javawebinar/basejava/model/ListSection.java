package ru.javawebinar.basejava.model;

import java.util.List;
import java.util.Objects;

public class ListSection extends AbstractSection {
    private final List<String> section;

    public ListSection(List<String> section) {
        this.section = section;
    }

    public List<String> getSection() {
        return section;
    }

    @Override
    public String toString() {
        String listSection = "\n";
        for (String entry : section) {
            listSection = listSection.concat(entry);
            listSection = listSection.concat("\n");
        }
        return listSection;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ListSection that = (ListSection) o;
        return Objects.equals(section, that.section);
    }

    @Override
    public int hashCode() {
        return Objects.hash(section);
    }
}
