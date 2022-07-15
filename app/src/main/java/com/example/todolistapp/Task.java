package com.example.todolistapp;

public class Task {
    private int id;
    private String name;
    private Date date;
    private boolean done;

    public Task(int id, String name, Date date) {
        this.id = id;
        this.name = name;
        this.date = date;
    }

    public Task(int id, String name, Date date, boolean done) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.done = done;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Task(String name, Date date) {
        this.name = name;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
