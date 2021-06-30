package by.epam.jwd.web.tag;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

public class LocalDateParser {

    public static String parseLocalDate(LocalDate date, String language) {
        final Locale locale = new Locale(language);
        final DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(locale);
        return formatter.format(date);
    }
}
