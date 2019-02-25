package com.gcml.module_mine.bean;

public class SetupBean {
    private String title;
    private String content;

    public SetupBean() {
    }

    public SetupBean(String title, String content) {
        this.title = title;
        this.content = content;
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
}
