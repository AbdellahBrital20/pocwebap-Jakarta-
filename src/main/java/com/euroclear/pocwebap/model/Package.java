package com.euroclear.pocwebap.model;

public class Package {
    
    private String packageName;
    private String packageStatus;
    private String dateCreated;
    private String dateInstalled;
    private String workChangeRequest;
    private String creator;
    private String packageLevel;
    private String packageType;
    private String packageTitle;
    private String requestorDept;

    public String getPackage() {
        return packageName;
    }

    public void setPackage(String packageName) {
        this.packageName = packageName;
    }

    public String getPackageStatus() {
        return packageStatus;
    }

    public void setPackageStatus(String packageStatus) {
        this.packageStatus = packageStatus;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getDateInstalled() {
        return dateInstalled;
    }

    public void setDateInstalled(String dateInstalled) {
        this.dateInstalled = dateInstalled;
    }

    public String getWorkChangeRequest() {
        return workChangeRequest;
    }

    public void setWorkChangeRequest(String workChangeRequest) {
        this.workChangeRequest = workChangeRequest;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getPackageLevel() {
        return packageLevel;
    }

    public void setPackageLevel(String packageLevel) {
        this.packageLevel = packageLevel;
    }

    public String getPackageType() {
        return packageType;
    }

    public void setPackageType(String packageType) {
        this.packageType = packageType;
    }

    public String getPackageTitle() {
        return packageTitle;
    }

    public void setPackageTitle(String packageTitle) {
        this.packageTitle = packageTitle;
    }

    public String getRequestorDept() {
        return requestorDept;
    }

    public void setRequestorDept(String requestorDept) {
        this.requestorDept = requestorDept;
    }
}