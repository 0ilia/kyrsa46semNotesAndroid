package com.example.kursa4new;

public class Note {
    private  int id;
    private String theme;
    private String message;

    public Note(String theme, String message, int id) {

        this.theme = theme;
        this.message = message;
        this.id = id;
    }

    public String getTheme() {
        return this.theme;
    }

    public int getId() {
        return this.id;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}
