package com.romullocordeiro.wallpaperparadise;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {


    Button btListar;
    Button btEntrar;
    Button btCadastrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btListar = (Button)findViewById(R.id.btListar);
        btListar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getBaseContext(), ListaActivity.class);
                startActivity(intent);
            }
        });

        btEntrar = (Button)findViewById(R.id.btEntrar);
        btEntrar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Toast.makeText(MainActivity.this, "Não implementado", Toast.LENGTH_SHORT).show();
            }
        });

        btCadastrar = (Button)findViewById(R.id.btCadastrar);
        btCadastrar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Toast.makeText(MainActivity.this, "Não implementado", Toast.LENGTH_SHORT).show();
            }
        });

    }


}

