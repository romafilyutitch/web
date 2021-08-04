package by.epam.jwd.web.servlet;

import by.epam.jwd.web.command.ActionCommand;
import by.epam.jwd.web.command.CommandFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/controller")
public class Controller extends HttpServlet {

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
        CommandFactory client = CommandFactory.getInstance();
        ActionCommand command = client.defineCommand(request);
        final String result = command.execute(request);
        request.getRequestDispatcher(result).forward(request, response);
    }
}
