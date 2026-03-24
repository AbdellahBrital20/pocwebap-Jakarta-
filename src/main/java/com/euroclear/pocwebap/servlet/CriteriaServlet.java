package com.euroclear.pocwebap.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * Servlet pour la page de critères de recherche
 */
@WebServlet("/criteria")
public class CriteriaServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Note: Authentication check is now handled by AuthenticationFilter
        
        request.getRequestDispatcher("/criteria.jsp").forward(request, response);
    }
}