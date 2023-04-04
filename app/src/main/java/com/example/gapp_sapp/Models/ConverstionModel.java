package com.example.gapp_sapp.Models;

public class ConverstionModel {
    String Receiverid, lastMessage, Senderid, SenderName, SenderPic, ReceiverName, ReceiverPic;
    long timestamp;

    public ConverstionModel() {
    }

    public ConverstionModel(String receiverid, String lastMessage, String senderid, String senderName, String senderPic, String receiverName, String receiverPic, long timestamp) {
        Receiverid = receiverid;
        this.lastMessage = lastMessage;
        Senderid = senderid;
        SenderName = senderName;
        SenderPic = senderPic;
        ReceiverName = receiverName;
        ReceiverPic = receiverPic;
        this.timestamp = timestamp;
    }

    public String getReceiverid() {
        return Receiverid;
    }

    public void setReceiverid(String receiverid) {
        Receiverid = receiverid;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getSenderid() {
        return Senderid;
    }

    public void setSenderid(String senderid) {
        Senderid = senderid;
    }

    public String getSenderName() {
        return SenderName;
    }

    public void setSenderName(String senderName) {
        SenderName = senderName;
    }

    public String getSenderPic() {
        return SenderPic;
    }

    public void setSenderPic(String senderPic) {
        SenderPic = senderPic;
    }

    public String getReceiverName() {
        return ReceiverName;
    }

    public void setReceiverName(String receiverName) {
        ReceiverName = receiverName;
    }

    public String getReceiverPic() {
        return ReceiverPic;
    }

    public void setReceiverPic(String receiverPic) {
        ReceiverPic = receiverPic;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}


