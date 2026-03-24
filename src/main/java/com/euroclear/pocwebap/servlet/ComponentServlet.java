package com.euroclear.pocwebap.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.euroclear.pocwebap.model.Component;
import com.euroclear.pocwebap.service.PackageService;
import com.euroclear.pocwebap.util.Constants;
import com.euroclear.pocwebap.util.InputSanitizer;

/**
 * Servlet pour afficher les composants d'un package
 */
@WebServlet("/package-detail")
public class ComponentServlet extends HttpServlet {
    
    private PackageService packageService = new PackageService();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 
        
        // Get and sanitize package ID (OWASP)
        String packageId = InputSanitizer.sanitize(request.getParameter("id"));
        
        // Validate input
        if (!InputSanitizer.isNotEmpty(packageId)) {
            request.setAttribute(Constants.ATTR_ERROR, "Package ID is required");
            request.getRequestDispatcher("/packages.jsp").forward(request, response);
            return;
        }
        
        // Get package components
        List<Component> components = packageService.getPackageComponents(packageId);
        
        // Pass results to JSP
        request.setAttribute("packageId", packageId);
        request.setAttribute(Constants.ATTR_COMPONENTS, components);
        request.getRequestDispatcher("/package-detail.jsp").forward(request, response);
    }
}