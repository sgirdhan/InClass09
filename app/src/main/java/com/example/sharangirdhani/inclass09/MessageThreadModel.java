package com.example.sharangirdhani.inclass09;

/**
 * Created by KashishSyeda on 11/6/2017.
 */

public class MessageThreadModel {
    private String title;
    private int userID;
    private int threadID;

    public MessageThreadModel(String title, int userID, int threadID) {
        this.title = title;
        this.userID = userID;
        this.threadID = threadID;
    }

    @Override
    public String toString() {
        return "MessageThreadModel{" +
                "title='" + title + '\'' +
                ", userID=" + userID +
                ", threadID=" + threadID +
                '}';
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getThreadID() {
        return threadID;
    }

    public void setThreadID(int threadID) {
        this.threadID = threadID;
    }
}