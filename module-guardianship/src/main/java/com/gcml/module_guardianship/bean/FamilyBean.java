package com.gcml.module_guardianship.bean;

public class FamilyBean {
    private String familyName;
    private String familyTag;
    private String familyTelphone;

    public FamilyBean() {
    }

    public FamilyBean(String familyName, String familyTag, String familyTelphone) {
        this.familyName = familyName;
        this.familyTag = familyTag;
        this.familyTelphone = familyTelphone;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getFamilyTag() {
        return familyTag;
    }

    public void setFamilyTag(String familyTag) {
        this.familyTag = familyTag;
    }

    public String getFamilyTelphone() {
        return familyTelphone;
    }

    public void setFamilyTelphone(String familyTelphone) {
        this.familyTelphone = familyTelphone;
    }
}
