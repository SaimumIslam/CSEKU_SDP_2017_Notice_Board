package com.example.foysal.noticeboardextend;

public class Notice {
    private String title;
    private String description;
    private String noticeWriter;
    private String noticeId;
    private String date;
    private String batch;
    private String file;
    private String showF;

    public Notice() {
    }

    public Notice(String title, String description, String noticeWriter) {
        this.title = title;
        this.description = description;
        this.noticeWriter = noticeWriter;
    }

    public Notice(String title, String description, String noticeWriter, String date, String batch, String file, String showF) {
        this.title = title;
        this.description = description;
        this.noticeWriter = noticeWriter;
        this.date = date;
        this.batch = batch;
        this.file = file;
        this.showF = showF;
    }

    public Notice(String title, String description, String noticeWriter, String noticeId, String date, String batch, String file, String showF) {
        this.title = title;
        this.description = description;
        this.noticeWriter = noticeWriter;
        this.noticeId = noticeId;
        this.date = date;
        this.batch = batch;
        this.file = file;
        this.showF = showF;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public String getnoticeWriter() {
        return noticeWriter;
    }

    public void setnoticeWriter(String noticeWriter) {
        this.noticeWriter = noticeWriter;
    }

    public String getdescription() {
        return description;
    }

    public void setdescription(String description) {
        this.description = description;
    }
    public String getNoticeId()
    {
        return noticeId;
    }

    public String getFile() {
        return file;
    }

    public String getBatch() {
        return batch;
    }

    public String getDate() {
        return date;
    }

    public String getShowF() {
        return showF;

    }
    public void setShowF(String showF) {
        this.showF = showF;
    }

}
