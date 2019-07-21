package com.project.siapp;

public class User {
    String uid;
    String name;
    String photo;

    public User(String uid, String name){
        this.uid = uid;
        this.name = name;
    }

    public User(String uid, String name, String photo){
        this.uid = uid;
        this.name = name;
        this.photo = photo;
    }
}
