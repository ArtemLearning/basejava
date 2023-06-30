package ru.javawebinar.basejava.util;

import ru.javawebinar.basejava.model.Organization;

import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

public class DateUtil {

    public static final LocalDate NOW = LocalDate.of(3000, 1, 1);
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("MM/yyyy");

    public static LocalDate of(int year, Month month) {
        return LocalDate.of(year, month, 1);
    }

    public static String format(LocalDate date) {
        if (date == null) return "";
        return date.format(FORMATTER);
    }

    public static String formatDates(Organization.Position position) {
        return position.getEndDate().equals(NOW) ? "Текущее место работы" :
                DateUtil.format(position.getStartDate()) + " - " + DateUtil.format(position.getEndDate());
    }

    public static LocalDate parse(String date) {
        if (date == null || "Сейчас".equals(date)) return NOW;
        YearMonth yearMonth = YearMonth.parse(date, FORMATTER);
        return LocalDate.of(yearMonth.getYear(), yearMonth.getMonth(), 1);
    }
}