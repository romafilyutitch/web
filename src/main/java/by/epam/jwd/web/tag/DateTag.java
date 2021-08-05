package by.epam.jwd.web.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

/**
 * Custom date tag that show current date in application.
 * Uses {@link DateTimeFormatter} instance to format current
 * {@link LocalDate} instance based on current locale.
 * @author roma0
 * @version 1.0
 * @since 1.0
 */
public class DateTag extends TagSupport {
    /**
     * Formats and shows current date in different locales
     * based on current default locale.
     * @return tage processing code.
     * @throws JspException when exception in jspWriter operation occurs.
     */
    @Override
    public int doStartTag() throws JspException {
        final DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(Locale.getDefault());
        final String currentDate = formatter.format(LocalDate.now());
        final JspWriter out = pageContext.getOut();
        try {
            out.println(currentDate);
        } catch (IOException e) {
            throw new JspException(e.getMessage());
        }
        return SKIP_BODY;
    }

    /**
     * Makes jsp continue page evaluation.
     * @return continue evaluation command code.
     */
    @Override
    public int doEndTag() {
        return EVAL_PAGE;
    }
}
