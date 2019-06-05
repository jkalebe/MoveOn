package com.example.moveon;

public class ScreenItem {
    String title, description;
    int ScreenImage;


    public ScreenItem(String title, String description, int ScrennImage) {
        this.title = title;
        this.description = description;
        this.ScreenImage = ScrennImage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitulo(String titulo) {
        this.title = titulo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getScreenImage() {
        return ScreenImage;
    }

    public void setScreenImage(int screenImage) {
        ScreenImage = screenImage;
    }
}
