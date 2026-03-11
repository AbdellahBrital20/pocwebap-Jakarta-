package com.euroclear.pocwebap.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.euroclear.pocwebap.config.OAuthConfig;

/**
 * Servlet pour le login 
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    
    // Mode test avec admin/admin (
    private static final boolean TEST_MODE = true;
    private static final String TEST_USER = "admin";
    private static final String TEST_PASSWORD = "admin";
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        if (TEST_MODE) {
            // Mode test - afficher la page de login normale
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        } else {
            // Mode production - rediriger vers OAuth
            String authUrl = OAuthConfig.AUTHORIZATION_URL 
                + "?client_id=" + OAuthConfig.CLIENT_ID
                + "&redirect_uri=" + OAuthConfig.REDIRECT_URI
                + "&response_type=code"
                + "&scope=openid profile";
            response.sendRedirect(authUrl);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        if (TEST_MODE) {
            // Mode test - vérifier admin/admin
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            
            if (TEST_USER.equals(username) && TEST_PASSWORD.equals(password)) {
                HttpSession session = request.getSession();
                session.setAttribute("user", username);
                response.sendRedirect(request.getContextPath() + "/criteria");
            } else {
                request.setAttribute("error", "Invalid username or password");
                request.getRequestDispatcher("/login.jsp").forward(request, response);
            }
        } else {
            // Mode production - pas de POST, utiliser OAuth
            response.sendRedirect(request.getContextPath() + "/login");
        }
    }
}