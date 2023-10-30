package com.example.mediquest;

import com.google.firebase.firestore.GeoPoint;

public class MedicineItem {
    private String name;
    private String lat;
    private String lon;
    private String pharmacy;
    private double price;
    private String unit;

    private String geoAddress;



    private double distance;



    public MedicineItem(String name, String pharmacy,double price,String lat,String lon, String unit) {
        this.name = name;
        this.lat = lat;
        this.lon=lon;
        this.pharmacy = pharmacy;
        this.price = price;
        this.unit = unit;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getDistance() {
        return distance;
    }

    public void setGeoAddress(String geoAddress) {
        this.geoAddress = geoAddress;
    }
    public String getGeoAddress(){
        return geoAddress;
    }

    public String getName() {
        return name;
    }

    public String getLat() {
        return lat;
    }
    public String getLon() {
        return lon;
    }

    public String getPharmacy() {
        return pharmacy;
    }

    public double getPrice() {
        return price;
    }

    public String getUnit() {
        return unit;
    }
}

