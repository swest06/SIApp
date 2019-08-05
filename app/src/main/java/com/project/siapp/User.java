package com.project.siapp;

import org.jetbrains.annotations.Nullable;

public class User {
    public String uid;
    public String name;
    public String photo = "";
    public String age = "";
    public String gender = "";
    public String about = "";
    public final String location = "London, UK";

    public User(String uid, String name){
        this.uid = uid;
        this.name = name;
    }

    public User(){
        this.uid = "";
        this.name = "";
    }
}
