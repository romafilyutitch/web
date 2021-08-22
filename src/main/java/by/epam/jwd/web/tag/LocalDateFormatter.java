package by.epam.jwd.web.tag;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

/**
 * Makes local date formatting for jsp page.
 * @author roma0
 * @version 1.0
 * @since 1.0
 */
public class LocalDateFormatter {

    /**
     * Return formatted local date in different languages based on current default locale.
     * @param date that need to be formatted.
     * @return formatted date in string representation.
     */
    public static String parseLocalDate(LocalDate date) {
        final DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG).withLocale(Locale.getDefault());
        return formatter.format(date);
    }
}
