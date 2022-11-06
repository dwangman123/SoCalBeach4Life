package com.example.socalbeach4life;

import java.util.List;

public class User {
    private String email;
    private String name;
    private String phoneNo;
    private String id;
    private List<String> trips;
    public User() {};

    public User(String email, String name, String phoneNo, String id){
        this.email = email;
        this.name = name;
        this.phoneNo= phoneNo;
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public String getId() {
        return id;
    }

    public List<String> getTrips(){ return trips;}
}
