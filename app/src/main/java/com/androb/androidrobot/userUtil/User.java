package com.androb.androidrobot.userUtil;

import java.util.Date;

/**
 * Created by kaki on 2018/05/08.
 */

public class User {

    String username;
    String password;
    int score;
    Date sessionExpiryDate;

    public User(String uname, String sc) {
        System.out.println("in User, score: " + sc);
        this.username = uname;
        if(!sc.equals("null")) {
            this.score = Integer.valueOf(sc);
        }
        else {
            this.score = 0;
        }

    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setSessionExpiryDate(Date sessionExpiryDate) {
        this.sessionExpiryDate = sessionExpiryDate;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getScore() {
        return score + "";
    }

    public Date getSessionExpiryDate() {
        return sessionExpiryDate;
    }

}