package ru.javawebinar.topjava.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static boolean isBetweenHalfOpenDate(LocalDateTime lt, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        LocalDate mealDate = lt.toLocalDate();
        LocalDate startDate = startDateTime.toLocalDate();
        LocalDate endDate = endDateTime.toLocalDate();
        return mealDate.compareTo(startDate) >= 0
                && mealDate.compareTo(endDate) < 0;
    }

    public static boolean isBetweenHalfOpenTime(LocalDateTime lt, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        LocalTime mealTime = lt.toLocalTime();
        LocalTime startTime = startDateTime.toLocalTime();
        LocalTime endTime = endDateTime.toLocalTime();
        return mealTime.compareTo(startTime) >= 0
                && mealTime.compareTo(endTime) < 0;
    }

    public static String toString(LocalDateTime ldt) {
        return ldt == null ? "" : ldt.format(DATE_TIME_FORMATTER);
    }
}

