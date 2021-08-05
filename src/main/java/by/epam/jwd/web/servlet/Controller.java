package by.epam.jwd.web.servlet;

import by.epam.jwd.web.command.ActionCommand;
import by.epam.jwd.web.command.CommandFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Main servlet that is application controller and
 * performs controller function. Defines what command
 * need to be executed executes it and makes response forward.
 * @author roma0
 * @version 1.0
 * @since 1.0
 */
@WebServlet("/controller")
public class Controller extends HttpServlet {

    /**
     * Reacts on get client request.
     * @param req client request.
     * @param resp client response.
     * @throws ServletException when exception in servlet occurs.
     * @throws IOException when exception in io occurs.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    /**
     * Reacts on post client request.
     * @param req client request.
     * @param resp client response.
     * @throws ServletException when exception in servlet occurs.
     * @throws IOException when excepition in io occurs.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    private void processRequest(HttpServletRequest request,
                                HttpServletResponse response)
            throws ServletException, IOException {
        CommandFactory client = CommandFactory.getInstance();
        ActionCommand command = client.defineCommand(request);
        final String result = command.execute(request);
        request.getRequestDispatcher(result).forward(request, response);
    }
}
