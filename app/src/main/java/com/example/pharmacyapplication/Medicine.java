package com.example.pharmacyapplication;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Medicine {
    private String medName;
    private String medDosage;
    private String medManu;
    private String medQuentity;
    private String medPrice;
    private String medManuDate;
    private String medExDate;

    public Medicine() {
    }

    public Medicine(String medName, String medDosage, String medManu, String medQuentity, String medPrice, String medManuDate, String medExDate) {
        this.medName = medName;
        this.medDosage = medDosage;
        this.medManu = medManu;
        this.medQuentity = medQuentity;
        this.medPrice = medPrice;
        this.medManuDate = medManuDate;
        this.medExDate = medExDate;
    }

    public String getMedName() {
        return medName;
    }

    public String getMedDosage() {
        return medDosage;
    }

    public String getMedManu() {
        return medManu;
    }

    public String getMedQuentity() {
        return medQuentity;
    }

    public String getMedPrice() {
        return medPrice;
    }

    public String getMedManuDate() {
        return medManuDate;
    }

    public String getMedExDate() {
        return medExDate;
    }
}
