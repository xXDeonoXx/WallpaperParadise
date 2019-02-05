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
            RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(getBaseContext(),imgList);
            recyclerView.setAdapter(recyclerViewAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }catch (NullPointerException e){
            Toast.makeText(this, "Houve um erro com o recebimento das informações", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this, "Permissão garantida, tente salvar o Wallpaper novamente", Toast.LENGTH_SHORT).show();
        }
    }


    private void setWallpaper(final int index){


        class setWallpaperClass extends AsyncTask<Bitmap, Integer, Integer>{

            @Override
            protected Integer doInBackground(Bitmap... bitmaps) {
                WallpaperManager wm = WallpaperManager.getInstance(ListaActivity.this);
                try {
                    wm.setBitmap(bitmaps[0]);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }


        //todo preciso reimplementar essa função depois de implementar RecyclerView

        AlertDialog.Builder builder = new AlertDialog.Builder(ListaActivity.this);
        builder.setCancelable(true);
        builder.setTitle("O que deseja fazer ?");
        builder.setMessage("O que dejesa fazer com a imagem '" + imgList.get(index).getName() + "' ?");
        builder.setNeutralButton("Definir como papel de parede",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            setWallpaperClass swc = new setWallpaperClass();
                            Toast.makeText(getApplicationContext(), "Alterando papel de parede...", Toast.LENGTH_SHORT).show();

                            swc.execute(imgList.get(index).getImg());
                            Toast.makeText(getApplicationContext(), "Papel de parede alterado ＼(＾▽＾)／", Toast.LENGTH_LONG).show();
                        }catch (Exception e){
                            Toast.makeText(getApplicationContext(),
                                    "Falha ao definir Papel de parede (｡╯︵╰｡)", Toast.LENGTH_SHORT).show();
                            Log.d("Falha set WP",e.getMessage());
                        }
                    }
                });
        builder.setPositiveButton("Salvar no dispositivo",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            saveImageToDevice(imgList.get(index));
                            Toast.makeText(ListaActivity.this, "Imagem salva na galeria (─‿‿─)", Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            Toast.makeText(ListaActivity.this, "Ocorreu um erro ｡･ﾟﾟ*(>д<)*ﾟﾟ･｡", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                            Log.d("erro ao salvar", e.getMessage());
                        }
                    }
                });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(ListaActivity.this, "Ação cancelada", Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    public void saveImageToDevice(Image image) throws IOException {

        //todo mudar o funcionamento dessa função quando RecyclerView for implementado

        if (Build.VERSION.SDK_INT >= 23 && checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }else{
            Bitmap bitmap = image.getImg();
            MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, image.getName() , "Wallpaper de WallpaperParadise");
        }



    }


}
