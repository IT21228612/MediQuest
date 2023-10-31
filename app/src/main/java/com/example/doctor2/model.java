package com.example.doctor2;

public class model {
    private String name;
    private String alt;
    private String age;

    private String symp;

    private String sex;

    private String key;

    private String status;

    public model() {
    }

    public model(String name, String alt, String age, String symp, String sex, String key, String status) {
        this.name = name;
        this.alt = alt;
        this.age = age;
        this.symp = symp;
        this.sex = sex;
        this.key = key;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlt() {
        return alt;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
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

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
