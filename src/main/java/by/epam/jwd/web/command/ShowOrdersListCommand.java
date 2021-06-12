package by.epam.jwd.web.command;

import by.epam.jwd.web.model.BookOrder;
import by.epam.jwd.web.service.SimpleOrderService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class ShowOrdersListCommand implements ActionCommand {
    @Override
    public String execute(HttpServletRequest request) {
        final List<BookOrder> orders = SimpleOrderService.getInstance().findAll();
        request.setAttribute("orders", orders);
        return "WEB-INF/jsp/orders.jsp";
    }
}
