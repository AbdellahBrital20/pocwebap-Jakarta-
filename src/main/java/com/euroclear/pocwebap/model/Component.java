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
    private String targetLoadLibType;
    
    private String userOption03;
    private String userOption05;
    private String userOption07;
    private String userOption08;

    // === GETTERS ===
    
    public String getApplName() {
        return applName;
    }

    public String getComponent() {
        return component;
    }
    
    // Alias pour JSP - "name" = component
    public String getName() {
        return component;
    }

    public String getComponentType() {
        return componentType;
    }

    public String getComponentStatus() {
        return componentStatus;
    }
    
    // Status converti en texte lisible (ACTIVE, APPROVED, etc.)
    public String getStatus() {
        if (componentStatus == null || componentStatus.isEmpty()) {
            return "---";
        }
        
        String code;
        if (componentStatus.contains(" - ")) {
            code = componentStatus.substring(0, 1);
        } else {
            code = componentStatus;
        }
        
        switch (code) {
            case "0": return "ACTIVE";
            case "1": return "APPROVED";
            case "2": return "CHECKOUT";
            case "3": return "DEMOTED";
            case "4": return "FROZEN";
            case "5": return "INACTIVE";
            case "6": return "INCOMP";
            case "7": return "PROMOTED";
            case "8": return "REFROZEN";
            case "9": return "REJECTED";
            case "A": return "RMTPROMO";
            case "B": return "SUBMITTED";
            case "C": return "UNFROZEN";
            default: return componentStatus;
        }
    }

    public String getDateLastModified() {
        return dateLastModified;
    }

    public String getTimeLastModified() {
        return timeLastModified;
    }

    public String getUpdater() {
        return updater;
    }
    
    // Alias pour JSP
    public String getUser() {
        return updater;
    }

    public String getBuildProc() {
        return buildProc;
    }
    
    // Alias pour JSP - "procname" = buildProc
    public String getProcname() {
        return buildProc;
    }

    public String getLanguage() {
        return language;
    }

    public int getVersion() {
        return version;
    }

    public String getSourceLib() {
        return sourceLib;
    }

    public String getTargetComponent() {
        return targetComponent;
    }

    public String getTargetLoadLibType() {
        return targetLoadLibType;
    }
    
    // LTP - essaie targetLoadLibType, sinon targetComponent
    public String getLtp() {
        if (targetLoadLibType != null && !targetLoadLibType.isEmpty()) {
            return targetLoadLibType;
        } else if (targetComponent != null && !targetComponent.isEmpty()) {
            return targetComponent;
        }
        return "---";
    }

    public String getUserOption03() {
        return userOption03;
    }

    public String getUserOption05() {
        return userOption05;
    }

    public String getUserOption07() {
        return userOption07;
    }

    public String getUserOption08() {
        return userOption08;
    }
    
   // B31 = userOption07
public String getB31() {
    // Si componentType commence par "CR", pas de userOptions
    if (componentType != null && componentType.startsWith("CR")) {
        return "---";
    }
    if ("Y".equals(userOption07)) {
        return "Y";
    } else if ("N".equals(userOption07)) {
        return "N";
    }
    return "---";
}

// B64 = userOption08
public String getB64() {
    // Si componentType commence par "CR", pas de userOptions
    if (componentType != null && componentType.startsWith("CR")) {
        return "---";
    }
    if ("Y".equals(userOption08)) {
        return "Y";
    } else if ("N".equals(userOption08)) {
        return "N";
    }
    return "---";
}

// O31 = userOption03
public String getO31() {
    // Si componentType commence par "CR", pas de userOptions
    if (componentType != null && componentType.startsWith("CR")) {
        return "---";
    }
    if ("Y".equals(userOption03)) {
        return "Y";
    } else if ("N".equals(userOption03)) {
        return "N";
    }
    return "---";
}

// ACC (Access Level) = userOption05
public String getAcc() {
    // Si componentType commence par "CR", pas de userOptions
    if (componentType != null && componentType.startsWith("CR")) {
        return "---";
    }
    if (userOption05 != null && !userOption05.isEmpty()) {
        return userOption05;
    }
    return "---";
}

    // === SETTERS ===
    
    public void setApplName(String applName) {
        this.applName = applName;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public void setComponentType(String componentType) {
        this.componentType = componentType;
    }

    public void setComponentStatus(String componentStatus) {
        this.componentStatus = componentStatus;
    }

    public void setDateLastModified(String dateLastModified) {
        this.dateLastModified = dateLastModified;
    }

    public void setTimeLastModified(String timeLastModified) {
        this.timeLastModified = timeLastModified;
    }

    public void setUpdater(String updater) {
        this.updater = updater;
    }

    public void setBuildProc(String buildProc) {
        this.buildProc = buildProc;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public void setSourceLib(String sourceLib) {
        this.sourceLib = sourceLib;
    }

    public void setTargetComponent(String targetComponent) {
        this.targetComponent = targetComponent;
    }

    public void setTargetLoadLibType(String targetLoadLibType) {
        this.targetLoadLibType = targetLoadLibType;
    }

    public void setUserOption03(String userOption03) {
        this.userOption03 = userOption03;
    }

    public void setUserOption05(String userOption05) {
        this.userOption05 = userOption05;
    }

    public void setUserOption07(String userOption07) {
        this.userOption07 = userOption07;
    }

    public void setUserOption08(String userOption08) {
        this.userOption08 = userOption08;
    }
}