package com.euroclear.pocwebap.util;

/**
 * Application constants.
 */
public class Constants {

    public static final String SESSION_API_ID = "API_SESSION_ID";
    // Session attributes
    public static final String SESSION_USER = "user";
    public static final String SESSION_RACF_ID = "racfId";

    // Request attributes
    public static final String ATTR_ERROR = "error";
    public static final String ATTR_PACKAGES = "packages";
    public static final String ATTR_COMPONENTS = "components";

    // API Endpoints
    public static final String API_VERSION = "/changeman/version";
    public static final String API_PACKAGE_SEARCH = "/package/search";
    public static final String API_PACKAGE_GET = "/package/get";
    public static final String API_PACKAGE_COMPONENTS = "/package/getComponents";

    private Constants() {} // Prevent instantiation
}