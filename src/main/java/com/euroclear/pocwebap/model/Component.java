package com.euroclear.pocwebap.model;

public class Component {

    private String applName;
    private String component;
    private String componentType;
    private String componentStatus;
    private String dateLastModified;
    private String timeLastModified;
    private String updater;
    private String buildProc;
    private String language;
    private int version;
    private String sourceLib;
    private String targetComponent;
    
    // 
    private String userOption03;
    private String userOption05;
    private String userOption07;
    private String userOption08;

    public String getApplName() {
        return applName;
    }

    public void setApplName(String applName) {
        this.applName = applName;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public String getComponentType() {
        return componentType;
    }

    public void setComponentType(String componentType) {
        this.componentType = componentType;
    }

    public String getComponentStatus() {
        return componentStatus;
    }

    public void setComponentStatus(String componentStatus) {
        this.componentStatus = componentStatus;
    }

    public String getDateLastModified() {
        return dateLastModified;
    }

    public void setDateLastModified(String dateLastModified) {
        this.dateLastModified = dateLastModified;
    }

    public String getTimeLastModified() {
        return timeLastModified;
    }

    public void setTimeLastModified(String timeLastModified) {
        this.timeLastModified = timeLastModified;
    }

    public String getUpdater() {
        return updater;
    }

    public void setUpdater(String updater) {
        this.updater = updater;
    }

    public String getBuildProc() {
        return buildProc;
    }

    public void setBuildProc(String buildProc) {
        this.buildProc = buildProc;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getSourceLib() {
        return sourceLib;
    }

    public void setSourceLib(String sourceLib) {
        this.sourceLib = sourceLib;
    }

    public String getTargetComponent() {
        return targetComponent;
    }

    public void setTargetComponent(String targetComponent) {
        this.targetComponent = targetComponent;
    }

    public String getUserOption03() {
        return userOption03;
    }

    public void setUserOption03(String userOption03) {
        this.userOption03 = userOption03;
    }

    public String getUserOption05() {
        return userOption05;
    }

    public void setUserOption05(String userOption05) {
        this.userOption05 = userOption05;
    }

    public String getUserOption07() {
        return userOption07;
    }

    public void setUserOption07(String userOption07) {
        this.userOption07 = userOption07;
    }

    public String getUserOption08() {
        return userOption08;
    }

    public void setUserOption08(String userOption08) {
        this.userOption08 = userOption08;
    }
}