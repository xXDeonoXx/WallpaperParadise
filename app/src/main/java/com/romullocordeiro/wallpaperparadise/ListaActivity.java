package com.romullocordeiro.wallpaperparadise;

import android.Manifest;
import android.app.AlertDialog;
import android.app.WallpaperManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.romullocordeiro.wallpaperparadise.DAO.ImageGetHandler;
import com.romullocordeiro.wallpaperparadise.Model.Image;
import com.romullocordeiro.wallpaperparadise.Model.RecyclerViewAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ListaActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //variaveis do DrawerLayout (menuzinho do lado)
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;


    private List<Image> imgList = new ArrayList<Image>();
    private ListaActivity ref = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_wallpapers);

        //Cuidando do DrawerLayout
        mDrawerLayout = (DrawerLayout)findViewById(R.id.listDrawer);
        mToggle = new ActionBarDrawerToggle(this,mDrawerLayout,R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Imagens Recentes");
        initializateMenuItens();



        //iniciando recebimento de imagens e etc
        try {
            ImageGetHandler igh = new ImageGetHandler(ref);
            igh.execute("imagens");
        }catch (Exception e){
            Toast.makeText(ref, "Por favor verifique sua conexão com a internet...", Toast.LENGTH_SHORT).show();
        }

    }

    public void startImageArray(List<Image> imgList){
        //aqui o array com o json é retornado e mandado para o RecyclerViewAdapter
        try{
            //popular recycler view aqui
            RecyclerView recyclerView = findViewById(R.id.recycler_view);
            RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(this,imgList, recyclerView);
            recyclerView.setAdapter(recyclerViewAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            if(imgList.isEmpty()){
                Toast.makeText(this, "0 Resultados encontrados, por favor pesquise por outra palavra chave",
                        Toast.LENGTH_LONG).show();
            }
        }catch (NullPointerException e){
            Toast.makeText(this, "Houve um erro com o recebimento das informações", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    //implementação dos listeners do Menu lateral (Drawer Layout)
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        // Handle navigation view item clicks here.
        switch (menuItem.getItemId()) {

            case R.id.menuItemNovos: {
                ImageGetHandler igh = new ImageGetHandler(ref);
                igh.execute("imagens");
                getSupportActionBar().setTitle("Imagens Recentes");
                break;
            }
            case R.id.menuItemPesquisar: {
                AlertDialog.Builder builder = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_DARK);
                builder.setCancelable(true);
                builder.setTitle("Pesquisar Imagens");
                builder.setMessage("Por qual Tag deseja Buscar ?");

                //textBox do builder
                final EditText input = new EditText(this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                builder.setView(input);


                //Botão Procurar do Dialog
                builder.setPositiveButton("Procurar",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ImageGetHandler igh = new ImageGetHandler(ref);
                                if(input.getText().length() > 0){
                                    igh.execute("imagens/tag/" + input.getText());
                                    getSupportActionBar().setTitle("Imagens sobre " + "\"" + input.getText() + "\"");
                                }else{
                                    Toast.makeText(ref,
                                            "A pesquisa não pode ser vazia",
                                            Toast.LENGTH_SHORT).show();
                                    getSupportActionBar().setTitle("Por favor pesquise novamente...");
                                }
                                
                            }
                        });

                //Botão cancelar do Builder
                builder.setNegativeButton("Cancelar",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getBaseContext(), "Ação cancelada", Toast.LENGTH_SHORT).show();
                            }
                        });
                builder.show();

                break;
            }
            case R.id.menuItemPorUsuarios: {
                Toast.makeText(this, "By Users", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.menuItemLogin: {
                Toast.makeText(this, "Login", Toast.LENGTH_SHORT).show();
                Log.d("Log do menu", "Você clicou em login");
                break;
            }
            case R.id.menuItemUpload: {
                Intent it = new Intent(getBaseContext(), UploadActivity.class);
                startActivity(it);
            }
        }
        //close navigation drawer
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    //inicialização dos listeners do NavigationMenu
    private void initializateMenuItens(){
        NavigationView myNav = (NavigationView)findViewById(R.id.navigation_view);
        myNav.setNavigationItemSelectedListener(this);
    }

}
