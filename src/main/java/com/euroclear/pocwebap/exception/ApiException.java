package com.euroclear.pocwebap.exception;

/**
 * Custom exception for API errors.
 * Provides clean error handling throughout the application.
 */
public class ApiException extends Exception {

    private final int errorCode;
    private final String userMessage;

    public ApiException(String message) {
        super(message);
        this.errorCode = 500;
        this.userMessage = "An error occurred. Please try again.";
    }

    public ApiException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
        this.userMessage = message;
    }

    public ApiException(String message, int errorCode, String userMessage) {
        super(message);
        this.errorCode = errorCode;
        this.userMessage = userMessage;
    }

    public ApiException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = 500;
        this.userMessage = "An error occurred. Please try again.";
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getUserMessage() {
        return userMessage;
    }

    // Common error factory methods
    public static ApiException unauthorized() {
        return new ApiException("Unauthorized", 401, "Invalid credentials.");
    }

    public static ApiException notFound(String resource) {
        return new ApiException(resource + " not found", 404, resource + " not found.");
    }

    public static ApiException serverError() {
        return new ApiException("Internal server error", 500, "Server error. Please try again later.");
    }

    public static ApiException timeout() {
        return new ApiException("Request timeout", 408, "Request timed out. Please try again.");
    }
}