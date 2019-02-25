package com.gcml.module_login_and_register.bean;

public class RegisterPutBean {
    private String accountType;
    private String mobileNum;
    private String guardianName;
    private String password;
    private String community;

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getMobileNum() {
        return mobileNum;
    }

    public void setMobileNum(String mobileNum) {
        this.mobileNum = mobileNum;
    }

    public String getGuardianName() {
        return guardianName;
    }

    public void setGuardianName(String guardianName) {
        this.guardianName = guardianName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCommunity() {
        return community;
    }

    public void setCommunity(String community) {
        this.community = community;
    }

    @Override
    public String toString() {
        return "RegisterPutBean{" +
                "accountType='" + accountType + '\'' +
                ", mobileNum='" + mobileNum + '\'' +
                ", guardianName='" + guardianName + '\'' +
                ", password='" + password + '\'' +
                ", community='" + community + '\'' +
                '}';
    }
}
