package com.behave.behave.models;

/**
 * Created by huyanh on 2017. 2. 6..
 */

public class Parent extends User {

    public String email;
    public String firstName;
    public String lastName;
    public Child[] children;
    public String[] prizeList;

    public Parent() {
        // Default constructor required for calls to DataSnapshot.getValue(Parent.class)
    }

    public Parent(String email) {
        this.email = email;
    }
}
