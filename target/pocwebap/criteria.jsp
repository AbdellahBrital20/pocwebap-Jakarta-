<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
    <!-- Dark Mode Toggle Button -->
    <button id="themeToggle" class="theme-toggle" onclick="toggleTheme()">Dark Mode</button>

    <div class="container">
    
        <!-- Header -->
        <div class="header">
            <h1>EUROCLEAR</h1>
            <p class="subtitle">Package List Parameters</p>
        </div>
        
        <div class="criteria-container">
            <div class="card criteria-card">
                <h2>Search Criteria</h2>
                
                <!-- Search Form -->
                <form action="${pageContext.request.contextPath}/packages" method="get">
                
                    <!-- Package Name -->
                    <div class="form-section">
                        <h3>Package</h3>
                        <div class="form-group">
                            <label for="packageName">Package Name (full name, pattern, or * for all)</label>
                            <input type="text" id="packageName" name="packageName" 
                                   placeholder="Example: AOCM* or AOCM000001">
                        </div>
                    </div>
                    
                    <!-- Package Status -->
                    <div class="form-section">
                        <h3>Package Status (select with /)</h3>
                        <div class="checkbox-grid">
                            <div class="checkbox-item">
                                <input type="checkbox" id="status_dev" name="status" value="DEV">
                                <label for="status_dev">Dev</label>
                            </div>
                            <div class="checkbox-item">
                                <input type="checkbox" id="status_frz" name="status" value="FRZ">
                                <label for="status_frz">Frz</label>
                            </div>
                            <div class="checkbox-item">
                                <input type="checkbox" id="status_apr" name="status" value="APR">
                                <label for="status_apr">Apr</label>
                            </div>
                            <div class="checkbox-item">
                                <input type="checkbox" id="status_rej" name="status" value="REJ">
                                <label for="status_rej">Rej</label>
                            </div>
                            <div class="checkbox-item">
                                <input type="checkbox" id="status_dis" name="status" value="DIS">
                                <label for="status_dis">Dis</label>
                            </div>
                            <div class="checkbox-item">
                                <input type="checkbox" id="status_ins" name="status" value="INS">
                                <label for="status_ins">Ins</label>
                            </div>
                            <div class="checkbox-item">
                                <input type="checkbox" id="status_bas" name="status" value="BAS">
                                <label for="status_bas">Bas</label>
                            </div>
                            <div class="checkbox-item">
                                <input type="checkbox" id="status_bak" name="status" value="BAK">
                                <label for="status_bak">Bak</label>
                            </div>
                            <div class="checkbox-item">
                                <input type="checkbox" id="status_del" name="status" value="DEL">
                                <label for="status_del">Del</label>
                            </div>
                            <div class="checkbox-item">
                                <input type="checkbox" id="status_opn" name="status" value="OPN">
                                <label for="status_opn">Opn</label>
                            </div>
                            <div class="checkbox-item">
                                <input type="checkbox" id="status_clo" name="status" value="CLO">
                                <label for="status_clo">Clo</label>
                            </div>
                            <div class="checkbox-item">
                                <input type="checkbox" id="status_tcc" name="status" value="TCC">
                                <label for="status_tcc">Tcc</label>
                            </div>
                        </div>
                    </div>
                    
                    <!-- Package Level -->
                    <div class="form-section">
                        <h3>Package Level</h3>
                        <div class="checkbox-grid">
                            <div class="checkbox-item">
                                <input type="checkbox" id="level_simple" name="level" value="SIMPLE">
                                <label for="level_simple">Simple</label>
                            </div>
                            <div class="checkbox-item">
                                <input type="checkbox" id="level_super" name="level" value="SUPER">
                                <label for="level_super">Super</label>
                            </div>
                            <div class="checkbox-item">
                                <input type="checkbox" id="level_complex" name="level" value="COMPLEX">
                                <label for="level_complex">Complex</label>
                            </div>
                            <div class="checkbox-item">
                                <input type="checkbox" id="level_part" name="level" value="PARTICIPATING">
                                <label for="level_part">Participating</label>
                            </div>
                        </div>
                    </div>
                    
                    <!-- Package Type -->
                    <div class="form-section">
                        <h3>Package Type</h3>
                        <div class="checkbox-grid">
                            <div class="checkbox-item">
                                <input type="checkbox" id="type_pp" name="type" value="PP">
                                <label for="type_pp">Planned Perm</label>
                            </div>
                            <div class="checkbox-item">
                                <input type="checkbox" id="type_pt" name="type" value="PT">
                                <label for="type_pt">Planned Temp</label>
                            </div>
                            <div class="checkbox-item">
                                <input type="checkbox" id="type_up" name="type" value="UP">
                                <label for="type_up">Unplanned Perm</label>
                            </div>
                            <div class="checkbox-item">
                                <input type="checkbox" id="type_ut" name="type" value="UT">
                                <label for="type_ut">Unplanned Temp</label>
                            </div>
                        </div>
                    </div>
                    
                    <!-- Additional Criteria -->
                    <div class="form-section">
                        <h3>Additional Criteria</h3>
                        <div class="two-columns">
                            <div class="form-group">
                                <label for="creator">Creator</label>
                                <input type="text" id="creator" name="creator" placeholder="User ID">
                            </div>
                            <div class="form-group">
                                <label for="workRequest">Work Request</label>
                                <input type="text" id="workRequest" name="workRequest" placeholder="WRK, CHG...">
                            </div>
                        </div>
                        <div class="two-columns">
                            <div class="form-group">
                                <label for="action">Action</label>
                                <select id="action" name="action">
                                    <option value="">---</option>
                                    <option value="PRE">PRE</option>
                                    <option value="SCR">SCR</option>
                                    <option value="RCP">RCP</option>
                                    <option value="BLn">BLn</option>
                                    <option value="CRV">CRV</option>
                                </select>
                            </div>
                            <div class="form-group">
                                <label for="dept">Department</label>
                                <input type="text" id="dept" name="dept" placeholder="Department">
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
                                    <input type="date" id="installDateFrom" name="installDateFrom">
                                    <span>to</span>
                                    <input type="date" id="installDateTo" name="installDateTo">
                                </div>
                            </div>
                            <div class="form-group">
                                <label>Creation Date</label>
                                <div class="date-range">
                                    <input type="date" id="creationDateFrom" name="creationDateFrom">
                                    <span>to</span>
                                    <input type="date" id="creationDateTo" name="creationDateTo">
                                </div>
                            </div>
                        </div>
                    </div>
                    
                    <!-- Buttons -->
                    <div class="form-actions">
                        <button type="submit" class="btn btn-primary">Search</button>
                        <button type="reset" class="btn btn-secondary">Reset</button>
                    </div>
                    
                </form>
            </div>
        </div>
        
        <!-- Footer -->
        <div class="footer">
            <p>POC Web Application | <a href="${pageContext.request.contextPath}/login">Logout</a></p>
        </div>
        
    </div>
</body>
</html>