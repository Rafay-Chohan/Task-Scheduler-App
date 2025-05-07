package com.example.taskscheduler;

public class Notifications {
    int id,taskid;
    String message,date;

    public Notifications(int id, String message, String date) {
        this.id = id;
        this.message = message;
        this.date = date;
    }

    public Notifications(int id, int taskid, String message, String date) {
        this.id = id;
        this.taskid = taskid;
        this.message = message;
        this.date = date;
    }

    public int getTaskid() {
        return taskid;
    }

    public void setTaskid(int taskid) {
        this.taskid = taskid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
