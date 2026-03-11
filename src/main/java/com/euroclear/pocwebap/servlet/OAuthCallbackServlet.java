package com.euroclear.pocwebap.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.nimbusds.jwt.JWTClaimsSet;

import com.euroclear.pocwebap.service.JwtService;

/**
 * Servlet pour recevoir le callback OAuth après authentification
 */
@WebServlet("/oauth/callback")
public class OAuthCallbackServlet extends HttpServlet {
    
    private JwtService jwtService = new JwtService();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Récupérer le token JWT depuis le header ou le paramètre
        String token = request.getParameter("id_token");
        if (token == null) {
            token = request.getHeader("Authorization");
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
        }
        
        if (token == null) {
            request.setAttribute("error", "No token received");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }
        
        try {
            // Valider le token
            JWTClaimsSet claims = jwtService.validateToken(token);
            
            // Extraire les informations utilisateur
            String username = jwtService.getUsername(claims);
            String racfId = jwtService.getRacfId(claims);
            
            // Créer la session
            HttpSession session = request.getSession();
            session.setAttribute("user", username);
            session.setAttribute("racfId", racfId);
            session.setAttribute("claims", claims);
            
            // Rediriger vers la page de recherche
            response.sendRedirect(request.getContextPath() + "/criteria");
            
        } catch (Exception e) {
            request.setAttribute("error", "Token validation failed: " + e.getMessage());
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }
}