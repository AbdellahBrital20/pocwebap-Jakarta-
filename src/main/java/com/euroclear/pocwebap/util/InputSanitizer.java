package com.euroclear.pocwebap.util;

/**
 * OWASP Security - Input sanitization utilities.
 * Prevents XSS and injection attacks.
 */
public class InputSanitizer {

    /**
     * Sanitize string input - removes dangerous characters
     */
    public static String sanitize(String input) {
        if (input == null) return null;
        
        return input
            .replaceAll("<", "&lt;")
            .replaceAll(">", "&gt;")
            .replaceAll("\"", "&quot;")
            .replaceAll("'", "&#x27;")
            .replaceAll("&", "&amp;")
            .trim();
    }

    /**
     * Validate username - only alphanumeric and underscore
     */
    public static boolean isValidUsername(String username) {
        if (username == null || username.isEmpty()) return false;
        return username.matches("^[a-zA-Z0-9_]{3,20}$");
    }

    /**
     * Validate package ID format
     */
    public static boolean isValidPackageId(String packageId) {
        if (packageId == null || packageId.isEmpty()) return false;
        return packageId.matches("^[A-Z0-9]{6,10}$");
    }

    /**
     * Check if input is not empty
     */
    public static boolean isNotEmpty(String input) {
        return input != null && !input.trim().isEmpty();
    }
}