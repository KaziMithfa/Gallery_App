package com.example.uploadmultiplefiles.Model;

public class ImageModel {

   public UrlModel urls;

    public ImageModel(UrlModel urls) {
        this.urls = urls;
    }

    public UrlModel getUrls() {
        return urls;
    }

    public void setUrls(UrlModel urls) {
        this.urls = urls;
    }
}
