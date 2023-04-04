package com.example.gapp_sapp.Models;

public class UserModel {
    String name;
    String fullname;
    String email;
    String id;
    String password;


    public long getLastmessage() {
        return lastmessage;
    }

    public void setLastmessage(long lastmessage) {
        this.lastmessage = lastmessage;
    }

    long lastmessage;

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    String profilePhoto;
    String bio;

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UserModel(String name, String fullname, String email, String id, String password) {
        this.name = name;
        this.fullname = fullname;
        this.email = email;
        this.id = id;
        this.password = password;
    }

    public UserModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }



    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
