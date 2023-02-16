package com.kameleoon.trial.models;

import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class User {
    private int id;
    private String name;
    private String email;
    private Date dateOfCreation;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public Date getDateOfCreation() {
        return dateOfCreation;
    }
    public void setDateOfCreation(Date dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
    }

    public User(){}

    public User(String name, String email){
        this.name = name;
        this.email = email;
        this.dateOfCreation = new Date();
    }
}
