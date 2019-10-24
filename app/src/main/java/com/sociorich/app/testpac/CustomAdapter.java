package com.sociorich.app.testpac;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.sociorich.app.R;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {
    ArrayList personNames;
    ArrayList personImages;
    private List<String> urls;
    Context context;
    private static final int TYPE_FULL = 0;
    private static final int TYPE_HALF = 1;
    private static final int TYPE_QUARTER = 2;
    public CustomAdapter(Context context, ArrayList personNames, ArrayList personImages) {
        this.context = context;
        this.personNames = personNames;
        this.personImages = personImages;
    }
    public CustomAdapter(Context context, List<String> urls) {
        this.context = context;
        this.urls = urls;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // infalte the item Layout
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_media_post, parent, false);

//        itemView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
//            @Override
//            public boolean onPreDraw() {
//                final int type = viewType;
//                final ViewGroup.LayoutParams lp = itemView.getLayoutParams();
//                if (lp instanceof StaggeredGridLayoutManager.LayoutParams) {
//                    StaggeredGridLayoutManager.LayoutParams sglp =
//                            (StaggeredGridLayoutManager.LayoutParams) lp;
//                    switch (type) {
//                        case TYPE_FULL:
//                            sglp.setFullSpan(true);
//                            break;
//                        case TYPE_HALF:
//                            sglp.setFullSpan(false);
//                            sglp.width = itemView.getWidth() / 2;
//                            break;
//                        case TYPE_QUARTER:
//                            sglp.setFullSpan(false);
//                            sglp.width = itemView.getWidth() / 2;
//                            sglp.height = itemView.getHeight() / 2;
//                            break;
//                    }
//                    itemView.setLayoutParams(sglp);
//                    final StaggeredGridLayoutManager lm =
//                            (StaggeredGridLayoutManager) ((RecyclerView) parent).getLayoutManager();
//                    lm.invalidateSpanAssignments();
//                }
//                itemView.getViewTreeObserver().removeOnPreDrawListener(this);
//                return true;
//            }
//        });

        MyViewHolder vh = new MyViewHolder(itemView); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        // set the data in items
//        holder.name.setText(personNames.get(position));
        Glide.with(context)
                .load(urls.get(position))
                .into(holder.image);
        // implement setOnClickListener event on item view.
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // open another activity on item click
                Intent intent = new Intent(context, SecondActivity.class);
                intent.putExtra("image", urls.get(position)); // put image data in Intent
                context.startActivity(intent); // start Intent
            }
        });
    }

    @Override
    public int getItemCount() {
        return urls.size();
    }

//    @Override
//    public int getItemViewType(int position) {
//        final int modeEight = position % 8;
//        switch (modeEight) {
//            case 0:
//            case 5:
//                return TYPE_HALF;
//            case 1:
//            case 2:
//            case 4:
//            case 6:
//                return TYPE_QUARTER;
//        }
//        return TYPE_FULL;
//    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        // init the item view's
        TextView name;
        ImageView image;
        public MyViewHolder(View itemView) {
            super(itemView);
            // get the reference of item view's
            name  = itemView.findViewById(R.id.name);
            image = itemView.findViewById(R.id.image);
        }
    }
}