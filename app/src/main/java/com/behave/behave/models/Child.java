package com.behave.behave.models;

/**
 * Created by huyanh on 2017. 2. 6..
 */

public class Child extends User {

    public String id;
    public String parentId;
    public String name;
    public int tokens = 0;

    public Child() {

    }

    public Child(String id, String parentId, String name) {
        this.id = id;
        this.parentId = parentId;
        this.name = name;
    }
}
