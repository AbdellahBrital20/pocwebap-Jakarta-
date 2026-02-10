<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - Euroclear POC</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <script src="${pageContext.request.contextPath}/js/theme.js"></script>
</head>
<body>
    <!-- Dark Mode Toggle Button -->
    <button id="themeToggle" class="theme-toggle" onclick="toggleTheme()">Dark Mode</button>

    <div class="container">

        <!-- Header -->
        <div class="header">
            <h1>EUROCLEAR</h1>
            <p class="subtitle">Package Management System</p>
        </div>

        <!-- Login Form -->
        <div class="login-container">
            <div class="card login-card">
                <h2>Sign In</h2>

                <!-- Error message if login fails -->
                <% if (request.getAttribute("error") != null) { %>
                    <div class="error-message">
                        <%= request.getAttribute("error") %>
                    </div>
                <% } %>

                <form action="${pageContext.request.contextPath}/login" method="post">
                    <div class="form-group">
                        <label for="username">User ID</label>
                        <input type="text" id="username" name="username"
                               placeholder="Enter your User ID" required autofocus>
                    </div>

                    <div class="form-group">
                        <label for="password">Password</label>
                        <input type="password" id="password" name="password"
                               placeholder="Enter your Password" required>
                    </div>

                    <button type="submit" class="btn btn-primary">Login</button>
                </form>

            </div>
        </div>

        <!-- Footer -->
        <div class="footer">
            <p>POC Web Application</p>
        </div>

    </div>
</body>
</html>