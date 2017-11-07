package com.example.sharangirdhani.inclass09;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by sharangirdhani on 11/6/17.
 */

public class Message {

    private String firstName;
    private String lastName;
    private int id;
    private int user_id;
    private String message;
    private String created;

    public Message(String firstName, String lastName, int id, int user_id, String message, String created) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.id = id;
        this.user_id = user_id;
        this.message = message;
        this.created = created;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public Message() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getPrettyTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date = new Date(0);
        try {
            date = dateFormat.parse(created);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        TimeZone estTime = TimeZone.getTimeZone("EST");
        dateFormat.setTimeZone(estTime);

        PrettyTime p = new PrettyTime();
        return p.format(new Date(date.getTime() - (5*60*60*1000)));
    }

}
