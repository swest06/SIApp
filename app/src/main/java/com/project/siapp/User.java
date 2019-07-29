package com.project.siapp;

public class User {
    public String uid;
    public String name;
    public String photo;

    public User(String uid, String name){
        this.uid = uid;
        this.name = name;
        this.photo = "";
    }

    public User(String uid, String name, String photo){
        this.uid = uid;
        this.name = name;
        this.photo = photo;
    }

    public User(){
        this.uid = "";
        this.name = "";
        this.photo = "";
    }
}
