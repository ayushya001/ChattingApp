package com.example.gapp_sapp.Models;

import java.util.List;

public class GroupModel {
    String groupName, groupImage;

    private List<String> userIds;
    long lastmessage;

    public String getLastConversation() {
        return lastConversation;
    }

    public void setLastConversation(String lastConversation) {
        this.lastConversation = lastConversation;
    }

    String lastConversation;

    String groupid;

    public GroupModel(String groupName, String groupImage, long lastmessage) {
        this.groupName = groupName;
        this.groupImage = groupImage;
        this.lastmessage = lastmessage;
    }

    public GroupModel() {
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupImage() {
        return groupImage;
    }

    public void setGroupImage(String groupImage) {
        this.groupImage = groupImage;
    }

    public long getLastmessage() {
        return lastmessage;
    }

    public void setLastmessage(long lastmessage) {
        this.lastmessage = lastmessage;
    }

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    public List<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<String> userIds) {
        this.userIds = userIds;
    }
}

