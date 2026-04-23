package com.euroclear.pocwebap.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/clear-filters")
public class ClearFiltersServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        
        if (session != null) {
            session.removeAttribute("filter_packageName");
            session.removeAttribute("filter_statuses");
            session.removeAttribute("filter_creator");
            session.removeAttribute("filter_workRequest");
            session.removeAttribute("filter_action");
            session.removeAttribute("filter_material");
            session.removeAttribute("filter_installFrom");
            session.removeAttribute("filter_installTo");
            session.removeAttribute("filter_createFrom");
            session.removeAttribute("filter_createTo");
        }
        
        response.sendRedirect(request.getContextPath() + "/criteria.jsp");
    }
}