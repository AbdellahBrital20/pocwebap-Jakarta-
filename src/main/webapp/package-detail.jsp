<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.euroclear.pocwebap.model.Component" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Package Detail - ${packageId}</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <script src="${pageContext.request.contextPath}/js/theme.js"></script>
</head>
<body>
    <button id="themeToggle" class="theme-toggle" onclick="toggleTheme()">Dark Mode</button>
    <a href="${pageContext.request.contextPath}/logout" class="logout-btn">Logout</a>

    <div class="container">
        <div class="header">
            <h1>EUROCLEAR</h1>
            <p class="subtitle">Components for Package: ${packageId}</p>
        </div>

        <div class="card">
            <%
                List<Component> components = (List<Component>) request.getAttribute("components");
                int count = (components != null) ? components.size() : 0;
            %>

            <div class="results-info">
                <a href="${pageContext.request.contextPath}/packages" class="btn btn-secondary">Back to Packages</a>
                <div class="pagination-top">
                    <button onclick="prevPage()" id="prevBtnTop" class="btn btn-secondary">Previous</button>
                    <span id="pageInfoTop">Page 1 of 1</span>
                    <button onclick="nextPage()" id="nextBtnTop" class="btn btn-secondary">Next</button>
                </div>
                <span id="rowInfo">Row 1 to 20 of <%= count %></span>
            </div>

            <% if (request.getAttribute("error") != null) { %>
                <div class="error-message"><%= request.getAttribute("error") %></div>
            <% } %>

            <% if (components != null && !components.isEmpty()) { %>
            <div class="table-container">
                <table class="data-table" id="componentsTable">
                    <thead>
                        <tr>
                            <th onclick="sortTable(0)" class="sortable">Name <span class="sort-arrow">▲▼</span></th>
                            <th>Type</th>
                            <th>Status</th>
                            <th onclick="sortTable(3)" class="sortable">Date <span class="sort-arrow">▲▼</span></th>
                            <th>Time</th>
                            <th>User</th>
                            <th>Procname</th>
                            <th>B31</th>
                            <th>B64</th>
                            <th>O31</th>
                            <th>Acc</th>
                        </tr>
                    </thead>
                    <tbody id="tableBody">
                        <% for (Component comp : components) { %>
                        <tr class="data-row">
                            <td><%= comp.getName() != null && !comp.getName().isEmpty() ? comp.getName() : "---" %></td>
                            <td><%= comp.getComponentType() != null && !comp.getComponentType().isEmpty() ? comp.getComponentType() : "---" %></td>
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
            </div>

            <div class="pagination">
                <button onclick="prevPage()" id="prevBtn" class="btn btn-secondary">Previous</button>
                <span id="pageInfo">Page 1 of 1</span>
                <button onclick="nextPage()" id="nextBtn" class="btn btn-secondary">Next</button>
            </div>

            <% } else { %>
                <p class="no-data">No components found for this package.</p>
            <% } %>
        </div>

        <div class="footer">
            <p>POC Web Application | <a href="${pageContext.request.contextPath}/logout">Logout</a></p>
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
                row.style.display = (index >= start && index < end) ? '' : 'none';
            });

            document.getElementById('pageInfo').textContent = 'Page ' + page + ' of ' + totalPages;
            document.getElementById('pageInfoTop').textContent = 'Page ' + page + ' of ' + totalPages;
            document.getElementById('rowInfo').innerHTML = 'Row ' + (start + 1) + ' to ' + Math.min(end, totalRows) + ' of ' + totalRows;
            document.getElementById('prevBtn').disabled = (page === 1);
            document.getElementById('nextBtn').disabled = (page === totalPages);
            document.getElementById('prevBtnTop').disabled = (page === 1);
            document.getElementById('nextBtnTop').disabled = (page === totalPages);
        }

        function prevPage() { showPage(currentPage - 1); }
        function nextPage() { showPage(currentPage + 1); }

        function sortTable(columnIndex) {
            var tbody = document.getElementById('tableBody');
            sortDirection[columnIndex] = sortDirection[columnIndex] === 'asc' ? 'desc' : 'asc';
            rows.sort(function(a, b) {
                var aValue = a.cells[columnIndex].textContent.trim();
                var bValue = b.cells[columnIndex].textContent.trim();
                return sortDirection[columnIndex] === 'asc' ? aValue.localeCompare(bValue) : bValue.localeCompare(aValue);
            });
            rows.forEach(function(row) { tbody.appendChild(row); });
            showPage(1);
        }
    </script>
</body>
</html>