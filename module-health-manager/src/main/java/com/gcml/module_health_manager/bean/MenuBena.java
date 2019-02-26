package com.gcml.module_health_manager.bean;

public class MenuBena {
    private int imgResource;
    private String title;

    public MenuBena() {
    }

    public MenuBena(int imgResource, String title) {
        this.imgResource = imgResource;
        this.title = title;
    }

    public int getImgResource() {
        return imgResource;
    }

    public void setImgResource(int imgResource) {
        this.imgResource = imgResource;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
