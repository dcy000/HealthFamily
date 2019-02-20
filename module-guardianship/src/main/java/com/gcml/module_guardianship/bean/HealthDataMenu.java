package com.gcml.module_guardianship.bean;

public class HealthDataMenu {

    /**
     * project : 运动
     * value : 8000
     * unit : 步
     */

    private String project;
    private int value;
    private String unit;

    public HealthDataMenu() {
    }

    public HealthDataMenu(String project, int value, String unit) {
        this.project = project;
        this.value = value;
        this.unit = unit;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
