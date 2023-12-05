package com.ass3.project;

public class Profiledata {

    String  dp;
    String name;
    String about;
    String email;
    int upload_count;

    public Profiledata(String dp, String name, String about, String email, int upload_count) {
        this.dp = dp;
        this.name = name;
        this.about = about;
        this.email = email;
        this.upload_count = upload_count;
    }

    public Profiledata() {

    }

    public int getUpload_count() {
        return upload_count;
    }

    public void setUpload_count(int upload_count) {
        this.upload_count = upload_count;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDp() {
        return dp;
    }

    public void setDp(String dp) {
        this.dp = dp;
    }

    public String getFname() {
        return name;
    }

    public void setFname(String fname) {
        this.name = fname;
    }

    public String getLname() {
        return about;
    }

    public void setLname(String lname) {
        this.about = lname;
    }
}
