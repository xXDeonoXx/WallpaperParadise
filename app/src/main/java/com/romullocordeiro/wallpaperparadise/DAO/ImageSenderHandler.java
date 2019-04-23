package com.romullocordeiro.wallpaperparadise.DAO;


import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import com.romullocordeiro.wallpaperparadise.MainActivity;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;
import okhttp3.Authenticator;
import okhttp3.Credentials;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.Route;

public class ImageSenderHandler extends AsyncTask<Uri, Integer, String> {

    final String UploadServerUrl = "https://romullo-image-provider.herokuapp.com/api/upload";


    @Override
    protected String doInBackground(Uri... strings) {

        Uri imageUri = strings[0];
        String username = "deonocf";
        String password = "5Urr0g4t35";

        File imageFile = null;
        try {
            imageFile = new File(MainActivity.getFilePath(MainActivity.context, imageUri));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }


        //fim preparando imagem

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(180, TimeUnit.SECONDS)
                .readTimeout(180, TimeUnit.SECONDS)
                .authenticator(new Authenticator(){
                    @Override
                    public Request authenticate(Route route, Response response) throws IOException {
                        String credential = Credentials.basic(username, password);
                        return response.request().newBuilder().header("Authorization", credential).build();
                    }
                }).build();


        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("file", "file.png", RequestBody.create(MediaType.parse("image/png"),imageFile))
                .addFormDataPart("name","Praia do forte")
                .addFormDataPart("uploader", "Ademir")
                .addFormDataPart("tag", "Paisagem, Praia, Céu")
                .build();
        Request request = new Request.Builder().url(UploadServerUrl).post(body).build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
            String result = response.body().toString();
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("ImageGetHandler", "deu merda cara");
        }

        return null;
    }
}
