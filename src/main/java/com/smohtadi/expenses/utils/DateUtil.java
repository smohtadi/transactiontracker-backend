package com.smohtadi.expenses.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class DateUtil {
    public final static String VANCOUVER_TZ = "America/Vancouver";
    public final static String UTC_TZ = "UTC";

    public static LocalDateTime utcNow() {
        return LocalDateTime.now().atZone(ZoneId.systemDefault())
                .withZoneSameInstant(ZoneId.of(UTC_TZ))
                .toLocalDateTime();
    }

    private static LocalDateTime to(String date, String fromTz, String toTz,
                                    String format) {
        LocalDateTime transactionDate = LocalDateTime.parse(date,
                DateTimeFormatter.ofPattern(format));
        return transactionDate.atZone(ZoneId.of(fromTz))
                .withZoneSameInstant(ZoneId.of(toTz)).toLocalDateTime();
    }

    public static LocalDateTime toUtc(String date, String fromTz) {
        // "yyyy-MM-dd HH:mm:ss"
        return to(date, fromTz, UTC_TZ, "yyyy-MM-dd HH:mm:ss");
    }

    public static LocalDateTime fromUtc(String date, String toTz) {
        return to(date, UTC_TZ, toTz, "yyyy-MM-dd HH:mm:ss");
    }
    public static String format(LocalDateTime localDateTime, String pattern) {
        return localDateTime.format(DateTimeFormatter.ofPattern(pattern));
    }
    public static String format(LocalDateTime localDateTime) {
        return format(localDateTime, "yyyy-MM-dd");
    }
}
