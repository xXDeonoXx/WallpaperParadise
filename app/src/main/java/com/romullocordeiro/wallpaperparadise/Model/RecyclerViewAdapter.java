package com.romullocordeiro.wallpaperparadise.Model;

import android.content.Context;
import android.support.annotation.NonNull;
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
import com.romullocordeiro.wallpaperparadise.R;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";

    private Context mContext;
    private List<Image> imgList;

    public RecyclerViewAdapter(Context mContext, List<Image> imgList) {
        this.mContext = mContext;
        this.imgList = imgList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_listitem, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        Log.d(TAG, "onBindViewHolder: called");

        Glide.with(mContext)
                .asBitmap()
                .load(imgList.get(i).getReference())
                .into(viewHolder.imageView);

        viewHolder.nome.setText(imgList.get(i).getName());
        viewHolder.uploader.setText(imgList.get(i).getUploader());
        viewHolder.tags.setText(imgList.get(i).getTag());

        viewHolder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Clicou no viewHolder: " + i);
                Toast.makeText(mContext, i, Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return imgList.size();
    }




    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView nome;
        TextView uploader;
        TextView tags;
        RelativeLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            nome = itemView.findViewById(R.id.nameText);
            uploader = itemView.findViewById(R.id.uploaderText);
            tags = itemView.findViewById(R.id.tagsText);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }

}
