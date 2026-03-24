package com.euroclear.pocwebap.filter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * OWASP Security - Rate limiting filter.
 * Limits requests per IP to prevent brute force attacks.
 */

public class RateLimitFilter implements Filter {

    private static final int MAX_REQUESTS_PER_MINUTE = 60;
    private static final long TIME_WINDOW_MS = 60000; // 1 minute
    
    private Map<String, RateLimitInfo> requestCounts = new ConcurrentHashMap<>();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String clientIP = getClientIP(httpRequest);
        
        if (isRateLimited(clientIP)) {
            httpResponse.setStatus(429); // Too Many Requests
            httpResponse.getWriter().write("Too many requests. Please wait and try again.");
            return;
        }

        chain.doFilter(request, response);
    }

    private boolean isRateLimited(String clientIP) {
        long now = System.currentTimeMillis();
        
        RateLimitInfo info = requestCounts.get(clientIP);
        
        if (info == null || now - info.windowStart > TIME_WINDOW_MS) {
            // New window
            requestCounts.put(clientIP, new RateLimitInfo(now, 1));
            return false;
        }
        
        if (info.count >= MAX_REQUESTS_PER_MINUTE) {
            return true;
        }
        
        info.count++;
        return false;
    }

    private String getClientIP(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty()) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    private static class RateLimitInfo {
        long windowStart;
        int count;

        RateLimitInfo(long windowStart, int count) {
            this.windowStart = windowStart;
            this.count = count;
        }
    }

    @Override
    public void init(FilterConfig filterConfig) {}

    @Override
    public void destroy() {
        requestCounts.clear();
    }
}