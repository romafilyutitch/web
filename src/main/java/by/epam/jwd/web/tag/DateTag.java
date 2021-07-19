package by.epam.jwd.web.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;
import java.util.StringJoiner;

public class DateTag extends TagSupport {
    private static final Locale RUSSIAN_LOCALE = new Locale("ru", "RU");
    private static final Locale ENGLISH_LOCALE = new Locale("en", "US");

    @Override
    public int doStartTag() throws JspException {

        final DateTimeFormatter englishFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(RUSSIAN_LOCALE);
        final DateTimeFormatter russianFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(ENGLISH_LOCALE);
        final StringJoiner joiner = new StringJoiner(",");
        joiner.add(englishFormatter.format(LocalDate.now())).add(russianFormatter.format(LocalDate.now()));
        final String currentDate = joiner.toString();
        final JspWriter out = pageContext.getOut();
        try {
            out.println(currentDate);
        } catch (IOException e) {
            throw new JspException(e.getMessage());
        }
        return SKIP_BODY;
    }

    @Override
    public int doEndTag() throws JspException {
        return EVAL_PAGE;
    }
}
