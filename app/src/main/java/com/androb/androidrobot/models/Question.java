package com.androb.androidrobot.models;

/**
 * Created by kaki on 2018/05/14.
 */

public class Question {

    private String type;
    private int id;

    public Question(String type, int id){
        this.type = type;
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setType(String type) {

        this.type = type;
    }

}
