package com.example.kursa4new;

public class Note {

    private String theme;
    private String message;

    public Note(String theme, String message){

        this.theme=theme;
        this.message = message;
    }

    public String getTheme() {
        return this.theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    
}
