package ru.javawebinar.basejava.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static ru.javawebinar.basejava.util.DateUtil.NOW;
import static ru.javawebinar.basejava.util.DateUtil.of;

public class Organization implements Serializable {
    public static final long serialVersionUID = 1L;
    private final Link homePage;

    private final List<Position> positionList;

    public Organization(String name, String url, Position... positions) {
        Objects.requireNonNull(name);
        this.homePage = new Link(name, url);
        this.positionList = Arrays.asList(positions);
    }

    public Organization(String name, String url, List<Position> positionList) {
        Objects.requireNonNull(name);
        this.homePage = new Link(name, url);
        this.positionList = positionList;
    }

    public Organization(String name, String url, LocalDate startDate, LocalDate endDate, String title, String description) {
        Objects.requireNonNull(name);
        this.homePage = new Link(name, url);
        Position position = new Position(startDate, endDate, title, description);
        this.positionList = new ArrayList<>();
        positionList.add(position);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Organization that = (Organization) o;
        return Objects.equals(homePage, that.homePage) && Objects.equals(positionList, that.positionList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(homePage, positionList);
    }

    @Override
    public String toString() {
        return "Organization{" +
                "homePage=" + homePage +
                ", periodList=" + positionList +
                '}';
    }

    public static class Position implements Serializable {
        public static final long serialVersionUID = 1L;
        private final LocalDate startDate;
        private final LocalDate endDate;
        private final String title;
        private final String description;

        public Position(int startYear, Month startMonth, String title, String description) {
            this(of(startYear, startMonth), NOW, title, description);
        }

        public Position(int startYear, Month startMonth, int endYear, Month endMonth, String title, String description) {
            this(of(startYear, startMonth), of(endYear, endMonth), title, description);
        }

        public Position(LocalDate startDate, LocalDate endDate, String title, String description) {
            Objects.requireNonNull(startDate, "Startdate must be filled");
            Objects.requireNonNull(endDate, "Enddate must be filled");
            Objects.requireNonNull(title, "Title must be filled");
            this.startDate = startDate;
            this.endDate = endDate;
            this.title = title;
            this.description = description;
        }

        public LocalDate getStartDate() {
            return startDate;
        }

        public LocalDate getEndDate() {
            return endDate;
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }

        @Override
        public String toString() {
            return "Дата начала - " + startDate.format(DateTimeFormatter.ofPattern("MM/yyyy")) + "\n" +
                    "Дата окончания - " + endDate.format(DateTimeFormatter.ofPattern("MM/yyyy")) + "\n" +
                    "Должность - " + title + "\n" +
                    "Описание \n " + description + "\n";
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Position position = (Position) o;
            return Objects.equals(startDate, position.startDate) && Objects.equals(endDate, position.endDate) &&
                    Objects.equals(title, position.title) && Objects.equals(description, position.description);
        }

        @Override
        public int hashCode() {
            return Objects.hash(startDate, endDate, title, description);
        }
    }
}