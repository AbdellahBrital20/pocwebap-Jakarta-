<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.euroclear.pocwebap.model.Package" %>
<%@ page import="com.euroclear.pocwebap.service.PackageService" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Package List - Euroclear POC</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <script src="${pageContext.request.contextPath}/js/theme.js"></script>
</head>
<body>
    <button id="themeToggle" class="theme-toggle" onclick="toggleTheme()">Dark Mode</button>
    <a href="${pageContext.request.contextPath}/logout" class="logout-btn">Logout</a>

    <div class="container">
    
        <div class="header">
            <h1>EUROCLEAR</h1>
            <p class="subtitle">Change Package List</p>
        </div>
        
        <div class="card">
            <%
                List<Package> packages = (List<Package>) request.getAttribute("packages");
                PackageService packageService = (PackageService) request.getAttribute("packageService");
                int count = (packages != null) ? packages.size() : 0;
            %>
            
            <div class="results-info">
                <span id="rowInfo">Row 1 to 20 of <%= count %></span>
                <a href="${pageContext.request.contextPath}/criteria" class="btn btn-secondary">Back to Search</a>
            </div>
            
            <% if (request.getAttribute("error") != null) { %>
                <div class="error-message">
                    <%= request.getAttribute("error") %>
                </div>
            <% } %>
            
            <% if (packages != null && packages.size() > 0) { %>
                <div class="table-container">
                    <table class="data-table" id="packagesTable">
                        <thead>
                            <tr>
                                <th onclick="sortTable(0)" class="sortable">Package ▲▼</th>
                                <th onclick="sortTable(1)" class="sortable">Sta ▲▼</th>
                                <th onclick="sortTable(2)" class="sortable">Created ▲▼</th>
                                <th onclick="sortTable(3)" class="sortable">Install ▲▼</th>
                                <th onclick="sortTable(4)" class="sortable">WorkRequest ▲▼</th>
                                <th>Act</th>
                                <th onclick="sortTable(6)" class="sortable">Creator ▲▼</th>
                                <th onclick="sortTable(7)" class="sortable">Dept ▲▼</th>
                                <th>T</th>
                                <th onclick="sortTable(9)" class="sortable">ECR/ESD/Other ▲▼</th>
                            </tr>
                        </thead>
                        <tbody id="tableBody">
                            <% for (Package pkg : packages) { %>
                                <tr class="data-row">
                                    <td>
                                        <a href="${pageContext.request.contextPath}/package-detail?id=<%= pkg.getPackage() %>" class="package-link">
                                            <%= pkg.getPackage() %>
                                        </a>
                                    </td>
                                    <td><%= packageService.getStatusShortName(pkg.getPackageStatus()) %></td>
                                    <td><%= pkg.getDateCreated() != null ? pkg.getDateCreated() : "---" %></td>
                                    <td><%= pkg.getDateInstalled() != null ? pkg.getDateInstalled() : "---" %></td>
                                    <td><%= pkg.getWorkChangeRequest() != null ? pkg.getWorkChangeRequest() : "---" %></td>
                                    <td>---</td>
                                    <td><%= pkg.getCreator() != null ? pkg.getCreator() : "---" %></td>
                                    <td><%= pkg.getRequestorDept() != null ? pkg.getRequestorDept() : "---" %></td>
                                    <td><%= packageService.getTypeShortName(pkg.getPackageType()) %></td>
                                    <td><%= pkg.getPackageTitle() != null ? pkg.getPackageTitle() : "---" %></td>
                                </tr>
                            <% } %>
                        </tbody>
                    </table>
                    
                    <div class="pagination">
                        <button onclick="prevPage()" id="prevBtn" class="btn btn-secondary">Previous</button>
                        <span id="pageInfo">Page 1 of 1</span>
                        <button onclick="nextPage()" id="nextBtn" class="btn btn-secondary">Next</button>
                    </div>
                </div>
            <% } else { %>
                <div class="error-message">
                    No packages found matching your criteria.
                </div>
            <% } %>
        </div>
        
        <div class="footer">
            <p>POC Web Application | <a href="${pageContext.request.contextPath}/login">Logout</a></p>
        </div>
        
    </div>
    
    <script>
        var currentPage = 1;
        var rowsPerPage = 20;
        var rows = [];
        var sortDirection = {};
        
        window.onload = function() {
            rows = Array.from(document.querySelectorAll('.data-row'));
            showPage(1);
        };
        
        function showPage(page) {
            var totalRows = rows.length;
            var totalPages = Math.ceil(totalRows / rowsPerPage);
            
            if (totalPages === 0) totalPages = 1;
            if (page < 1) page = 1;
            if (page > totalPages) page = totalPages;
            
            currentPage = page;
            
            var start = (page - 1) * rowsPerPage;
            var end = start + rowsPerPage;
            
            rows.forEach(function(row, index) {
                if (index >= start && index < end) {
                    row.style.display = '';
                } else {
                    row.style.display = 'none';
                }
            });
            
            document.getElementById('pageInfo').textContent = 'Page ' + page + ' of ' + totalPages;
            document.getElementById('rowInfo').innerHTML = 'Row ' + (start + 1) + ' to ' + Math.min(end, totalRows) + ' of ' + totalRows;
            
            document.getElementById('prevBtn').disabled = (page === 1);
            document.getElementById('nextBtn').disabled = (page === totalPages);
        }
        
        function prevPage() {
            showPage(currentPage - 1);
        }
        
        function nextPage() {
            showPage(currentPage + 1);
        }
        
        function sortTable(columnIndex) {
            var table = document.getElementById('packagesTable');
            var tbody = document.getElementById('tableBody');
            
            if (sortDirection[columnIndex] === 'asc') {
                sortDirection[columnIndex] = 'desc';
            } else {
                sortDirection[columnIndex] = 'asc';
            }
            
            rows.sort(function(a, b) {
                var aValue = a.cells[columnIndex].textContent.trim();
                var bValue = b.cells[columnIndex].textContent.trim();
                
                if (sortDirection[columnIndex] === 'asc') {
                    return aValue.localeCompare(bValue);
                } else {
                    return bValue.localeCompare(aValue);
                }
            });
            
            rows.forEach(function(row) {
                tbody.appendChild(row);
            });
            
            showPage(1);
        }
    </script>
</body>
</html>

/**
<thead>
    <tr>
        <th onclick="sortTable(0)" class="sortable">Package</th>
        <th onclick="sortTable(1)" class="sortable">Sta</th>
        <th onclick="sortTable(2)" class="sortable">Created</th>
        <th onclick="sortTable(3)" class="sortable">Install</th>
        <th onclick="sortTable(4)" class="sortable">WorkRequest</th>
        <th onclick="sortTable(5)" class="sortable">Act</th>
        <th onclick="sortTable(6)" class="sortable">MTL</th>
        <th>AU</th>
        <th onclick="sortTable(8)" class="sortable">Creator</th>
        <th>C</th>
        <th>T</th>
        <th onclick="sortTable(11)" class="sortable">ECR/ESD/Other</th>
    </tr>
</thead> **/

<tbody id="tableBody">
<% for (Package pkg : packages) { %>
    <tr class="data-row">
        <!-- Package -->
        <td>
            <a href="${pageContext.request.contextPath}/package-detail?id=<%= pkg.getPackageId() %>" class="package-link">
                <%= pkg.getPackageId() %>
            </a>
        </td>
        
        <!-- Sta -->
        <td>
            <%
            String rawSta = pkg.getPackageStatus();
            String sta = "---";
            if (rawSta != null && !rawSta.isEmpty()) {
                switch (rawSta) {
                    case "1": sta = "APR"; break;
                    case "2": sta = "BAK"; break;
                    case "3": sta = "BAS"; break;
                    case "4": sta = "CLS"; break;
                    case "5": sta = "DEL"; break;
                    case "6": sta = "DEV"; break;
                    case "7": sta = "DIS"; break;
                    case "8": sta = "FRZ"; break;
                    case "9": sta = "INS"; break;
                    case "A": sta = "OPN"; break;
                    case "B": sta = "REJ"; break;
                    case "C": sta = "TCC"; break;
                    case "E": sta = "AIP"; break;
                    case "F": sta = "XDS"; break;
                    case "G": sta = "XIN"; break;
                    case "H": sta = "XBK"; break;
                    case "I": sta = "XRV"; break;
                    case "J": sta = "IIP"; break;
                    default: sta = rawSta; break;
                }
            }
            %>
            <%= sta %>
        </td>
        
        <!-- Created -->
        <td><%= pkg.getDateCreated() != null ? pkg.getDateCreated() : "---" %></td>
        
        <!-- Install -->
        <td><%= pkg.getDateInstalled() != null ? pkg.getDateInstalled() : "---" %></td>
        
        <!-- WorkRequest -->
        <td><%= pkg.getWorkChangeRequest() != null ? pkg.getWorkChangeRequest() : "---" %></td>
        
        <!-- Act -->
        <td><%= pkg.getRequestorDept() != null && !pkg.getRequestorDept().isEmpty() ? pkg.getRequestorDept() : "---" %></td>
        
        <!-- MTL -->
        <td><%= pkg.getRequestorPhone() != null && !pkg.getRequestorPhone().isEmpty() ? pkg.getRequestorPhone() : "---" %></td>
        
        <!-- AU -->
        <td><%= pkg.getAuditRC() %></td>
        
        <!-- Creator -->
        <td><%= pkg.getCreator() != null ? pkg.getCreator() : "---" %></td>
        
        <!-- C -->
        <td><%= pkg.getTgtRlsPrd() %></td>
        
        <!-- T -->
        <td><%= pkg.getRefConc() %></td>
        
        <!-- ECR/ESD/Other -->
        <td><%= pkg.getPackageTitle() != null ? pkg.getPackageTitle() : "---" %></td>
    </tr>
<% } %>
</tbody>