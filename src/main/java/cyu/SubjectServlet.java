package cyu;

import cyu.schoolmanager.*;
import cyu.schoolmanager.service.SubjectManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(name = "SubjectServlet", urlPatterns = {"/subjects", "/subject"})
public class SubjectServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String path = request.getServletPath();
        SubjectManager subjectManager = SubjectManager.getInstance();

        if (session.getAttribute("user") != null && Admin.class.getName().equals(session.getAttribute("role"))){
            if ("/subjects".equals(path)) {
                session.setAttribute("subjects", subjectManager.getListOfSubject());
                request.getRequestDispatcher("WEB-INF/views/subjects.jsp").forward(request, response);
            } else if ("/subject".equals(path)) {
                String action = request.getParameter("action");
                String name = request.getParameter("name");
                String id = request.getParameter("id");

                if (name != null) {
                    if ("save".equals(action)) {
                        if (id == null || id.isEmpty()) {
                            // if there is no id, then it is a new object
                            String error = subjectManager.createSubject(name);
                            if (error != null && !error.isEmpty()) {
                                System.out.println(error);
                                request.setAttribute("errorMessage", error);
                            }
                        } else {
                            // if there is an id the object already exists
                            String error = subjectManager.updateSubjectById(id, name);
                            if (error != null && !error.isEmpty()) {
                                request.setAttribute("errorMessage", error);
                                System.out.println(error);
                            }
                        }
                        request.getRequestDispatcher("/subjects").forward(request, response);
                    } else if ("delete".equals(action) && id != null && !id.isEmpty()) {
                        String error = subjectManager.deleteSubjectById(id);
                        if (error != null) {
                            request.setAttribute("errorMessage", error);
                        }
                        request.getRequestDispatcher("/subjects").forward(request, response);
                    } else {
                        request.setAttribute("errorMessage", "Requête non reconnue");
                        request.getRequestDispatcher("/subjects").forward(request, response);
                    }
                }
                request.setAttribute("errorMessage", "Paramètres manquants, impossible de traiter la requête");
                request.getRequestDispatcher("/subjects").forward(request, response);
            }
        } else {
            request.getRequestDispatcher("WEB-INF/views/error.jsp").forward(request, response);
        }

    }
}