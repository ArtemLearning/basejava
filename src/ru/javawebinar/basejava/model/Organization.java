package ru.javawebinar.basejava.model;

import java.util.List;
import java.util.Objects;

public class Organization {
    private final String name;
    private final String job;
    private final List<Period> periods;
    private final String website;

    public Organization(String name, String job, List<Period> periods, String website) {
        this.name = name;
        this.job = job;
        this.periods = periods;
        this.website = website;
    }

    public String getName() {
        return name;
    }

    public String getJob() {
        return job;
    }

    public String getWebsite() {
        return website;
    }

    public List<Period> getPeriods() {
        return periods;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Organization that = (Organization) o;
        return Objects.equals(name, that.name) && Objects.equals(job, that.job) && Objects.equals(periods, that.periods) && Objects.equals(website, that.website);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, job, periods, website);
    }

    @Override
    public String toString() {
        return  name + '\n' +
                job + '\n' +
                periods + '\n' +
                website + '\n';
    }
}