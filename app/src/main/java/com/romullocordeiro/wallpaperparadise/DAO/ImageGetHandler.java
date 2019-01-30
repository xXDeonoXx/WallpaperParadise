package com.romullocordeiro.wallpaperparadise.DAO;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

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
import java.util.List;

public class ImageGetHandler extends AsyncTask<String, String, List<Image>> {

    ListaActivity ref;

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


    @Override
    protected List<Image> doInBackground(String... params) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {

            //esse primeiro bloco cuida de receber o JSON da URI da API e preparar ele pra uso
            URL url = new URL("http://192.168.0.109:8080/api/"+params[0]);
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
                    Bitmap imgBitmap = null;
                    String s1 = jsonArray.getJSONObject(i).getString("reference").toString();
                    URL url1 = new URL(s1);
                    imgBitmap = BitmapFactory.decodeStream(url1.openConnection().getInputStream());
                    Image img = new Image(
                            jsonArray.getJSONObject(i).getInt("id"),
                            jsonArray.getJSONObject(i).getString("name"),
                            jsonArray.getJSONObject(i).getString("uploader"),
                            jsonArray.getJSONObject(i).getString("tag"),
                            jsonArray.getJSONObject(i).getString("reference"),
                            imgBitmap
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

        for(int i = 0; i < imgList.size(); i++){
            Log.d("imgList Values: ", imgList.get(i).getName());
        }

        ref.startImageArray(imgList);
    }
}
