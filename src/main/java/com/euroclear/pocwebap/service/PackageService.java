package com.euroclear.pocwebap.service;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.euroclear.pocwebap.model.Component;
import com.euroclear.pocwebap.model.Package;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PackageService {
    
    private ObjectMapper objectMapper = new ObjectMapper();
    
    // Configuration API
    private static final boolean USE_API = false;
    private static final String API_URL_PACKAGES = "API_URL_HERE_FOR_PACKAGES";
    private static final String API_URL_COMPONENTS = "API_URL_HERE_FOR_COMPONENTS";
    
    public String getStatusShortName(String statusFull) {
        if (statusFull == null) return "---";
        if (statusFull.contains("Development")) return "DEV";
        if (statusFull.contains("Frozen")) return "FRZ";
        if (statusFull.contains("Approved")) return "APR";
        if (statusFull.contains("Rejected")) return "REJ";
        if (statusFull.contains("Distributed")) return "DIS";
        if (statusFull.contains("Installed")) return "INS";
        if (statusFull.contains("Baselined")) return "BAS";
        if (statusFull.contains("Backed")) return "BAK";
        if (statusFull.contains("Deleted")) return "DEL";
        if (statusFull.contains("Open")) return "OPN";
        if (statusFull.contains("Closed")) return "CLO";
        if (statusFull.contains("Temporary")) return "TCC";
        return "---";
    }
    
    public String getLevelShortName(String levelFull) {
        if (levelFull == null) return "---";
        if (levelFull.contains("Simple")) return "SIMPLE";
        if (levelFull.contains("Super")) return "SUPER";
        if (levelFull.contains("Complex")) return "COMPLEX";
        if (levelFull.contains("Participating")) return "PARTICIPATING";
        return "---";
    }
    
    public String getTypeShortName(String typeFull) {
        if (typeFull == null) return "-";
        if (typeFull.contains("Planned permanent")) return "PP";
        if (typeFull.contains("Planned temporary")) return "PT";
        if (typeFull.contains("Unplanned permanent")) return "UP";
        if (typeFull.contains("Unplanned temporary")) return "UT";
        return "-";
    }
    
    public String convertDateFormat(String htmlDate) {
        if (htmlDate == null || htmlDate.isEmpty()) return null;
        return htmlDate.replace("-", "");
    }
    
    private JsonNode getDataFromSource(String jsonFilePath, String apiUrl) {
        try {
            if (USE_API) {
                URL url = new URL(apiUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");
                conn.setRequestProperty("Content-Type", "application/json");
                
                if (conn.getResponseCode() == 200) {
                    return objectMapper.readTree(conn.getInputStream());
                }
                return null;
            } else {
                InputStream inputStream = getClass().getResourceAsStream(jsonFilePath);
                return objectMapper.readTree(inputStream);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public List<Package> searchPackages(String packageName, String[] statuses, 
            String[] levels, String[] types, String creator, String workRequest,
            String action, String dept, String installDateFrom, String installDateTo, 
            String creationDateFrom, String creationDateTo) {
        
        List<Package> packages = new ArrayList<>();
        
        try {
            JsonNode rootNode = getDataFromSource("/json/packages.json", API_URL_PACKAGES);
            if (rootNode == null) return packages;
            
            JsonNode resultArray = rootNode.get("result");
            
            String searchPattern = "";
            if (packageName != null && !packageName.trim().isEmpty()) {
                searchPattern = packageName.replace("*", "").toUpperCase();
            }
            
            String installFrom = convertDateFormat(installDateFrom);
            String installTo = convertDateFormat(installDateTo);
            String creationFrom = convertDateFormat(creationDateFrom);
            String creationTo = convertDateFormat(creationDateTo);
            
            for (JsonNode node : resultArray) {
                
                String pkgName = node.get("package").asText();
                String pkgStatus = node.has("packageStatus") ? node.get("packageStatus").asText() : "";
                String pkgLevel = node.has("packageLevel") ? node.get("packageLevel").asText() : "";
                String pkgType = node.has("packageType") ? node.get("packageType").asText() : "";
                String pkgCreator = node.has("creator") ? node.get("creator").asText() : "";
                String pkgWorkRequest = node.has("workChangeRequest") ? node.get("workChangeRequest").asText() : "";
                String pkgDateInstalled = node.has("dateInstalled") ? node.get("dateInstalled").asText() : "";
                String pkgDateCreated = node.has("dateCreated") ? node.get("dateCreated").asText() : "";
                
                if (!searchPattern.isEmpty()) {
                    if (!pkgName.toUpperCase().startsWith(searchPattern)) continue;
                }
                
                if (statuses != null && statuses.length > 0) {
                    boolean match = false;
                    String shortStatus = getStatusShortName(pkgStatus);
                    for (String s : statuses) {
                        if (s.equalsIgnoreCase(shortStatus)) {
                            match = true;
                            break;
                        }
                    }
                    if (!match) continue;
                }
                
                if (levels != null && levels.length > 0) {
                    boolean match = false;
                    String shortLevel = getLevelShortName(pkgLevel);
                    for (String l : levels) {
                        if (shortLevel.toUpperCase().contains(l.toUpperCase())) {
                            match = true;
                            break;
                        }
                    }
                    if (!match) continue;
                }
                
                if (types != null && types.length > 0) {
                    boolean match = false;
                    String shortType = getTypeShortName(pkgType);
                    for (String t : types) {
                        if (t.equalsIgnoreCase(shortType)) {
                            match = true;
                            break;
                        }
                    }
                    if (!match) continue;
                }
                
                if (creator != null && !creator.trim().isEmpty()) {
                    if (!pkgCreator.equalsIgnoreCase(creator.trim())) continue;
                }
                
                if (workRequest != null && !workRequest.trim().isEmpty()) {
                    if (!pkgWorkRequest.toUpperCase().contains(workRequest.toUpperCase())) continue;
                }
                
                String pkgDept = node.has("requestorDept") ? node.get("requestorDept").asText() : "";
                if (dept != null && !dept.trim().isEmpty()) {
                    if (!pkgDept.toUpperCase().contains(dept.toUpperCase())) continue;
                }
                
                if (installFrom != null && !pkgDateInstalled.isEmpty()) {
                    if (pkgDateInstalled.compareTo(installFrom) < 0) continue;
                }
                if (installTo != null && !pkgDateInstalled.isEmpty()) {
                    if (pkgDateInstalled.compareTo(installTo) > 0) continue;
                }
                if (creationFrom != null && !pkgDateCreated.isEmpty()) {
                    if (pkgDateCreated.compareTo(creationFrom) < 0) continue;
                }
                if (creationTo != null && !pkgDateCreated.isEmpty()) {
                    if (pkgDateCreated.compareTo(creationTo) > 0) continue;
                }
                
                Package pkg = new Package();
                pkg.setPackage(pkgName);
                pkg.setPackageStatus(pkgStatus);
                pkg.setDateCreated(node.has("dateCreated") ? node.get("dateCreated").asText() : "---");
                pkg.setDateInstalled(node.has("dateInstalled") ? node.get("dateInstalled").asText() : "---");
                pkg.setWorkChangeRequest(pkgWorkRequest);
                pkg.setCreator(pkgCreator);
                pkg.setPackageLevel(pkgLevel);
                pkg.setPackageType(pkgType);
                pkg.setPackageTitle(node.has("packageTitle") ? node.get("packageTitle").asText() : "---");
                pkg.setRequestorDept(node.has("requestorDept") ? node.get("requestorDept").asText() : "---");
                packages.add(pkg);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return packages;
    }
    
    public List<Component> getPackageComponents(String packageId) {
        
        List<Component> components = new ArrayList<>();
        
        if (packageId == null || packageId.trim().isEmpty()) return components;
        
        try {
            String apiUrlWithId = API_URL_COMPONENTS.replace("{packageId}", packageId);
            JsonNode rootNode = getDataFromSource("/json/components.json", apiUrlWithId);
            
            if (rootNode == null) return components;
            
            JsonNode resultArray = rootNode.get("result");
            
            for (JsonNode node : resultArray) {
                String pkgName = node.has("package") ? node.get("package").asText() : "";
                
                if (pkgName.equalsIgnoreCase(packageId)) {
                    Component comp = new Component();
                    comp.setComponent(node.has("component") ? node.get("component").asText() : "---");
                    comp.setComponentType(node.has("componentType") ? node.get("componentType").asText() : "---");
                    comp.setComponentStatus(node.has("componentStatus") ? node.get("componentStatus").asText() : "---");
                    comp.setDateLastModified(node.has("dateLastModified") ? node.get("dateLastModified").asText() : "---");
                    comp.setTimeLastModified(node.has("timeLastModified") ? node.get("timeLastModified").asText() : "---");
                    comp.setUpdater(node.has("updater") ? node.get("updater").asText() : "---");
                    comp.setBuildProc(node.has("buildProc") ? node.get("buildProc").asText() : "---");
                    comp.setLanguage(node.has("language") ? node.get("language").asText() : "---");
                    comp.setVersion(node.has("version") ? node.get("version").asInt() : 0);
                    comp.setUserOption03(node.has("userOption03") ? node.get("userOption03").asText() : null);
                    comp.setUserOption05(node.has("userOption05") ? node.get("userOption05").asText() : null);
                    comp.setUserOption07(node.has("userOption07") ? node.get("userOption07").asText() : null);
                    comp.setUserOption08(node.has("userOption08") ? node.get("userOption08").asText() : null);
                    components.add(comp);
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return components;
    }
}