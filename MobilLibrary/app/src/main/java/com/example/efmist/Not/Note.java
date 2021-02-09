package com.example.efmist.Not;

public class Note {

    private String ntitle;
    private String title;
    private String author;
    private String description;

    public Note() {

    }

    public Note(String ntitle, String description, String title, String author) {
        this.ntitle = ntitle;
        this.description = description;
        this.title = title;
        this.author = author;


    }

    public String getNTitle() {
        return ntitle;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }


    public void setNtitle(String ntitle) {
        this.ntitle = ntitle;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
