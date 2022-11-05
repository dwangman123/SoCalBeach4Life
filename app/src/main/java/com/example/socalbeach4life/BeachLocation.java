package com.example.socalbeach4life;

public class BeachLocation {
    private String name;
    private double lng, lat;
    public BeachLocation(double lat, double lng, String name){
        this.name = name;
        this.lng = lng;
        this.lat = lat;
    }

    public double getLat(){
        return lat;
    }

    public double getLong(){
        return lng;
    }

    public String getName(){
        return name;
    }
}
