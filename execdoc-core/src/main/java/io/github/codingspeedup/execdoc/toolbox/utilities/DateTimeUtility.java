package io.github.codingspeedup.execdoc.toolbox.utilities;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateTimeUtility {

    public static final String ISO_DATE_PATTERN = "yyyy-MM-dd";
    public static final String COMPACT_ISO_DATE_PATTERN = "yyyyMMdd";
    public static final String ISO_DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String COMPACT_ISO_DATE_TIME_PATTERN = "yyyyMMddHHmmss";

    private static final DateTimeFormatter ISO_DATE_FORMATTER = DateTimeFormatter.ofPattern(ISO_DATE_PATTERN);

    public static String toIsoDateString(Date value) {
        return new SimpleDateFormat(ISO_DATE_PATTERN).format(value);
    }

    public static String toIsoDateTimeString(Date value) {
        return new SimpleDateFormat(ISO_DATE_TIME_PATTERN).format(value);
    }

    public static String toCompactIsoDateTimeString(Date value) {
        return new SimpleDateFormat(COMPACT_ISO_DATE_TIME_PATTERN).format(value);
    }

    public static String toIsoDateString(LocalDate localDate) {
        return ISO_DATE_FORMATTER.format(localDate);
    }

}
