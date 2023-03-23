package ru.javawebinar.basejava.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Period {
    private final LocalDate beginDate;
    private final LocalDate endDate;
    private final String description;

    public Period(LocalDate beginDate, LocalDate endDate, String description) {
        this.beginDate = beginDate;
        this.endDate = endDate;
        this.description = description;
    }

    public LocalDate getBeginDate() {
        return beginDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "Дата начала - " + beginDate.format(DateTimeFormatter.ofPattern("MM/yyyy")) + "\n" +
               "Дата окончания - " + endDate.format(DateTimeFormatter.ofPattern("MM/yyyy")) + "\n" +
               "Описание \n " + description + "\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Period period = (Period) o;
        return Objects.equals(beginDate, period.beginDate) && Objects.equals(endDate, period.endDate) && Objects.equals(description, period.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(beginDate, endDate, description);
    }
}
