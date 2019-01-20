package com.romullocordeiro.wallpaperparadise.DAO;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.romullocordeiro.wallpaperparadise.ListaActivity;
import com.romullocordeiro.wallpaperparadise.Model.Image;

import java.net.URL;

public class ImageGetHandler extends AsyncTask<String, Integer, Image> {

    ListaActivity ref;
    int viewId;

    public ImageGetHandler (int viewId ,ListaActivity ref){
        this.viewId = viewId;
        this.ref = ref;
    }

    /*
    TODO preciso tratar caso a imagem com o id solicitado não exista ou tenha sido excluido
    provavelmente isso vai ser resolvido quando eu implementar o banco de dados com referencias
    mas ja fica aqui o aviso romullo do futuro, esse pepino é seu
    */


    @Override
    protected Image doInBackground(String... params) {
        Bitmap img = null;//vai receber a imagem da url
        String s = "";//usada pra segurar a string da url
        Image image = new Image();
        try {//tenta pegar uma imagem png, se n for ou falhar, vai pro catch
            s = "https://romulloimagedatabase.000webhostapp.com/Imagens/" + params[0] + ".png";
            URL url = new URL(s);
            img = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            image.setImg(img);
        } catch (Exception e) {
            e.printStackTrace();
            try {//tenta pegar uma imagem jpg, se n for ou falhar, vai pro catch
                s = "https://romulloimagedatabase.000webhostapp.com/Imagens/" + params[0] + ".jpg";
                URL url = new URL(s);
                img = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                image.setImg(img);
            } catch (Exception e1) {
                e.printStackTrace();
            }
        }

        return image;
    }

    @Override
    protected void onPostExecute (Image img){
        ref.setViewImage(viewId, img);
    }
}
