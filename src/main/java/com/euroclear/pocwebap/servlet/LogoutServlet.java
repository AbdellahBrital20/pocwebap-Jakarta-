package com.euroclear.pocwebap.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.euroclear.pocwebap.service.RestApiService;

/**
 * Servlet pour le logout
 */
@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        
        if (session != null) {
            // Logout from API
            String apiSessionId = (String) session.getAttribute("API_SESSION_ID");
            if (apiSessionId != null) {
                RestApiService.logout(apiSessionId);
            }
            
            // Destroy local session
            session.invalidate();
        }
        
        response.sendRedirect(request.getContextPath() + "/login.jsp");
    }
}