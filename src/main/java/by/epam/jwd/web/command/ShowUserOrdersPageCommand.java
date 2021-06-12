package by.epam.jwd.web.command;

import by.epam.jwd.web.model.BookOrder;
import by.epam.jwd.web.model.User;
import by.epam.jwd.web.service.SimpleOrderService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class ShowUserOrdersPageCommand implements ActionCommand {
    @Override
    public String execute(HttpServletRequest request) {
        final User user = (User) request.getSession().getAttribute("user");
        final List<BookOrder> userOrders = SimpleOrderService.getInstance().findByUserId(user.getId());
        request.setAttribute("orders", userOrders);
        return "WEB-INF/jsp/user_orders.jsp";
    }
}
