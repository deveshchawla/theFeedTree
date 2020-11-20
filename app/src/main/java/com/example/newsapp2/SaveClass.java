package com.example.newsapp2;

public class SaveClass {


    public String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String url;

    public SaveClass(String title, String url) {
        this.title = title;
        this.url = url;
    }
}
