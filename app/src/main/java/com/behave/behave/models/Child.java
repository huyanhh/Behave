package com.behave.behave.models;

/**
 * Created by huyanh on 2017. 2. 6..
 */

public class Child extends User {

    public String parentId;
    public int tokens = 0;

    public Child() {

    }

    public Child(String uid, String parentId, String name) {
        this.uid = uid;
        this.parentId = parentId;
        this.name = name;
    }
}
