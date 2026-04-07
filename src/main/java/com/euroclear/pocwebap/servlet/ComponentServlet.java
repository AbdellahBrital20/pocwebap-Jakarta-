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
import com.euroclear.pocwebap.model.Component;
import com.euroclear.pocwebap.service.PackageService;
import com.euroclear.pocwebap.service.RestApiService;
import com.euroclear.pocwebap.util.Constants;
import com.euroclear.pocwebap.util.InputSanitizer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Servlet pour afficher les composants d'un package
 */
@WebServlet("/package-detail")
public class ComponentServlet extends HttpServlet {
    
    private PackageService packageService = new PackageService();
    
    @Override
protected void doGet(HttpServletRequest request, HttpServletResponse response) 
        throws ServletException, IOException {
    
    // Note: Authentication check is now handled by AuthenticationFilter
    
    HttpSession session = request.getSession(false);
    
    // Get and sanitize package ID (OWASP)
    String packageId = InputSanitizer.sanitize(request.getParameter("id"));
    
    // Validate input
    if (!InputSanitizer.isNotEmpty(packageId)) {
        request.setAttribute(Constants.ATTR_ERROR, "Package ID is required");
        request.getRequestDispatcher("/packages.jsp").forward(request, response);
        return;
    }
    
    List<Component> components = new ArrayList<>();
    
    if (AppConfig.isTestMode()) {
        // Test mode - use JSON files
        components = packageService.getPackageComponents(packageId);
    } else {
        // Production mode - call REST API
        String sessionId = (String) session.getAttribute("API_SESSION_ID");
        
        if (sessionId != null) {
            String jsonResponse = RestApiService.getComponents(packageId, sessionId);
            
            if (jsonResponse != null) {
                components = parseComponentsFromJson(jsonResponse);
            } else {
                request.setAttribute(Constants.ATTR_ERROR, "Error: " + RestApiService.getLastError());
            }
        } else {
            request.setAttribute(Constants.ATTR_ERROR, "Session expired. Please login again.");
        }
    }
    
    // Pass results to JSP
    request.setAttribute("packageId", packageId);
    request.setAttribute(Constants.ATTR_COMPONENTS, components);
    request.getRequestDispatcher("/package-detail.jsp").forward(request, response);
}

private List<Component> parseComponentsFromJson(String json) {
    List<Component> components = new ArrayList<>();
    
    try {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(json);
        JsonNode results = root.get("result");
        
        if (results != null && results.isArray()) {
            for (JsonNode node : results) {
                Component comp = new Component();
                comp.setApplName(node.has("applName") ? node.get("applName").asText() : "");
                comp.setComponent(node.has("component") ? node.get("component").asText() : "");
                comp.setComponentType(node.has("componentType") ? node.get("componentType").asText() : "");
                comp.setComponentStatus(node.has("componentStatus") ? node.get("componentStatus").asText() : "");
                comp.setDateLastModified(node.has("dateLastModified") ? node.get("dateLastModified").asText() : "");
                comp.setTimeLastModified(node.has("timeLastModified") ? node.get("timeLastModified").asText() : "");
                comp.setUpdater(node.has("updater") ? node.get("updater").asText() : "");
                comp.setBuildProc(node.has("buildProc") ? node.get("buildProc").asText() : "");
                comp.setLanguage(node.has("language") ? node.get("language").asText() : "");
                comp.setVersion(node.has("version") ? node.get("version").asInt() : 0);
                comp.setSourceLib(node.has("sourceLib") ? node.get("sourceLib").asText() : "");
                comp.setTargetComponent(node.has("targetComponent") ? node.get("targetComponent").asText() : "");
                comp.setUserOption03(node.has("userOption03") ? node.get("userOption03").asText() : "");
                comp.setUserOption05(node.has("userOption05") ? node.get("userOption05").asText() : "");
                comp.setUserOption07(node.has("userOption07") ? node.get("userOption07").asText() : "");
                comp.setUserOption08(node.has("userOption08") ? node.get("userOption08").asText() : "");
                components.add(comp);
            }
        }
    } catch (Exception e) {
        System.err.println("Error parsing JSON: " + e.getMessage());
    }
    
    return components;
}
}