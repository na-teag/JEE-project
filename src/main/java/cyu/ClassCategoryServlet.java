package cyu;

import cyu.schoolmanager.*;
import cyu.schoolmanager.service.ClassCategoryManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(name = "ClassCategoryServlet", urlPatterns = {"/classCategories", "/classCategory"})
public class ClassCategoryServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String path = request.getServletPath();
        ClassCategoryManager classCategoryManager = ClassCategoryManager.getInstance();

        if (session.getAttribute("user") != null && Admin.class.getName().equals(session.getAttribute("role"))){
            if ("/classCategories".equals(path)) {
                session.setAttribute("classCategories", classCategoryManager.getListOfClassCategories());
                request.getRequestDispatcher("views/classCategories.jsp").forward(request, response);
            } else if ("/classCategory".equals(path)) {
                String action = request.getParameter("action");
                String name = request.getParameter("name");
                String color = request.getParameter("color");
                String id = request.getParameter("id");

                if (name != null && color != null) {
                    if ("save".equals(action)) {
                        if (id == null || id.isEmpty()) {
                            // if there is no id, then it is a new object
                            String error = classCategoryManager.createClassCategory(name, color);
                            if (error != null && !error.isEmpty()) {
                                System.out.println(error);
                                request.setAttribute("errorMessage", error);
                            }
                        } else {
                            // if there is an id the object already exists
                            String error = classCategoryManager.updateClassCategoryById(id, name, color);
                            if (error != null && !error.isEmpty()) {
                                request.setAttribute("errorMessage", error);
                                System.out.println(error);
                            }
                        }
                        request.getRequestDispatcher("/classCategories").forward(request, response);
                    } else if ("delete".equals(action) && id != null && !id.isEmpty()) {
                        String error = classCategoryManager.deleteClassCategoryById(id);
                        if (error != null) {
                            request.setAttribute("errorMessage", error);
                        }
                        request.getRequestDispatcher("/classCategories").forward(request, response);
                    } else {
                        request.setAttribute("errorMessage", "Requête non reconnue");
                        request.getRequestDispatcher("/classCategories").forward(request, response);
                    }
                }
                request.setAttribute("errorMessage", "Paramètres manquants, impossible de traiter la requête");
                request.getRequestDispatcher("/classCategories").forward(request, response);
            }
        } else {
            request.getRequestDispatcher("views/error.jsp").forward(request, response);
        }

    }
}