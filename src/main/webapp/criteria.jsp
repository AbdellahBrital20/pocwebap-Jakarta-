<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Arrays"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Package Search - Euroclear POC</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <script src="${pageContext.request.contextPath}/js/theme.js"></script>
</head>
<body>
    <button id="themeToggle" class="theme-toggle" onclick="toggleTheme()">Dark Mode</button>
    <a href="${pageContext.request.contextPath}/logout" class="logout-btn">Logout</a>

    <div class="container">
        <div class="header">
            <h1>EUROCLEAR</h1>
            <p class="subtitle">Package List Parameters</p>
        </div>

        <%
            String savedPackage = (session.getAttribute("filter_packageName") != null) ? (String) session.getAttribute("filter_packageName") : "";
            String savedCreator = (session.getAttribute("filter_creator") != null) ? (String) session.getAttribute("filter_creator") : "";
            String savedWorkRequest = (session.getAttribute("filter_workRequest") != null) ? (String) session.getAttribute("filter_workRequest") : "";
            String savedAction = (session.getAttribute("filter_action") != null) ? (String) session.getAttribute("filter_action") : "";
            String savedMaterial = (session.getAttribute("filter_material") != null) ? (String) session.getAttribute("filter_material") : "";
            String savedInstallFrom = (session.getAttribute("filter_installFrom") != null) ? (String) session.getAttribute("filter_installFrom") : "";
            String savedInstallTo = (session.getAttribute("filter_installTo") != null) ? (String) session.getAttribute("filter_installTo") : "";
            String savedCreateFrom = (session.getAttribute("filter_createFrom") != null) ? (String) session.getAttribute("filter_createFrom") : "";
            String savedCreateTo = (session.getAttribute("filter_createTo") != null) ? (String) session.getAttribute("filter_createTo") : "";
            String[] savedStatuses = (session.getAttribute("filter_statuses") != null) ? (String[]) session.getAttribute("filter_statuses") : new String[]{};
            List<String> statusList = Arrays.asList(savedStatuses);
        %>

        <div class="criteria-container">
            <div class="card criteria-card">
                <h2>Search Criteria</h2>

                <form action="${pageContext.request.contextPath}/packages" method="get">

                    <!-- Package Name -->
                    <div class="form-section">
                        <h3>Package</h3>
                        <div class="form-group">
                            <label for="packageName">Package Name (full name, pattern, or * for all)</label>
                            <input type="text" id="packageName" name="packageName" 
                                   placeholder="Example: AOCM* or AOCM000001"
                                   value="<%= savedPackage %>">
                        </div>
                    </div>

                    <!-- Package Status -->
                    <div class="form-section">
                        <h3>Package Status</h3>
                        <div class="checkbox-grid">
                            <div class="checkbox-item">
                                <input type="checkbox" id="status_dev" name="status" value="DEV" <%= statusList.contains("DEV") ? "checked" : "" %>>
                                <label for="status_dev">Dev</label>
                            </div>
                            <div class="checkbox-item">
                                <input type="checkbox" id="status_frz" name="status" value="FRZ" <%= statusList.contains("FRZ") ? "checked" : "" %>>
                                <label for="status_frz">Frz</label>
                            </div>
                            <div class="checkbox-item">
                                <input type="checkbox" id="status_apr" name="status" value="APR" <%= statusList.contains("APR") ? "checked" : "" %>>
                                <label for="status_apr">Apr</label>
                            </div>
                            <div class="checkbox-item">
                                <input type="checkbox" id="status_rej" name="status" value="REJ" <%= statusList.contains("REJ") ? "checked" : "" %>>
                                <label for="status_rej">Rej</label>
                            </div>
                            <div class="checkbox-item">
                                <input type="checkbox" id="status_dis" name="status" value="DIS" <%= statusList.contains("DIS") ? "checked" : "" %>>
                                <label for="status_dis">Dis</label>
                            </div>
                            <div class="checkbox-item">
                                <input type="checkbox" id="status_ins" name="status" value="INS" <%= statusList.contains("INS") ? "checked" : "" %>>
                                <label for="status_ins">Ins</label>
                            </div>
                            <div class="checkbox-item">
                                <input type="checkbox" id="status_bas" name="status" value="BAS" <%= statusList.contains("BAS") ? "checked" : "" %>>
                                <label for="status_bas">Bas</label>
                            </div>
                            <div class="checkbox-item">
                                <input type="checkbox" id="status_bak" name="status" value="BAK" <%= statusList.contains("BAK") ? "checked" : "" %>>
                                <label for="status_bak">Bak</label>
                            </div>
                            <div class="checkbox-item">
                                <input type="checkbox" id="status_del" name="status" value="DEL" <%= statusList.contains("DEL") ? "checked" : "" %>>
                                <label for="status_del">Del</label>
                            </div>
                            <div class="checkbox-item">
                                <input type="checkbox" id="status_opn" name="status" value="OPN" <%= statusList.contains("OPN") ? "checked" : "" %>>
                                <label for="status_opn">Opn</label>
                            </div>
                            <div class="checkbox-item">
                                <input type="checkbox" id="status_clo" name="status" value="CLO" <%= statusList.contains("CLO") ? "checked" : "" %>>
                                <label for="status_clo">Clo</label>
                            </div>
                            <div class="checkbox-item">
                                <input type="checkbox" id="status_tcc" name="status" value="TCC" <%= statusList.contains("TCC") ? "checked" : "" %>>
                                <label for="status_tcc">Tcc</label>
                            </div>
                        </div>
                    </div>

                    <!-- Additional Criteria -->
                    <div class="form-section">
                        <h3>Additional Criteria</h3>
                        <div class="two-columns">
                            <div class="form-group">
                                <label for="creator">Creator</label>
                                <input type="text" id="creator" name="creator" 
                                       placeholder="User ID"
                                       value="<%= savedCreator %>">
                            </div>
                            <div class="form-group">
                                <label for="workRequest">Work Request</label>
                                <input type="text" id="workRequest" name="workRequest" 
                                       placeholder="WRK, CHG..."
                                       value="<%= savedWorkRequest %>">
                            </div>
                        </div>
                        <div class="two-columns">
                            <div class="form-group">
                                <label for="action">Action</label>
                                <select id="action" name="action">
                                    <option value="" <%= savedAction.isEmpty() ? "selected" : "" %>>--- (ALL)</option>
                                    <option value="EMPTY" <%= "EMPTY".equals(savedAction) ? "selected" : "" %>>--- (No Action)</option>
                                    <option value="PRE" <%= "PRE".equals(savedAction) ? "selected" : "" %>>PRE</option>
                                    <option value="SCR" <%= "SCR".equals(savedAction) ? "selected" : "" %>>SCR</option>
                                    <option value="RCP" <%= "RCP".equals(savedAction) ? "selected" : "" %>>RCP</option>
                                    <option value="BLn" <%= "BLn".equals(savedAction) ? "selected" : "" %>>BLn</option>
                                    <option value="CRV" <%= "CRV".equals(savedAction) ? "selected" : "" %>>CRV</option>
                                </select>
                            </div>
                            <div class="form-group">
                                <label for="dept">Material</label>
                                <input type="text" id="dept" name="dept" 
                                       placeholder="Material"
                                       value="<%= savedMaterial %>">
                            </div>
                        </div>
                    </div>

                    <!-- Date Filters -->
                    <div class="form-section">
                        <h3>Date Filters</h3>
                        <div class="two-columns">
                            <div class="form-group">
                                <label>Install Date</label>
                                <div class="date-range">
                                    <input type="date" id="installDateFrom" name="installDateFrom" value="<%= savedInstallFrom %>">
                                    <span>to</span>
                                    <input type="date" id="installDateTo" name="installDateTo" value="<%= savedInstallTo %>">
                                </div>
                            </div>
                            <div class="form-group">
                                <label>Creation Date</label>
                                <div class="date-range">
                                    <input type="date" id="creationDateFrom" name="creationDateFrom" value="<%= savedCreateFrom %>">
                                    <span>to</span>
                                    <input type="date" id="creationDateTo" name="creationDateTo" value="<%= savedCreateTo %>">
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Buttons -->
                    <div class="form-actions">
                        <button type="submit" class="btn btn-primary">Search</button>
                        <a href="${pageContext.request.contextPath}/clear-filters" class="btn btn-secondary">Reset</a>
                    </div>
                </form>
            </div>
        </div>

        <div class="footer">
            <p>POC Web Application | <a href="${pageContext.request.contextPath}/logout">Logout</a></p>
        </div>
    </div>
</body>
</html>