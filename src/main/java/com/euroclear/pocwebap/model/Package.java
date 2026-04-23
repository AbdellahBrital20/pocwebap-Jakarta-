package com.euroclear.pocwebap.model;

public class Package {

    private String packageId;
    private String packageTitle;
    private String packageStatus;
    private String packageType;
    private String packageLevel;
    private String creator;
    private String workChangeRequest;
    private String dateCreated;
    private String dateInstalled;
    private String requestorDept;
    private String requestorPhone;
    private String requestorName;
    private String auditReturnCode;
    private String userVarLen101;
    private String userVarLen102;

    // === GETTERS ===

    public String getPackageId() {
        return packageId;
    }
    
    public String getPackage() {
        return packageId;
    }

    public String getPackageTitle() {
        return packageTitle;
    }

    public String getPackageStatus() {
        return packageStatus;
    }
    
    public String getStatusCode() {
        if (packageStatus == null || packageStatus.isEmpty()) {
            return "";
        }
        if (packageStatus.contains(" - ")) {
            return packageStatus.substring(0, 1);
        }
        return packageStatus;
    }

    public String getPackageType() {
        return packageType;
    }

    public String getPackageLevel() {
        return packageLevel;
    }

    public String getCreator() {
        return creator;
    }

    public String getWorkChangeRequest() {
        return workChangeRequest;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public String getDateInstalled() {
        return dateInstalled;
    }

    public String getRequestorDept() {
        return requestorDept;
    }

    public String getRequestorPhone() {
        return requestorPhone;
    }

    public String getRequestorName() {
        return requestorName;
    }

    public String getAuditReturnCode() {
        return auditReturnCode;
    }
    
    public String getAU() {
        if (auditReturnCode != null && !auditReturnCode.isEmpty()) {
            return auditReturnCode;
        }
        return "---";
    }

    public String getUserVarLen101() {
        return userVarLen101;
    }
    
    public String getT() {
        if (userVarLen101 != null && !userVarLen101.isEmpty()) {
            return userVarLen101;
        }
        return "---";
    }

    public String getUserVarLen102() {
        return userVarLen102;
    }
    
    public String getC() {
        if (userVarLen102 != null && !userVarLen102.isEmpty()) {
            return userVarLen102;
        }
        return "---";
    }

    // === SETTERS ===

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }
    
    public void setPackage(String packageId) {
        this.packageId = packageId;
    }

    public void setPackageTitle(String packageTitle) {
        this.packageTitle = packageTitle;
    }

    public void setPackageStatus(String packageStatus) {
        this.packageStatus = packageStatus;
    }

    public void setPackageType(String packageType) {
        this.packageType = packageType;
    }

    public void setPackageLevel(String packageLevel) {
        this.packageLevel = packageLevel;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public void setWorkChangeRequest(String workChangeRequest) {
        this.workChangeRequest = workChangeRequest;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public void setDateInstalled(String dateInstalled) {
        this.dateInstalled = dateInstalled;
    }

    public void setRequestorDept(String requestorDept) {
        this.requestorDept = requestorDept;
    }

    public void setRequestorPhone(String requestorPhone) {
        this.requestorPhone = requestorPhone;
    }

    public void setRequestorName(String requestorName) {
        this.requestorName = requestorName;
    }

    public void setAuditReturnCode(String auditReturnCode) {
        this.auditReturnCode = auditReturnCode;
    }

    public void setUserVarLen101(String userVarLen101) {
        this.userVarLen101 = userVarLen101;
    }

    public void setUserVarLen102(String userVarLen102) {
        this.userVarLen102 = userVarLen102;
    }
}