package com.example.moveon.model;

import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;

public class MoveOn {
    private String titulo;
    private String comentario;
    private String tipoacess;
    private String classific;
    private double latitude;
    private double longitude;
    private String id;
    private String image;
    private String updatedAt;

    public String getCreateadAt() {
        return updatedAt;
    }

    public void setCreateadAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public MoveOn(String titulo, String comentario, String tipoacess, String classific, double latitude, double longitude, String image) {
        this.titulo = titulo;
        this.comentario = comentario;
        this.tipoacess = tipoacess;
        this.classific = classific;
        this.latitude = latitude;
        this.longitude = longitude;
        this.image = image;
    }

    public MoveOn(String titulo, String classific, String createadAt, String image) {
        this.titulo = titulo;
        this.classific = classific;
        this.updatedAt = createadAt;
        this.image = image;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getTipoacess() {
        return tipoacess;
    }

    public void setTipoacess(String tipoacess) {
        this.tipoacess = tipoacess;
    }

    public String getClassific() {
        return classific;
    }

    public void setClassific(String classific) {
        this.classific = classific;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
