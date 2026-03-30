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
public static String searchPackages(String packageFilter, String sessionId) {
    lastError = "";
    try {
        String baseUrl = AppConfig.getApiBaseUrl();
        String encodedFilter = java.net.URLEncoder.encode(packageFilter, "UTF-8");
        URL url = new URL(baseUrl + "package/search?package=" + encodedFilter);
        
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