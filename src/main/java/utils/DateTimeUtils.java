//https://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html
package utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public final class DateTimeUtils {

    private DateTimeUtils() {}

    public static LocalDateTime parseAcme(String dateAsString) {
        //String dateString = "08:45:14.027 EEST May 10 2023";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSS z MMM d yyyy", Locale.ENGLISH);
        return LocalDateTime.parse(dateAsString, formatter);
    }

    public static LocalDateTime parseOsv(String dateAsString) {
        //String dateString = "2023-02-01T14:27:56.3+0200";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-dd-MM'T'HH:mm:ss.SZZZ", Locale.ENGLISH);
        return LocalDateTime.parse(dateAsString, formatter);
    }
}
