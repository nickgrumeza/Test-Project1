package com.kameleoon.trial.models;

import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class Quote implements Comparable<Quote>{
    private int id;
    private String text;
    private int userId;
    private int score;
    private String author;

    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public int getScore() {
        return score;
    }
    public void setScore(int score) {
        this.score = score;
    }
    public java.sql.Date getDate() {
        return (java.sql.Date) date;
    }
    public void setDate(Date date) {
        this.date = date;
    }
    private Date date;

    @Override
    public int compareTo(Quote o) {
        return compareTo(o);
    }
}
