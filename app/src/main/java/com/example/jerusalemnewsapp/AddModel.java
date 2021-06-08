package com.example.jerusalemnewsapp;

public class AddModel {

    public String post_id;
    public String user_id;
    String title;
    String description;
    String photo;

    public AddModel() {
    }

    public AddModel(String title, String description) {
        this.title = title;
        this.description = description;
    }
}
