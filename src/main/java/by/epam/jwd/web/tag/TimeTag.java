package by.epam.jwd.web.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class TimeTag extends TagSupport {
    @Override
    public int doStartTag() throws JspException {
        final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);
        final String currentDateTime = "<b>Current date and time " + dateTimeFormatter.format(LocalDateTime.now()) + "</b>";
        final JspWriter out = pageContext.getOut();
        try {
            out.println(currentDateTime);
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
