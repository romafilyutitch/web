package by.epam.jwd.web.command;

import by.epam.jwd.web.service.ServiceException;
import by.epam.jwd.web.service.ServiceFactory;

import javax.servlet.http.HttpServletRequest;

public class ApproveOrderCommand implements ActionCommand {

    public static final String ID = "id";
    public static final String COMMAND_RESULT = "commandResult";
    public static final String RESULT_MESSAGE = "order was approved";

    private ApproveOrderCommand() {
    }

    public static ApproveOrderCommand getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public String execute(HttpServletRequest request) {
        final Long id = Long.valueOf(request.getParameter(ID));
        try {
            ServiceFactory.getInstance().getOrderService().approveOrder(id);
            request.getSession().setAttribute(COMMAND_RESULT, RESULT_MESSAGE);
            return null;
        } catch (ServiceException e) {
            request.getSession().setAttribute(COMMAND_RESULT, e.getMessage());
            return null;
        }
    }

    private static class Singleton {
        private static final ApproveOrderCommand INSTANCE = new ApproveOrderCommand();
    }
}
