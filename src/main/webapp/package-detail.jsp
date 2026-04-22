<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.euroclear.pocwebap.model.Component" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Package Detail - ${packageId}</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="container">
        <div class="header">
            <h1>Components for Package: ${packageId}</h1>
            <a href="${pageContext.request.contextPath}/packages" class="back-btn">Back to Packages</a>
            <a href="${pageContext.request.contextPath}/logout" class="logout-btn">Logout</a>
        </div>
        
        <% if (request.getAttribute("error") != null) { %>
            <div class="error-message"><%= request.getAttribute("error") %></div>
        <% } %>
        
        <% 
            List<Component> components = (List<Component>) request.getAttribute("components");
            if (components != null && !components.isEmpty()) { 
        %>
        <table class="data-table">
            <thead>
                <tr>
                    <th>Name</th>
                    <th>Type</th>
                    <th>LTP</th>
                    <th>Status</th>
                    <th>Date</th>
                    <th>Time</th>
                    <th>User</th>
                    <th>Procname</th>
                    <th>B31</th>
                    <th>B64</th>
                    <th>O31</th>
                    <th>Acc</th>
                </tr>
            </thead>
            <tbody>
                <% for (Component comp : components) { %>
                <tr>
                    <td><%= comp.getName() != null && !comp.getName().isEmpty() ? comp.getName() : "---" %></td>
                    <td><%= comp.getComponentType() != null && !comp.getComponentType().isEmpty() ? comp.getComponentType() : "---" %></td>
                    <td><%= comp.getLtp() %></td>
                    <td><%= comp.getStatus() %></td>
                    <td><%= comp.getDateLastModified() != null && !comp.getDateLastModified().isEmpty() ? comp.getDateLastModified() : "---" %></td>
                    <td><%= comp.getTimeLastModified() != null && !comp.getTimeLastModified().isEmpty() ? comp.getTimeLastModified() : "---" %></td>
                    <td><%= comp.getUser() != null && !comp.getUser().isEmpty() ? comp.getUser() : "---" %></td>
                    <td><%= comp.getProcname() != null && !comp.getProcname().isEmpty() ? comp.getProcname() : "---" %></td>
                    <td><%= comp.getB31() %></td>
                    <td><%= comp.getB64() %></td>
                    <td><%= comp.getO31() %></td>
                    <td><%= comp.getAcc() %></td>
                </tr>
                <% } %>
            </tbody>
        </table>
        <% } else { %>
            <p class="no-data">No components found for this package.</p>
        <% } %>
    </div>
</body>
</html>