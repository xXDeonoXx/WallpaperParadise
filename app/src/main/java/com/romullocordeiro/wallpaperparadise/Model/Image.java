package com.romullocordeiro.wallpaperparadise.Model;

import android.graphics.Bitmap;

import com.romullocordeiro.wallpaperparadise.DAO.ImageGetHandler;

import java.util.Date;

public class Image {

    private String name;
    private Date date;
    private String uploader;
    private String tag;
    private String id;
    private Bitmap img = null;

    private ImageGetHandler imageControl;

    //construtor vazio
    public Image() {
        name = "indefinido";
        uploader = "indefinido";
        tag = "indefinido";
    }
    //construtor completo
    public Image(String name, Date date, String uploader,String tag, String id, Bitmap img) {
        this.name = name;
        this.date = date;
        this.uploader = uploader;
        this.tag = tag;
        this.id = id;
        this.img = img;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getUploader() {
        return uploader;
    }

    public void setUploader(String uploader) {
        this.uploader = uploader;
    }

    public Bitmap getImg() {
        return img;
    }

    public void setImg(Bitmap img) {
        this.img = img;
    }

}
