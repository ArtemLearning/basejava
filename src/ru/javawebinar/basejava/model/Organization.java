package ru.javawebinar.basejava.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Organization {
    private final Link homePage;

    private final List<Period> periodList;

    public Organization(String name, String url, List<Period> periodList) {
        Objects.requireNonNull(name);
        this.homePage = new Link(name, url);
        this.periodList = periodList;
    }

    public Organization(String name, String url, LocalDate startDate, LocalDate endDate, String title, String description) {
        Objects.requireNonNull(name);
        this.homePage = new Link(name, url);
        Period period = new Period(startDate, endDate, title, description);
        this.periodList = new ArrayList<>();
        periodList.add(period);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Organization that = (Organization) o;
        return Objects.equals(homePage, that.homePage) && Objects.equals(periodList, that.periodList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(homePage, periodList);
    }

    @Override
    public String toString() {
        return "Organization{" +
                "homePage=" + homePage +
                ", periodList=" + periodList +
                '}';
    }
}