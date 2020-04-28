package com.example.kursa4new;

public class Note {
    private  int id;
    private String theme;
    private String message;
    private String dateUpdate;
    private String dateCreate;

    public Note(String theme, String message, int id,String dateUpdate,String dateCreate) {

        this.theme = theme;
        this.message = message;
        this.dateUpdate = dateUpdate;
        this.dateCreate = dateCreate;
        this.id = id;
    }

    public String getTheme() {
        return this.theme;
    }
    public String getDateUpdate() {
        return this.dateUpdate;
    }
    public String getDateCreate() {
        return this.dateCreate;
    }
    public void setDateUpdate(String dateUpdate) {
        this.dateUpdate = dateUpdate;
    }
    public void setDateCreate(String dateCreate) {
        this.dateCreate = dateCreate;
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
