package by.epam.jwd.web.servlet;

import by.epam.jwd.web.command.ActionCommand;
import by.epam.jwd.web.command.ActionFactory;
import by.epam.jwd.web.command.CommandResult;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/controller")
public class Controller extends HttpServlet {

    public static final String INDEX_JSP_PATH = "index.jsp";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    private void processRequest(HttpServletRequest request,
                                HttpServletResponse response)
            throws ServletException, IOException {
        ActionFactory client = ActionFactory.getInstance();
        ActionCommand command = client.defineCommand(request);
        final CommandResult commandResult = command.execute(request);
        if (commandResult.isRedirect()) {
            response.sendRedirect(commandResult.getResultPath());
        } else {
            final RequestDispatcher dispatcher = request.getRequestDispatcher(commandResult.getResultPath());
            dispatcher.forward(request, response);
        }
    }
}
