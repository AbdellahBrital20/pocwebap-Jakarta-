package com.euroclear.pocwebap.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.euroclear.pocwebap.config.AppConfig;
import com.euroclear.pocwebap.util.Constants;
import com.euroclear.pocwebap.util.InputSanitizer;

/**
 * Servlet pour le login
 * Test mode: admin/admin
 * Production mode: RACF authentication via REST API
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
   
    private static final String TEST_USER = "admin";
    private static final String TEST_PASSWORD = "admin";
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Get and sanitize inputs (OWASP)
        String username = InputSanitizer.sanitize(request.getParameter("username"));
        String password = request.getParameter("password"); // Don't sanitize password
        
        // Validate inputs
        if (!InputSanitizer.isNotEmpty(username) || !InputSanitizer.isNotEmpty(password)) {
            request.setAttribute(Constants.ATTR_ERROR, "Username and password are required");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }
        
        boolean authenticated = false;
        
        if (AppConfig.isTestMode()) {
            // Test mode - check admin/admin
            authenticated = TEST_USER.equals(username) && TEST_PASSWORD.equals(password);
        } else {
            // Production mode - RACF authentication via REST API
            // TODO: Implement APi
            // authenticated = RestApiService.authenticate(username, password);
            authenticated = false;
        }
        
        if (authenticated) {
            HttpSession session = request.getSession();
            session.setAttribute(Constants.SESSION_USER, username);
            session.setAttribute(Constants.SESSION_RACF_ID, username);
            response.sendRedirect(request.getContextPath() + "/criteria");
        } else {
            request.setAttribute(Constants.ATTR_ERROR, "Invalid username or password");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }
}