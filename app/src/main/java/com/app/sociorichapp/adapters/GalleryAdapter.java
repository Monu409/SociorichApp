package com.app.sociorichapp.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.app.sociorichapp.R;
import com.app.sociorichapp.activities.FullImageActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.GalleryHolder> {
    private List<String> imgList;
    private Context context;
    private LayoutInflater layoutInflater;

    public GalleryAdapter(List<String> imgList, Context context){
        this.imgList = imgList;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }
    @NonNull
    @Override
    public GalleryHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = layoutInflater.inflate(R.layout.adapter_gallery,null);
        return new GalleryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryHolder galleryHolder, int i) {
//        Glide.with(context).load(imgList.get(i)).into(galleryHolder.gImage);
        Glide.with(context)
                .load(imgList.get(i))
                .placeholder(R.drawable.logo_rounded)
                .error(R.drawable.logo_rounded)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH)
                .into(galleryHolder.gImage);
        galleryHolder.gImage.setOnClickListener(gg->{
            Intent intent = new Intent(context, FullImageActivity.class);
            ArrayList<String> stringArrayList = (ArrayList<String>)imgList;
            intent.putExtra("all_images",stringArrayList);
            intent.putExtra("header_name","Gallery Images");
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return imgList.size();
    }

    class GalleryHolder extends RecyclerView.ViewHolder {
        public ImageView gImage;
        public GalleryHolder(@NonNull View itemView) {
            super(itemView);
            gImage = itemView.findViewById(R.id.image_view);
        }
    }

}