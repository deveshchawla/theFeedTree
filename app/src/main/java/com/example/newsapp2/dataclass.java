package com.example.newsapp2;

import java.util.ArrayList;

public class dataclass {
    public String title;
    public String sdetails;
    public String time;
    public String author;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSdetails() {
        return sdetails;
    }

    public void setSdetails(String sdetails) {
        this.sdetails = sdetails;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getGalleryImage() {
        return galleryImage;
    }

    public void setGalleryImage(String galleryImage) {
        this.galleryImage = galleryImage;
    }

    public dataclass(String title, String sdetails, String time, String author, String galleryImage) {
        this.title = title;
        this.sdetails = sdetails;
        this.time = time;
        this.author = author;
        this.galleryImage = galleryImage;
    }

    public String galleryImage;
}
