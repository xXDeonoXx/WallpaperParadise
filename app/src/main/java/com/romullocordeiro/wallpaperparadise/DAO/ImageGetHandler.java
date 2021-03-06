package com.romullocordeiro.wallpaperparadise.DAO;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.romullocordeiro.wallpaperparadise.ListaActivity;
import com.romullocordeiro.wallpaperparadise.Model.Image;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ImageGetHandler extends AsyncTask<String, String, List<Image>> {

    ListaActivity ref;
    final String serverUrl = "https://romullo-image-provider.herokuapp.com/api/";

    public ImageGetHandler (ListaActivity ref){
        this.ref = ref;
    }

    /*
    Essa classe funciona da seguinte maneira...
    Ela é um AsyncTask que é iniciado como qualquer outro, os parametros que ela deve receber
    tem que ser caminhos de GET definidos da api image_provider criada com o Spring boot
    os caminhos são:
    images = pegar todas as imagens do banco em um JSON
    images/id/value = pegar uma imagem especifica pelo id
    images/tag/value = pegar todas imagens que possuam uma certa tag

    De nada Rômullo do futuro
     */

    //todo preciso criar um metodo que retorno todos os resultados que possuam uma certa TAG, ja implementado na api
    //caminho = imagens/tag/tagAqui

    @Override
    protected List<Image> doInBackground(String... params) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {
            //esse primeiro bloco cuida de receber o JSON da URI da API e preparar ele pra uso
            URL url = new URL(serverUrl + params[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream stream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(stream));
            StringBuffer buffer = new StringBuffer();
            String s = buffer.toString();
            Log.d("buffer to string", s);
            String line = "";
            while ((line = reader.readLine()) != null) {
                buffer.append(line+"\n");
                Log.d("Response: ", "> " + line);   //here u ll get whole response...... :-)
            }

            s = buffer.toString();

            //esse segundo bloco uma lista de Image é populada com o json

            try {
                JSONArray jsonArray = new JSONArray(s);
                List<Image> imgList = new ArrayList<Image>();
                for(int i = 0; i < jsonArray.length(); i++){
                    Image img = new Image(
                            jsonArray.getJSONObject(i).getInt("id"),
                            jsonArray.getJSONObject(i).getString("name"),
                            jsonArray.getJSONObject(i).getString("uploader"),
                            jsonArray.getJSONObject(i).getString("tag"),
                            jsonArray.getJSONObject(i).getString("reference"),
                            null
                    );
                    imgList.add(img);
                    Log.d("List Check", "Adicionou");
                }
                //String id, String name,String uploader,String tag, String reference
                return imgList;

            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute (List<Image> imgList){
        List<Image> holder = imgList;
        try{
            Collections.reverse(holder);
            ref.startImageArray(holder);
        }catch (Exception e){
            Toast.makeText(ref, "Falha ao baixar imagens, verifique sua internet e tenta novamente", Toast.LENGTH_SHORT).show();
        }
    }
}
