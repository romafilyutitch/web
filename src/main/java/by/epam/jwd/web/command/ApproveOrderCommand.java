package by.epam.jwd.web.command;

import by.epam.jwd.web.service.ServiceException;
import by.epam.jwd.web.service.SimpleOrderService;

import javax.servlet.http.HttpServletRequest;

public class ApproveOrderCommand implements ActionCommand {
    @Override
    public String execute(HttpServletRequest request) {
        final Long id = Long.valueOf(request.getParameter("id"));
        try {
            SimpleOrderService.getInstance().approveOrder(id);
            return "controller?command=show_orders";
        } catch (ServiceException e) {
            request.setAttribute("error", e.getMessage());
            return "controller?command=show_orders";
        }
    }
}
