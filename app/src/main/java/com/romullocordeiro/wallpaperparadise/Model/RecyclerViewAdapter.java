package com.romullocordeiro.wallpaperparadise.Model;

import android.app.AlertDialog;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.romullocordeiro.wallpaperparadise.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";
    private static final String folderName = "/WallpaperParadise";


    private Context mContext;
    private List<Image> imgList;
    private RecyclerView mRecyclerView;

    public RecyclerViewAdapter(Context mContext, List<Image> imgList, RecyclerView recyclerView) {
        this.mContext = mContext;
        this.imgList = imgList;
        this.mRecyclerView = recyclerView;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_listitem, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder1, final int i) {
        Log.d(TAG, "onBindViewHolder: called");

        ViewHolder viewHolder = viewHolder1;


        Glide.with(mContext)
                .asBitmap()
                .load(imgList.get(i).getReference())
                .listener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                        Toast.makeText(mContext, "Falha ao carregar imagem, verifique sua conexão", Toast.LENGTH_SHORT).show();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, Object model, final Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                        final ViewHolder viewHolder = viewHolder1;
                        viewHolder.nome.setText("Nome: " +imgList.get(i).getName());
                        viewHolder.uploader.setText("Uploader: " +imgList.get(i).getUploader());
                        viewHolder.tags.setText("Tags: " +imgList.get(i).getTag());
                        viewHolder.getImageView().getLayoutParams().height = RelativeLayout.LayoutParams.WRAP_CONTENT;
                        viewHolder.getImageView().requestLayout();
                        viewHolder.parentLayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Log.d(TAG, "Clicou no viewHolder: " + i);
                                BitmapDrawable drawable = (BitmapDrawable) viewHolder.getImageView().getDrawable();
                                Bitmap img = drawable.getBitmap();
                                setOrSaveWallpaper(img, i);
                            }
                        });
                        return false;
                    }
                })
                .into(viewHolder.imageView);


    }

    @Override
    public int getItemCount() {
        return imgList.size();
    }




    public class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView imageView;
        private TextView nome;
        private TextView uploader;
        private TextView tags;

        public ImageView getImageView() {
            return imageView;
        }

        public void setImageView(ImageView imageView) {
            this.imageView = imageView;
        }

        public TextView getNome() {
            return nome;
        }

        public void setNome(TextView nome) {
            this.nome = nome;
        }

        public TextView getUploader() {
            return uploader;
        }

        public void setUploader(TextView uploader) {
            this.uploader = uploader;
        }

        public TextView getTags() {
            return tags;
        }

        public void setTags(TextView tags) {
            this.tags = tags;
        }

        public RelativeLayout getParentLayout() {
            return parentLayout;
        }

        public void setParentLayout(RelativeLayout parentLayout) {
            this.parentLayout = parentLayout;
        }

        private RelativeLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            imageView.setImageResource(R.drawable.logo);

            nome = itemView.findViewById(R.id.nameText);

            uploader = itemView.findViewById(R.id.uploaderText);

            tags = itemView.findViewById(R.id.tagsText);

            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }

    private void setOrSaveWallpaper(final Bitmap image, final int index){


        AlertDialog.Builder builder = new AlertDialog.Builder(mContext,AlertDialog.THEME_HOLO_DARK);
        builder.setCancelable(true);
        builder.setTitle("O que deseja fazer ?");
        builder.setMessage("O que deseja fazer com a imagem '" + imgList.get(index).getName() + "' ?");
        builder.setNeutralButton("Definir como papel de parede",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //todo preciso usar o assistente de wallpaper padrão do celular para definir o wallpaper

                        try {

                            //salva a imagem no dispositivo e chama o assistente de wallpaper antivo do celular para definiar o wallpaper
                            Intent intent = new Intent(Intent.ACTION_ATTACH_DATA);
                            intent.addCategory(Intent.CATEGORY_DEFAULT);
                            Uri uriHolder = saveImageToDevice(image, imgList.get(index).getName());
                            intent.setDataAndType(uriHolder, "image/jpeg").addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            intent.putExtra("mimeType", "image/jpeg");
                            mContext.startActivity(Intent.createChooser(intent, "Definir como:"));
                        }catch (Exception e){
                            Toast.makeText(mContext,
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
                            saveImageToDevice(image,imgList.get(index).getName());
                            Toast.makeText(mContext, "Imagem salva na sua galeria", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Toast.makeText(mContext, "Ocorreu um erro ｡･ﾟﾟ*(>д<)*ﾟﾟ･｡", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                            Log.d("erro ao salvar", e.getMessage());
                        }
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    public Uri saveImageToDevice(Bitmap image, String name) throws IOException {


        File direct = new File(Environment.getExternalStorageDirectory() + folderName);
        Uri contentUri = null;

        if (!direct.exists()) {
            File wallpaperDirectory = new File("/sdcard" + folderName);
            wallpaperDirectory.mkdirs();
        }

        File file = new File(new File("/sdcard" + folderName), name + ".jpeg");
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.JPEG, 100, out);

            //o bloco de if abaixo atualiza a galeria
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                Intent mediaScanIntent = new Intent(
                        Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                contentUri = Uri.fromFile(file);
                mediaScanIntent.setData(contentUri);
                mContext.sendBroadcast(mediaScanIntent);
            } else {
                mContext.sendBroadcast(new Intent(
                        Intent.ACTION_MEDIA_MOUNTED,
                        Uri.parse("file://"
                                + Environment.getExternalStorageDirectory())));
            }

            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        contentUri = FileProvider.getUriForFile(mContext,"com.romullocordeiro.wallpaperparadise.fileprovider", file);
        return  contentUri;
    }


}
