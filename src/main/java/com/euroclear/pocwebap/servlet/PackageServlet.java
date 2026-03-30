package com.euroclear.pocwebap.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.euroclear.pocwebap.config.AppConfig;
import com.euroclear.pocwebap.model.Package;
import com.euroclear.pocwebap.service.PackageService;
import com.euroclear.pocwebap.util.Constants;
import com.euroclear.pocwebap.util.InputSanitizer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.euroclear.pocwebap.service.RestApiService;

/**
 * Servlet pour rechercher et afficher les packages
 */
@WebServlet("/packages")
public class PackageServlet extends HttpServlet {
    
    private PackageService packageService = new PackageService();
    
   @Override
protected void doGet(HttpServletRequest request, HttpServletResponse response) 
        throws ServletException, IOException {
    
    HttpSession session = request.getSession(false);
    
    String packageName = InputSanitizer.sanitize(request.getParameter("packageName"));
    if (packageName == null || packageName.isEmpty()) {
        packageName = "*";
    }
    
    List<Package> packages = new ArrayList<>();
    
    if (AppConfig.isTestMode()) {
        // Test mode - use JSON files
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
        
        packages = packageService.searchPackages(
            packageName, statuses, levels, types, creator, workRequest,
            action, dept, installDateFrom, installDateTo, creationDateFrom, creationDateTo
        );
    } else {
        // Production mode - call REST API
        String sessionId = (String) session.getAttribute("API_SESSION_ID");
        
        if (sessionId != null) {
            String jsonResponse = RestApiService.searchPackages(packageName, sessionId);
            
            if (jsonResponse != null) {
                packages = parsePackagesFromJson(jsonResponse);
            } else {
                request.setAttribute(Constants.ATTR_ERROR, "Error: " + RestApiService.getLastError());
            }
        } else {
            request.setAttribute(Constants.ATTR_ERROR, "Session expired. Please login again.");
        }
    }
    
    request.setAttribute(Constants.ATTR_PACKAGES, packages);
    request.setAttribute("packageService", packageService);
    request.getRequestDispatcher("/packages.jsp").forward(request, response);
}
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        doGet(request, response);
    }

    private List<Package> parsePackagesFromJson(String json) {
    List<Package> packages = new ArrayList<>();
    
    try {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(json);
        JsonNode results = root.get("result");
        
        if (results != null && results.isArray()) {
            for (JsonNode node : results) {
                Package pkg = new Package();
                pkg.setPackage(node.has("package") ? node.get("package").asText() : "");
                pkg.setPackageTitle(node.has("packageTitle") ? node.get("packageTitle").asText() : "");
                pkg.setPackageStatus(node.has("packageStatus") ? node.get("packageStatus").asText() : "");
                pkg.setPackageLevel(node.has("packageLevel") ? node.get("packageLevel").asText() : "");
                pkg.setPackageType(node.has("packageType") ? node.get("packageType").asText() : "");
                pkg.setCreator(node.has("creator") ? node.get("creator").asText() : "");
                pkg.setWorkChangeRequest(node.has("workChangeRequest") ? node.get("workChangeRequest").asText() : "");
                pkg.setDateInstalled(node.has("dateInstalled") ? node.get("dateInstalled").asText() : "");
                pkg.setDateCreated(node.has("dateCreated") ? node.get("dateCreated").asText() : "");
                pkg.setRequestorDept(node.has("requestorDept") ? node.get("requestorDept").asText() : "");
                packages.add(pkg);
            }
        }
    } catch (Exception e) {
        System.err.println("Error parsing JSON: " + e.getMessage());
    }
    
    return packages;
}
}