package com.euroclear.pocwebap.config;

import java.io.InputStream;
import java.util.Properties;

/**
 * Application configuration loader.
 * Reads settings from application.properties
 */
public class AppConfig {

    private static final Properties properties = new Properties();
    private static boolean loaded = false;

    static {
        loadProperties();
    }

    private static void loadProperties() {
        if (loaded) return;
        
        try (InputStream input = AppConfig.class.getClassLoader()
                .getResourceAsStream("application.properties")) {
            if (input != null) {
                properties.load(input);
                loaded = true;
            }
        } catch (Exception e) {
            System.err.println("Could not load application.properties: " + e.getMessage());
        }
    }

    public static String getApiBaseUrl() {
        return properties.getProperty("api.base.url", "http://localhost:8080");
    }

    public static int getApiTimeout() {
        return Integer.parseInt(properties.getProperty("api.timeout", "30000"));
    }

    public static boolean isTestMode() {
        return Boolean.parseBoolean(properties.getProperty("app.test.mode", "true"));
    }

    public static int getSessionTimeout() {
        return Integer.parseInt(properties.getProperty("session.timeout", "30"));
    }
}