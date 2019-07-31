package com.project.siapp;

public class User {
    public String uid;
    public String name;
    public String photo = "";
    public String age = "";
    public String gender = "";
    public String about = "";

    public User(String uid, String name){
        this.uid = uid;
        this.name = name;
    }

    public User(){
        this.uid = "";
        this.name = "";
    }
}
