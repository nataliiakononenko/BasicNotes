package com.xstudioo.BasicNotes;

public class Note {
    private long id;
    private String title;
    private String content;
    private String lastUpdated;
    private String created;
    private Boolean flag;
    private Boolean deleted;

    Note(String title, String content, String lastUpdated, String created, Boolean flag, Boolean deleted){
        this.title = title;
        this.content = content;
        this.lastUpdated = lastUpdated;
        this.created = created;
        this.flag = flag;
        this.deleted = deleted;
    }

    Note(long id, String title, String content, String lastUpdated, String created, Boolean flag, Boolean deleted){
        this.id = id;
        this.title = title;
        this.content = content;
        this.lastUpdated = lastUpdated;
        this.created = created;
        this.flag = flag;
        this.deleted = deleted;
    }

    Note(){
       // empty constructor
    }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
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

    public String getLastUpdated() {
        return lastUpdated;
    }
    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getCreated() {
        return created;
    }
    public void setCreated(String created) {
        this.created = created;
    }

    public Boolean getFlag() {
        return flag;
    }
    public void setFlag(Boolean flag) {
        this.flag = flag;
    }

    public Boolean getStatus() {
        return deleted;
    }
    public void setStatus(Boolean deleted) {
        this.deleted = deleted;
    }
}
