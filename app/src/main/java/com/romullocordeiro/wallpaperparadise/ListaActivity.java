package com.romullocordeiro.wallpaperparadise;

import android.Manifest;
import android.app.AlertDialog;
import android.app.WallpaperManager;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.romullocordeiro.wallpaperparadise.DAO.ImageGetHandler;
import com.romullocordeiro.wallpaperparadise.Model.Image;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ListaActivity extends AppCompatActivity {



    //inicio da classe com AsyncTask que vai setar o wallpaper, é usada na função setWallpaper

    public class setWallpaperClass extends AsyncTask<Bitmap, Integer, Integer>{

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
    //fim










    private ImageView[] myImgViews = new ImageView[5];
    private Image[] myImagesModels = new Image[5];
    private ScrollView myScrollView;
    private Button btBack;
    private Button btNext;

    private int keyId = 1;
    //TODO esse sistema de keyId devera ser substituido por um get de ids aleatórias do banco de dados quando for implementado

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_wallpapers);

        myImgViews[0] = (ImageView)findViewById(R.id.imageView1);
        myImgViews[0].setClickable(true);
        myImgViews[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setWallpaper(0);
            }
        });

        myImgViews[1] = (ImageView)findViewById(R.id.imageView2);
        myImgViews[1].setClickable(true);
        myImgViews[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setWallpaper(1);
            }
        });

        myImgViews[2] = (ImageView)findViewById(R.id.imageView3);
        myImgViews[2].setClickable(true);
        myImgViews[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setWallpaper(2);
            }
        });

        myImgViews[3] = (ImageView)findViewById(R.id.imageView4);
        myImgViews[3].setClickable(true);
        myImgViews[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setWallpaper(3);
            }
        });

        myImgViews[4] = (ImageView)findViewById(R.id.imageView5);
        myImgViews[4].setClickable(true);
        myImgViews[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setWallpaper(4);
            }
        });

        myScrollView = (ScrollView)findViewById(R.id.scrollViewWithImages);//usado para dar scroll pro topo

        btBack = (Button)findViewById(R.id.btBack);
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                keyId -= myImgViews.length * 2;
                populateViews();
            }
        });

        btNext = (Button)findViewById(R.id.btNext);
        btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                populateViews();
            }
        });

        populateViews();


    }

    public void populateViews(){
        for(int i = 0; i < myImgViews.length; i++){
            Log.d("KeyId","Valor da KeyId: " + keyId);
            //todo na linha abaixo eu preciso dar set em uma imagem de "carregando"

            myImgViews[i].setImageResource(R.drawable.ic_launcher_background);
            myScrollView.smoothScrollTo(0,0);
            ImageGetHandler igh = new ImageGetHandler(i,this);
            igh.execute("" + (keyId));
            keyId += 1;
        }
        //myScrollView.smoothScrollTo(0,0);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this, "Permissão garantida, tente salvar o Wallpaper novamente", Toast.LENGTH_SHORT).show();
        }
    }

    public void setViewImage(int index, Image img){
        myImgViews[index].setImageBitmap(img.getImg());
        myImagesModels[index] = img;
    }

    private void setWallpaper(final int index){



        AlertDialog.Builder builder = new AlertDialog.Builder(ListaActivity.this);
        builder.setCancelable(true);
        builder.setTitle("O que deseja fazer ?");
        builder.setMessage("O que dejesa fazer com a imagem '" + myImagesModels[index].getName() + "' ?");
        builder.setNeutralButton("Definir como papel de parede",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            //TODO criei uma classe com asynctask la em cima, tenho q setar o wallpaper por ela
                            setWallpaperClass swc = new setWallpaperClass();
                            Toast.makeText(getApplicationContext(), "Alterando papel de parede...", Toast.LENGTH_SHORT).show();
                            swc.execute(myImagesModels[index].getImg());
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
                            saveImageToDevice(myImagesModels[index]);
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

        if (Build.VERSION.SDK_INT >= 23 && checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }else{
            Bitmap bitmap = image.getImg();
            MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, image.getName() , "Wallpaper de WallpaperParadise");
        }



    }


}
