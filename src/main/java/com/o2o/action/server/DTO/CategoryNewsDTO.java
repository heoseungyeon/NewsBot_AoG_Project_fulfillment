package com.o2o.action.server.DTO;

public class CategoryNewsDTO {
    private int category;
    private String url;
    private String title;
    private String aid;
    private String context;

    public CategoryNewsDTO(int category, String title, String url, String aid, String context){
        this.category=category;
        this.title = title;
        this.url = url;
        this.aid = aid;
        this.context = context;
    }
    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

}
