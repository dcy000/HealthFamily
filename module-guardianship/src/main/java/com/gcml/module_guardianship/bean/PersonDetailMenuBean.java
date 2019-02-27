package com.gcml.module_guardianship.bean;

public class PersonDetailMenuBean {
    private String title;
    private String content;
    private int img;

    public PersonDetailMenuBean() {
    }

    public PersonDetailMenuBean(String title, String content, int img) {
        this.title = title;
        this.content = content;
        this.img = img;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }
}
