package com.example.socalbeach4life;

public class Restaurant {
    private String name;
    private double lng, lat, beachlng, beachlat;

    private String hours;

    public Restaurant(double lat, double lng, String name, double beachlat, double beachlng){
        this.name = name;
        this.lng = lng;
        this.lat = lat;
        this.beachlat = beachlat;
        this.beachlng = beachlng;
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

    public double getBeachlng(){ return beachlng; }

    public double getBeachLat(){ return beachlat; }
}
