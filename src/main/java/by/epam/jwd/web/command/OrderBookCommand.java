package by.epam.jwd.web.command;

import by.epam.jwd.web.model.User;
import by.epam.jwd.web.service.ServiceException;
import by.epam.jwd.web.service.SimpleBookService;
import by.epam.jwd.web.service.SimpleOrderService;

import javax.servlet.http.HttpServletRequest;

public class OrderBookCommand implements ActionCommand {
    @Override
    public String execute(HttpServletRequest request) {
        final User user = (User) request.getSession().getAttribute("user");
        final Long bookId = Long.valueOf(request.getParameter("id"));
        final Long userId = user.getId();
        try {
            SimpleOrderService.getInstance().createOrder(userId, bookId);
            SimpleBookService.getInstance().removeOneCopy(bookId);
            return "controller?command=main";//todo: maybe forward to MY ORDERS PAGE in future
        } catch (ServiceException e) {
            request.setAttribute("error", e.getMessage());
            return "controller?command=main";
        }
    }
}