package com.example.gapp_sapp.Models;

public class MessegeModel {
    String uid,messege;
    Long timestamp;

    public MessegeModel(String uid, String messege, Long timestamp) {
        this.uid = uid;
        this.messege = messege;
        this.timestamp = timestamp;
    }

    public MessegeModel() {
    }

    public MessegeModel(String uid, String messege) {
        this.uid = uid;
        this.messege = messege;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getMessege() {
        return messege;
    }

    public void setMessege(String messege) {
        this.messege = messege;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
