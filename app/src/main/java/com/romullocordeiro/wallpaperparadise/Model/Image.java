package com.romullocordeiro.wallpaperparadise.Model;

import android.graphics.Bitmap;

import com.romullocordeiro.wallpaperparadise.DAO.ImageGetHandler;

import java.util.Date;

public class Image {

    private String name;
    private String uploader;
    private String tag;
    private int id;
    private String reference = null;
    private Bitmap img;

    private ImageGetHandler imageControl;

    //construtor vazio
    public Image() {
        name = "indefinido";
        reference = "indefinido";
        tag = "indefinido";
        uploader = "indefinido";
    }



    //construtor completo
    public Image(int id, String name,String uploader,String tag, String reference, Bitmap img) {
        this.id = id;
        this.name = name;
        this.uploader = uploader;
        this.tag = tag;
        this.reference = reference;
        this.img = img;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUploader() {
        return uploader;
    }

    public void setUploader(String uploader) {
        this.uploader = uploader;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public Bitmap getImg() {
        return img;
    }

    public void setImg(Bitmap img) {
        this.img = img;
    }


}
