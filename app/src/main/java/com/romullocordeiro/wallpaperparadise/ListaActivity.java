package com.romullocordeiro.wallpaperparadise;

import android.Manifest;
import android.app.AlertDialog;
import android.app.WallpaperManager;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.romullocordeiro.wallpaperparadise.DAO.ImageGetHandler;
import com.romullocordeiro.wallpaperparadise.Model.Image;
import com.romullocordeiro.wallpaperparadise.Model.RecyclerViewAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ListaActivity extends AppCompatActivity {

    private List<Image> imgList = new ArrayList<Image>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_wallpapers);



        ImageGetHandler igh = new ImageGetHandler(this);
        igh.execute("imagens");


    }

    public void startImageArray(List<Image> imgList){
        //aqui o array com o json é retornado e mandado para o RecyclerViewAdapter
        try{
            //popular recycler view aqui
            RecyclerView recyclerView = findViewById(R.id.recycler_view);
            RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(this,imgList);
            recyclerView.setAdapter(recyclerViewAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }catch (NullPointerException e){
            Toast.makeText(this, "Houve um erro com o recebimento das informações", Toast.LENGTH_SHORT).show();
        }

    }


}
