package com.euroclear.pocwebap.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.euroclear.pocwebap.model.Package;
import com.euroclear.pocwebap.service.PackageService;
import com.euroclear.pocwebap.util.Constants;
import com.euroclear.pocwebap.util.InputSanitizer;

/**
 * Servlet pour rechercher et afficher les packages
 */
@WebServlet("/packages")
public class PackageServlet extends HttpServlet {
    
    private PackageService packageService = new PackageService();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Note: Authentication check is now handled by AuthenticationFilter
        
        // Get and sanitize search parameters (OWASP)
        String packageName = InputSanitizer.sanitize(request.getParameter("packageName"));
        String[] statuses = request.getParameterValues("status");
        String[] levels = request.getParameterValues("level");
        String[] types = request.getParameterValues("type");
        String creator = InputSanitizer.sanitize(request.getParameter("creator"));
        String workRequest = InputSanitizer.sanitize(request.getParameter("workRequest"));
        String action = InputSanitizer.sanitize(request.getParameter("action"));
        String dept = InputSanitizer.sanitize(request.getParameter("dept"));
        String installDateFrom = InputSanitizer.sanitize(request.getParameter("installDateFrom"));
        String installDateTo = InputSanitizer.sanitize(request.getParameter("installDateTo"));
        String creationDateFrom = InputSanitizer.sanitize(request.getParameter("creationDateFrom"));
        String creationDateTo = InputSanitizer.sanitize(request.getParameter("creationDateTo"));
        
        // Search packages
        List<Package> packages = packageService.searchPackages(
            packageName, statuses, levels, types, creator, workRequest,
            action, dept, installDateFrom, installDateTo, creationDateFrom, creationDateTo
        );
        
        // Pass results to JSP
        request.setAttribute(Constants.ATTR_PACKAGES, packages);
        request.setAttribute("packageService", packageService);
        request.getRequestDispatcher("/packages.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        doGet(request, response);
    }
}