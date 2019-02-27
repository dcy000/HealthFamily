package com.gcml.module_guardianship.bean;

public class HealthManagerReportBean {
    private String name;
    private String commitTime;
    private String auditTime;

    public HealthManagerReportBean() {
    }

    public HealthManagerReportBean(String name, String commitTime, String auditTime) {
        this.name = name;
        this.commitTime = commitTime;
        this.auditTime = auditTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCommitTime() {
        return commitTime;
    }

    public void setCommitTime(String commitTime) {
        this.commitTime = commitTime;
    }

    public String getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(String auditTime) {
        this.auditTime = auditTime;
    }
}
