package com.example.socalbeach4life;

public class Beach {

    private String hours, name;
    private double latitude, longitude;

    public Beach(String name, String hours, double latitude, double longitude){
        this.hours = hours;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }


    public double getLat(){
        return latitude;
    }

    public double getLong(){
        return longitude;
    }

    public String getHours(){
        return hours;
    }

    public String getName(){
        return name;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }
}
