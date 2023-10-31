package com.example.doctor2;

public class Doctor {
    String name;
    String Age;
    String Alt;
    String symp;
    String sex;
    
    String reason;

    String instruction;

    String status;

    public Doctor() {
    }

    public Doctor(String name, String age, String alt, String symp, String sex, String reason, String instruction, String status) {
        this.name = name;
        Age = age;
        Alt = alt;
        this.symp = symp;
        this.sex = sex;
        this.reason = reason;
        this.instruction = instruction;
        this.status = status;
    }

    public Doctor(String reason, String instruction) {
        this.reason = reason;
        this.instruction = instruction;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return Age;
    }

    public void setAge(String age) {
        Age = age;
    }

    public String getAlt() {
        return Alt;
    }

    public void setAlt(String alt) {
        Alt = alt;
    }

    public String getSymp() {
        return symp;
    }

    public void setSymp(String symp) {
        this.symp = symp;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
