package ru.javawebinar.basejava.model;

import java.util.List;
import java.util.Objects;

public class OrganizationSection extends AbstractSection {
    private final List<Organization> section;

    public OrganizationSection(List<Organization> section) {
        this.section = section;
    }

    public List<Organization> getSection() {
        return section;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrganizationSection that = (OrganizationSection) o;
        return Objects.equals(section, that.section);
    }

    @Override
    public int hashCode() {
        return Objects.hash(section);
    }

    @Override
    public String toString() {
        String result = "\n";
        for (Organization organization: section) {
            result = result.concat(organization.getName());
            result = result.concat("\n");
            result = result.concat(organization.getJob());
            result = result.concat("\n");
            for (Period period: organization.getPeriods()) {
                result = result.concat(period.toString());
            }
            result = result.concat(organization.getWebsite());
            result = result.concat("\n\n");
        }
        return result;
    }
}
