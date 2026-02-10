package com.euroclear.pocwebap.servlet;

import java.io.IOException;
import java.util.List;

import com.euroclear.pocwebap.model.Package;
import com.euroclear.pocwebap.service.PackageService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/packages")
public class PackageServlet extends HttpServlet {
    
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
        
        // Récupérer les paramètres de recherche
        String packageName = request.getParameter("packageName");
        String[] statuses = request.getParameterValues("status");
        String[] levels = request.getParameterValues("level");
        String[] types = request.getParameterValues("type");
        String creator = request.getParameter("creator");
        String workRequest = request.getParameter("workRequest");
        String action = request.getParameter("action");
        String dept = request.getParameter("dept");
        String installDateFrom = request.getParameter("installDateFrom");
        String installDateTo = request.getParameter("installDateTo");
        String creationDateFrom = request.getParameter("creationDateFrom");
        String creationDateTo = request.getParameter("creationDateTo");
        
        // Rechercher les packages
        List<Package> packages = packageService.searchPackages(
            packageName, statuses, levels, types, creator, workRequest,
            action, dept, installDateFrom, installDateTo, creationDateFrom, creationDateTo
        );
        
        // Passer les résultats à la JSP
        request.setAttribute("packages", packages);
        request.setAttribute("packageService", packageService);
        request.getRequestDispatcher("/packages.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        doGet(request, response);
    }
}