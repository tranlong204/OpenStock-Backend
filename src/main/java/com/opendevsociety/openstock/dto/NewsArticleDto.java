package com.opendevsociety.openstock.dto;

public class NewsArticleDto {
    
    private long id;
    private String headline;
    private String summary;
    private String source;
    private String url;
    private long datetime;
    private String category;
    private String related;
    private String image;
    
    // Constructors
    public NewsArticleDto() {}
    
    public NewsArticleDto(long id, String headline, String summary, String source, 
                         String url, long datetime, String category, String related) {
        this.id = id;
        this.headline = headline;
        this.summary = summary;
        this.source = source;
        this.url = url;
        this.datetime = datetime;
        this.category = category;
        this.related = related;
    }
    
    public NewsArticleDto(long id, String headline, String summary, String source, 
                         String url, long datetime, String category, String related, String image) {
        this.id = id;
        this.headline = headline;
        this.summary = summary;
        this.source = source;
        this.url = url;
        this.datetime = datetime;
        this.category = category;
        this.related = related;
        this.image = image;
    }
    
    // Getters and Setters
    public long getId() {
        return id;
    }
    
    public void setId(long id) {
        this.id = id;
    }
    
    public String getHeadline() {
        return headline;
    }
    
    public void setHeadline(String headline) {
        this.headline = headline;
    }
    
    public String getSummary() {
        return summary;
    }
    
    public void setSummary(String summary) {
        this.summary = summary;
    }
    
    public String getSource() {
        return source;
    }
    
    public void setSource(String source) {
        this.source = source;
    }
    
    public String getUrl() {
        return url;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }
    
    public long getDatetime() {
        return datetime;
    }
    
    public void setDatetime(long datetime) {
        this.datetime = datetime;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public String getRelated() {
        return related;
    }
    
    public void setRelated(String related) {
        this.related = related;
    }
    
    public String getImage() {
        return image;
    }
    
    public void setImage(String image) {
        this.image = image;
    }
}
