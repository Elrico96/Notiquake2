package com.example.notiquake.app.model;

public class News {
    private String thumbnail;
    private String title;
    private String url;
    private String publishedDate;
    private String author;

    public News(String thumbnail, String title, String url, String publishedDate, String author) {
        this.thumbnail = thumbnail;
        this.title = title;
        this.url = url;
        this.publishedDate = publishedDate;
        this.author = author;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public String getAuthor() {
        return author;
    }
}
