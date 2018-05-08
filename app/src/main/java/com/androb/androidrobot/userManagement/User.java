package com.androb.androidrobot.userManagement;

import java.util.Date;

/**
 * Created by kaki on 2018/05/08.
 */

public class User {

    String username;
    String password;
    int score;
    Date sessionExpiryDate;

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

    public int getScore() {
        return score;
    }

    public Date getSessionExpiryDate() {
        return sessionExpiryDate;
    }

}