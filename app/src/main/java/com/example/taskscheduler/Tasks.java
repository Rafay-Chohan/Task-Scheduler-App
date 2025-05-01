package com.example.taskscheduler;

public class Tasks {

    int id;
    String title, description, Date,Status;

    public Tasks(int id,String title, String description, String date,String status) {
        this.id=id;
        this.title = title;
        this.description = description;
        Date = date;
        Status=status;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }
}
