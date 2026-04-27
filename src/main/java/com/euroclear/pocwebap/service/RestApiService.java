package com.euroclear.pocwebap.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import com.euroclear.pocwebap.config.AppConfig;

/**
 * Service for REST API calls to mainframe.
 * Uses HTTP Basic Authentication with JSESSIONID cookie.
 */
public class RestApiService {
    private static String lastError = "";

public static String getLastError() {
    return lastError;
}

    /**
     * Authenticate user with RACF credentials.
     * Returns JSESSIONID cookie if successful, null if failed.
     */
    public static String login(String username, String password) {
        try {
            String baseUrl = AppConfig.getApiBaseUrl();
            URL url = new URL(baseUrl + "logon");
            
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(AppConfig.getApiTimeout());
            conn.setReadTimeout(AppConfig.getApiTimeout());
            conn.setRequestProperty("Accept", "application/json");
            
            // Basic Authentication header
            String auth = username + ":" + password;
            String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
            conn.setRequestProperty("Authorization", "Basic " + encodedAuth);
            
            int responseCode = conn.getResponseCode();
            
            if (responseCode == 200) {
                // Extract JSESSIONID from Set-Cookie header
                String sessionId = extractSessionId(conn);
                conn.disconnect();
                return sessionId;
            } else {
                conn.disconnect();
                return null;
            }
            
        } catch (Exception e) {
            System.err.println("Login error: " + e.getMessage());
            return null;
        }
    }

    /**
     * Logout and cleanup session.
     */
    public static boolean logout(String sessionId) {
        try {
            String baseUrl = AppConfig.getApiBaseUrl();
            URL url = new URL(baseUrl + "logoff");
            
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(AppConfig.getApiTimeout());
            conn.setReadTimeout(AppConfig.getApiTimeout());
            
            // Send session cookie
            conn.setRequestProperty("Cookie", "JSESSIONID=" + sessionId);
            
            int responseCode = conn.getResponseCode();
            conn.disconnect();
            
            return responseCode == 200;
            
        } catch (Exception e) {
            System.err.println("Logout error: " + e.getMessage());
            return false;
        }
    }

    /**
     * Call API with session cookie.
     * Returns response body as String.
     */
    public static String callApi(String endpoint, String method, String jsonBody, String sessionId) {
        try {
            String baseUrl = AppConfig.getApiBaseUrl();
            URL url = new URL(baseUrl + endpoint);
            
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(method);
            conn.setConnectTimeout(AppConfig.getApiTimeout());
            conn.setReadTimeout(AppConfig.getApiTimeout());
            conn.setRequestProperty("Content-Type", "application/json");
            
            // Send session cookie
            conn.setRequestProperty("Cookie", "JSESSIONID=" + sessionId);
            
            // Send body if POST
            if ("POST".equals(method) && jsonBody != null) {
                conn.setDoOutput(true);
                try (OutputStream os = conn.getOutputStream()) {
                    os.write(jsonBody.getBytes("UTF-8"));
                }
            }
            
            int responseCode = conn.getResponseCode();
            
            if (responseCode == 200) {
                BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                conn.disconnect();
                return response.toString();
            } else {
                conn.disconnect();
                return null;
            }
            
        } catch (Exception e) {
            System.err.println("API call error: " + e.getMessage());
            return null;
        }
    }

    /**
     * Extract JSESSIONID from Set-Cookie header.
     */
    private static String extractSessionId(HttpURLConnection conn) {
        Map<String, List<String>> headers = conn.getHeaderFields();
        List<String> cookies = headers.get("Set-Cookie");
        
        if (cookies != null) {
            for (String cookie : cookies) {
                if (cookie.startsWith("JSESSIONID=")) {
                    // Extract value before semicolon
                    String sessionId = cookie.split(";")[0];
                    return sessionId.replace("JSESSIONID=", "");
                }
            }
        }
        return null;
    }

    /**
 * Search packages
 */
public static String searchPackages(String packageName, String sessionId, 
        String[] statuses, String creator, String workRequest, String action,
        String material, String installFrom, String installTo, 
        String createFrom, String createTo) {
    
    lastError = "";
    try {
        String baseUrl = AppConfig.getApiBaseUrl();
        StringBuilder urlBuilder = new StringBuilder(baseUrl + "package/search?");
        
        if (packageName != null && !packageName.isEmpty()) {
            urlBuilder.append("package=").append(java.net.URLEncoder.encode(packageName, "UTF-8"));
        } else {
            urlBuilder.append("package=*");
        }
        
        if (statuses != null && statuses.length > 0) {
            for (String status : statuses) {
                String param = getStatusParam(status);
                if (param != null) {
                    urlBuilder.append("&").append(param).append("=Y");
                }
            }
        }
        
        if (creator != null && !creator.isEmpty()) {
            urlBuilder.append("&creator=").append(java.net.URLEncoder.encode(creator, "UTF-8"));
        }
        
        if (workRequest != null && !workRequest.isEmpty()) {
            urlBuilder.append("&formNumber=").append(java.net.URLEncoder.encode(workRequest, "UTF-8"));
        }
        
        if (action != null && !action.isEmpty()) {
            if (action.equals("EMPTY")) {
                urlBuilder.append("&requestorDept=---");
            } else {
                urlBuilder.append("&requestorDept=").append(java.net.URLEncoder.encode(action, "UTF-8"));
            }
        }
        
        if (material != null && !material.isEmpty()) {
            urlBuilder.append("&requestorPhone=").append(java.net.URLEncoder.encode(material, "UTF-8"));
        }
        
        if (installFrom != null && !installFrom.isEmpty()) {
            urlBuilder.append("&searchFromDateInstalled=").append(formatDate(installFrom));
        }
        if (installTo != null && !installTo.isEmpty()) {
            urlBuilder.append("&searchToDateInstalled=").append(formatDate(installTo));
        }
        
        if (createFrom != null && !createFrom.isEmpty()) {
            urlBuilder.append("&searchFromDateCreated=").append(formatDate(createFrom));
        }
        if (createTo != null && !createTo.isEmpty()) {
            urlBuilder.append("&searchToDateCreated=").append(formatDate(createTo));
        }
        
        URL url = new URL(urlBuilder.toString());
        
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(AppConfig.getApiTimeout());
        conn.setReadTimeout(AppConfig.getApiTimeout());
        conn.setRequestProperty("Cookie", "JSESSIONID=" + sessionId);
        conn.setRequestProperty("Accept", "application/json");
        
        int responseCode = conn.getResponseCode();
        
        if (responseCode == 200) {
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            conn.disconnect();
            return response.toString();
        } else {
            lastError = "HTTP " + responseCode + " from " + url;
            conn.disconnect();
            return null;
        }
        
    } catch (Exception e) {
        lastError = "Exception: " + e.getMessage();
        return null;
    }
}

public static String searchPackages(String packageName, String sessionId) {
    return searchPackages(packageName, sessionId, null, null, null, null, null, null, null, null, null);
}

// === GET PACKAGE USER RECORDS (for C and T) ===

public static String getPackageUserRecords(String packageId, String sessionId) {
    try {
        String baseUrl = AppConfig.getApiBaseUrl();
        String encodedId = java.net.URLEncoder.encode(packageId, "UTF-8");
        URL url = new URL(baseUrl + "package/userrecords?package=" + encodedId);
        
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(AppConfig.getApiTimeout());
        conn.setReadTimeout(AppConfig.getApiTimeout());
        conn.setRequestProperty("Cookie", "JSESSIONID=" + sessionId);
        conn.setRequestProperty("Accept", "application/json");
        
        if (conn.getResponseCode() == 200) {
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            conn.disconnect();
            return response.toString();
        }
        conn.disconnect();
        return null;
        
    } catch (Exception e) {
        return null;
    }
}

// === HELPER: Map status text to API parameter ===

private static String getStatusParam(String status) {
    switch (status) {
        case "DEV": return "searchForDevelopmentStatus";
        case "FRZ": return "searchForFrozenStatus";
        case "APR": return "searchForApprovedStatus";
        case "REJ": return "searchForRejectedStatus";
        case "DIS": return "searchForDeliveredStatus";
        case "INS": return "searchForInstalledStatus";
        case "BAS": return "searchForBaselineStatus";
        case "BAK": return "searchForBackedOutStatus";
        case "DEL": return "searchForDeletedStatus";
        case "OPN": return "searchForOpenedStatus";
        case "CLO": return "searchForClosedStatus";
        case "TCC": return "searchForTempChangeCycledStatus";
        default: return null;
    }
}

// === HELPER: Format date YYYY-MM-DD to YYYYMMDD ===

private static String formatDate(String date) {
    if (date == null || date.isEmpty()) return "";
    return date.replace("-", "");
}

/**
 * Get components of a package
 */
public static String getComponents(String packageId, String sessionId) {
    lastError = "";
    try {
        String baseUrl = AppConfig.getApiBaseUrl();
        String encodedId = java.net.URLEncoder.encode(packageId, "UTF-8");
        URL url = new URL(baseUrl + "component?package=" + encodedId + "&longFormat=Y");
        
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(AppConfig.getApiTimeout());
        conn.setReadTimeout(AppConfig.getApiTimeout());
        conn.setRequestProperty("Cookie", "JSESSIONID=" + sessionId);
        conn.setRequestProperty("Accept", "application/json");
        
        int responseCode = conn.getResponseCode();
        
        if (responseCode == 200) {
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            conn.disconnect();
            return response.toString();
        } else {
            lastError = "HTTP " + responseCode + " from " + url;
            conn.disconnect();
            return null;
        }
        
    } catch (Exception e) {
        lastError = "Exception: " + e.getMessage();
        return null;
    }
}

}