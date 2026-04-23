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

import com.euroclear.pocwebap.model.Package;
import com.euroclear.pocwebap.service.PackageService;
import com.euroclear.pocwebap.service.RestApiService;
import com.euroclear.pocwebap.util.Constants;
import com.euroclear.pocwebap.util.InputSanitizer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebServlet("/packages")
public class PackageServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        // Get filter parameters
        String packageName = InputSanitizer.sanitize(request.getParameter("packageName"));
        String[] statuses = request.getParameterValues("status");
        String creator = InputSanitizer.sanitize(request.getParameter("creator"));
        String workRequest = InputSanitizer.sanitize(request.getParameter("workRequest"));
        String action = InputSanitizer.sanitize(request.getParameter("action"));
        String material = InputSanitizer.sanitize(request.getParameter("dept"));
        String installFrom = InputSanitizer.sanitize(request.getParameter("installDateFrom"));
        String installTo = InputSanitizer.sanitize(request.getParameter("installDateTo"));
        String createFrom = InputSanitizer.sanitize(request.getParameter("creationDateFrom"));
        String createTo = InputSanitizer.sanitize(request.getParameter("creationDateTo"));

        // Save filters to session
        session.setAttribute("filter_packageName", packageName);
        session.setAttribute("filter_statuses", statuses);
        session.setAttribute("filter_creator", creator);
        session.setAttribute("filter_workRequest", workRequest);
        session.setAttribute("filter_action", action);
        session.setAttribute("filter_material", material);
        session.setAttribute("filter_installFrom", installFrom);
        session.setAttribute("filter_installTo", installTo);
        session.setAttribute("filter_createFrom", createFrom);
        session.setAttribute("filter_createTo", createTo);

        if (packageName == null || packageName.isEmpty()) {
            packageName = "*";
        }

        List<Package> packages = new ArrayList<>();

        if (session != null && session.getAttribute("API_SESSION_ID") != null) {
            String sessionId = (String) session.getAttribute("API_SESSION_ID");
            
            // Call API with filters
            String jsonResponse = RestApiService.searchPackages(packageName, sessionId,
                    statuses, creator, workRequest, action, material,
                    installFrom, installTo, createFrom, createTo);

            if (jsonResponse != null) {
                packages = parsePackagesFromJson(jsonResponse);

                // Enrich with user records (C and T)
                for (Package pkg : packages) {
                    String userRecJson = RestApiService.getPackageUserRecords(pkg.getPackageId(), sessionId);
                    if (userRecJson != null) {
                        try {
                            ObjectMapper mapper = new ObjectMapper();
                            JsonNode root = mapper.readTree(userRecJson);
                            JsonNode result = root.get("result");

                            if (result != null && result.isArray() && result.size() > 0) {
                                JsonNode rec = result.get(0);
                                if (rec.has("userVarLen101")) {
                                    pkg.setUserVarLen101(rec.get("userVarLen101").asText());
                                }
                                if (rec.has("userVarLen102")) {
                                    pkg.setUserVarLen102(rec.get("userVarLen102").asText());
                                }
                            }
                        } catch (Exception e) {
                        }
                    }
                }
            } else {
                request.setAttribute(Constants.ATTR_ERROR, "Error: " + RestApiService.getLastError());
            }
        } else {
            request.setAttribute(Constants.ATTR_ERROR, "Session expired. Please login again.");
        }

        request.setAttribute(Constants.ATTR_PACKAGES, packages);
        request.setAttribute("packageService", new PackageService());
        request.getRequestDispatcher("/packages.jsp").forward(request, response);
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
                    pkg.setPackageId(getStringValue(node, "package"));
                    pkg.setPackageTitle(getStringValue(node, "packageTitle"));
                    pkg.setPackageStatus(getStringValue(node, "packageStatus"));
                    pkg.setPackageType(getStringValue(node, "packageType"));
                    pkg.setPackageLevel(getStringValue(node, "packageLevel"));
                    pkg.setCreator(getStringValue(node, "creator"));
                    pkg.setWorkChangeRequest(getStringValue(node, "workChangeRequest"));
                    pkg.setDateCreated(getStringValue(node, "dateCreated"));
                    pkg.setDateInstalled(getStringValue(node, "dateInstalled"));
                    pkg.setRequestorDept(getStringValue(node, "requestorDept"));
                    pkg.setRequestorPhone(getStringValue(node, "requestorPhone"));
                    pkg.setRequestorName(getStringValue(node, "requestorName"));
                    pkg.setAuditReturnCode(getStringValue(node, "auditReturnCode"));
                    packages.add(pkg);
                }
            }
        } catch (Exception e) {
            System.err.println("Error parsing JSON: " + e.getMessage());
        }

        return packages;
    }
    
    private String getStringValue(JsonNode node, String fieldName) {
        if (node.has(fieldName) && !node.get(fieldName).isNull()) {
            return node.get(fieldName).asText();
        }
        return "";
    }
}