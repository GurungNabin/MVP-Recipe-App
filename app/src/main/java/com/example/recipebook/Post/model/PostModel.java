package com.example.recipebook.Post.model;

public class PostModel {

    private int id;
    private String title;
    private String body;

    public PostModel(int id, String updatedTitle, String updateBody) {
        this.id = id;
        this.title = updatedTitle;
        this.body = updateBody;
    }

    public PostModel() {

    }

    public String getTitle() { return title; }
    public void setTitle(String value) { this.title = value; }

    public String getBody() { return body; }
    public void setBody(String value) { this.body = value; }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
