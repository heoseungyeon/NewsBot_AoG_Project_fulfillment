package com.o2o.action.server.DTO;

public class NewsDTO {
    private String url;
    private String title;
    private String word;
    private String aid;
    private String context;
    private String imgurl;
    private String fullcontext;

    public NewsDTO(String title, String url, String word, String aid, String context, String imgurl) {
        this.title = title;
        this.url = url;
        this.word = word;
        this.aid = aid;
        this.context = context;
        this.imgurl = imgurl;
    }

    public String getFullcontext() {
        return fullcontext;
    }

    public void setFullcontext(String fullcontext) {
        this.fullcontext = fullcontext;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }


    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
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

