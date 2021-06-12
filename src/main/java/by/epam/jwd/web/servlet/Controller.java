package by.epam.jwd.web.servlet;

import by.epam.jwd.web.command.ActionCommand;
import by.epam.jwd.web.command.ActionFactory;
import by.epam.jwd.web.connectionPool.ConnectionPool;
import by.epam.jwd.web.connectionPool.ConnectionPoolInitializationException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
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
        if (command != null) {
            String page = command.execute(request);
            if (page != null) {
                RequestDispatcher dispatcher = request.getRequestDispatcher(page);
                dispatcher.forward(request, response);
            } else {
                response.sendRedirect(INDEX_JSP_PATH);
            }
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
