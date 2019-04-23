package com.romullocordeiro.wallpaperparadise;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.romullocordeiro.wallpaperparadise.DAO.ImageSenderHandler;

import java.net.URISyntaxException;


public class UploadActivity extends AppCompatActivity {


    public static final int PICK_IMAGE = 10;
    ImageSenderHandler ish = new ImageSenderHandler();


    private boolean imagePicked = false;
    Uri imageUri = null;

    Button pickImageBt;
    Button submitBt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_image);

        pickImageBt = (Button)findViewById(R.id.pickImageBt);
        pickImageBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select Picture"),PICK_IMAGE);

            }
        });

        submitBt = (Button)findViewById(R.id.uploadSubmitButton);
        submitBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(imagePicked){
                    ish.execute(imageUri);
                    imagePicked = false;
                }else{
                    Toast.makeText(UploadActivity.this, "Por favor selecione uma imagem", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == PICK_IMAGE) {
            if(resultCode == Activity.RESULT_OK){
                    imageUri = data.getData();
                    ImageView viewTest = (ImageView)findViewById(R.id.imageTest);
                    viewTest.setImageURI(imageUri);
                    imagePicked = true;
                    Log.d("meuLog", "Pegou imagem: " + data);
                try {
                    Log.d("meuLog", "PathFromUri: " + MainActivity.getFilePath(MainActivity.context,imageUri));
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }else{
                Toast.makeText(this, "Erro ao selecionar imagem", Toast.LENGTH_SHORT).show();
            }
        }
    }



}
