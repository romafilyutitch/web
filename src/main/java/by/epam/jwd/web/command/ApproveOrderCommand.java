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
            request.getSession().setAttribute("commandResult", "order was approved");
            return null;
        } catch (ServiceException e) {
            request.getSession().setAttribute("commandResult", e.getMessage());
            return null;
        }
    }
}
