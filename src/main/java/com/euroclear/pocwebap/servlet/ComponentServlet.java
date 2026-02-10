package com.euroclear.pocwebap.servlet;

import java.io.IOException;
import java.util.List;

import com.euroclear.pocwebap.model.Component;
import com.euroclear.pocwebap.service.PackageService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/package-detail")
public class ComponentServlet extends HttpServlet {
    
    private PackageService packageService = new PackageService();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Vérifier si l'utilisateur est connecté
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        // Récupérer l'ID du package
        String packageId = request.getParameter("id");
        
        if (packageId == null || packageId.trim().isEmpty()) {
            request.setAttribute("error", "Package ID is required");
            request.getRequestDispatcher("/packages.jsp").forward(request, response);
            return;
        }
        
        // Récupérer les composants du package
        List<Component> components = packageService.getPackageComponents(packageId);
        
        // Passer les résultats à la JSP
        request.setAttribute("packageId", packageId);
        request.setAttribute("components", components);
        request.getRequestDispatcher("/package-detail.jsp").forward(request, response);
    }
}