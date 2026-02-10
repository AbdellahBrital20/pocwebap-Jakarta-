<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.euroclear.pocwebap.model.Component" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Package Components - Euroclear POC</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <script src="${pageContext.request.contextPath}/js/theme.js"></script>
</head>
<body>
    <button id="themeToggle" class="theme-toggle" onclick="toggleTheme()">Dark Mode</button>

    <div class="container">

        <div class="header">
            <h1>EUROCLEAR</h1>
            <p class="subtitle">STAGE: <%= request.getAttribute("packageId") %> Components</p>
        </div>

        <div class="card">
            <%
                List<Component> components = (List<Component>) request.getAttribute("components");
                int count = (components != null) ? components.size() : 0;
            %>
            
            <div class="results-info">
                <span>Row 1 to <%= count %> of <%= count %></span>
                <a href="javascript:history.back()" class="btn btn-secondary">Back to Package List</a>
            </div>

            <% if (request.getAttribute("error") != null) { %>
                <div class="error-message">
                    <%= request.getAttribute("error") %>
                </div>
            <% } %>

            <% if (components != null && components.size() > 0) { %>
                <div class="table-container">
                    <table class="data-table">
                        <thead>
                            <tr>
                                <th>Name</th>
                                <th>Ltp</th>
                                <th>Status</th>
                                <th>Changed</th>
                                <th>Time</th>
                                <th>User</th>
                                <th>Procname</th>
                                <th>b31</th>
                                <th>b64</th>
                                <th>o31</th>
                                <th>acc</th>
                            </tr>
                        </thead>
                        <tbody>
                            <% for (Component comp : components) { 
                                String status = comp.getComponentStatus();
                                if (status != null && status.length() > 4) {
                                    status = status.substring(4);
                                }
                            %>
                                <tr>
                                    <td><%= comp.getComponent() %></td>
                                    <td><%= comp.getComponentType() %></td>
                                    <td><%= status %></td>
                                    <td><%= comp.getDateLastModified() %></td>
                                    <td><%= comp.getTimeLastModified() %></td>
                                    <td><%= comp.getUpdater() %></td>
                                    <td><%= comp.getBuildProc() %></td>
                                    <td><%= comp.getUserOption07() != null ? comp.getUserOption07() : "---" %></td>
                                    <td><%= comp.getUserOption08() != null ? comp.getUserOption08() : "---" %></td>
                                    <td><%= comp.getUserOption03() != null ? comp.getUserOption03() : "---" %></td>
                                    <td><%= comp.getUserOption05() != null ? comp.getUserOption05() : "---" %></td>
                                </tr>
                            <% } %>
                        </tbody>
                    </table>
                </div>
            <% } else { %>
                <div class="error-message">
                    No components found for this package.
                </div>
            <% } %>
        </div>

        <div class="footer">
            <p>POC Web Application | <a href="${pageContext.request.contextPath}/login">Logout</a></p>
        </div>

    </div>
</body>
</html>