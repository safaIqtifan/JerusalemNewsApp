package com.example.jerusalemnewsapp.Model;

public class AddModel {

    public String post_id;
    public String user_id;
    public String title;
    public String description;
    public String photo;

    public AddModel() {
    }

    public AddModel(String title, String description) {
        this.title = title;
        this.description = description;
    }
}
