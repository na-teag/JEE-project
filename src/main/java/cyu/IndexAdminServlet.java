package cyu;

import cyu.schoolmanager.Admin;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(name = "IndexAdminServlet", urlPatterns = "/indexAdmin")
public class IndexAdminServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        if (session.getAttribute("user") != null && session.getAttribute("role").equals(Admin.class.getName())) {
            request.getRequestDispatcher("WEB-INF/views/indexAdmin.jsp").forward(request, response);
        } else {
            request.getRequestDispatcher("WEB-INF/views/error.jsp").forward(request, response);
        }
    }
}
