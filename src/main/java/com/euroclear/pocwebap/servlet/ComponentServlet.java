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

@WebServlet("/package-detail")
public class ComponentServlet extends HttpServlet {
    
    private PackageService packageService = new PackageService();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        
        String packageId = InputSanitizer.sanitize(request.getParameter("id"));
        
        if (packageId == null || packageId.isEmpty()) {
            request.setAttribute(Constants.ATTR_ERROR, "Package ID is required");
            request.getRequestDispatcher("/packages.jsp").forward(request, response);
            return;
        }
        
        List<Component> components = new ArrayList<>();
        
        if (AppConfig.isTestMode()) {
            components = packageService.getPackageComponents(packageId);
        } else {
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
                    
                    comp.setApplName(getStringValue(node, "applName"));
                    comp.setComponent(getStringValue(node, "component"));
                    comp.setComponentType(getStringValue(node, "componentType"));
                    comp.setComponentStatus(getStringValue(node, "componentStatus"));
                    comp.setDateLastModified(getStringValue(node, "dateLastModified"));
                    comp.setTimeLastModified(getStringValue(node, "timeLastModified"));
                    comp.setUpdater(getStringValue(node, "updater"));
                    comp.setBuildProc(getStringValue(node, "buildProc"));
                    comp.setLanguage(getStringValue(node, "language"));
                    comp.setVersion(node.has("version") ? node.get("version").asInt() : 0);
                    comp.setSourceLib(getStringValue(node, "sourceLib"));
                    comp.setTargetComponent(getStringValue(node, "targetComponent"));
                    comp.setTargetLoadLibType(getStringValue(node, "targetLoadLibType"));
                    
                    comp.setUserOption03(getStringValue(node, "userOption03"));
                    comp.setUserOption05(getStringValue(node, "userOption05"));
                    comp.setUserOption07(getStringValue(node, "userOption07"));
                    comp.setUserOption08(getStringValue(node, "userOption08"));
                    
                    components.add(comp);
                }
            }
        } catch (Exception e) {
            System.err.println("Error parsing JSON: " + e.getMessage());
            e.printStackTrace();
        }
        
        return components;
    }
    
    private String getStringValue(JsonNode node, String fieldName) {
        if (node.has(fieldName) && !node.get(fieldName).isNull()) {
            return node.get(fieldName).asText();
        }
        return "";
    }
}