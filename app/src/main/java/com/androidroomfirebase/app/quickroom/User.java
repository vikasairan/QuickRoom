package com.androidroomfirebase.app.quickroom;

public class User {
    private String name="";
    private String contact="";
    private String email="";
    public User()
    {

    }
    public String getName()
    {
        return name;
    }
    public void setName(String name) {this.name = name;}
    public String getcontact()
    {
        return contact;
    }
    public void setcontact(String contact)
    {
        this.contact = contact;
    }
    public String getEmail()
    {
        return email;
    }
    public void setEmail(String email)
    {
        this.email = email;
    }
}