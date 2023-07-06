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
        return date.equals(NOW) ? "Сейчас" : date.format(FORMATTER);
    }

    public static String formatDates(Organization.Position position) {
        return DateUtil.format(position.getStartDate()) + " - " + DateUtil.format(position.getEndDate());
    }

    public static boolean isEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }

    public static LocalDate parse(String date) {
        if (date == null || date.equals("") || "Сейчас".equals(date)) return NOW;
        YearMonth yearMonth = YearMonth.parse(date, FORMATTER);
        return LocalDate.of(yearMonth.getYear(), yearMonth.getMonth(), 1);
    }
}