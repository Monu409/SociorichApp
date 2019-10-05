package com.sociorich.app.adapters;

import android.content.Context;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;

import com.sociorich.app.R;
import com.sociorich.app.modals.SocoDayModal;
import com.bumptech.glide.Glide;

import java.util.List;


public class SlidingImage_Adapter extends PagerAdapter {


    private List<SocoDayModal> socoDayModals;
    private LayoutInflater inflater;
    private Context context;


    public SlidingImage_Adapter(Context context, List<SocoDayModal> socoDayModals) {
        this.context = context;
        this.socoDayModals = socoDayModals;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return socoDayModals.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout = inflater.inflate(R.layout.slidingimages_layout, view, false);

        assert imageLayout != null;
        final ImageView imageView = imageLayout.findViewById(R.id.image);
        final TextView uNameTxt = imageLayout.findViewById(R.id.username);
        final TextView socoCrdt = imageLayout.findViewById(R.id.soco_crdt);
        final TextView desTxt = imageLayout.findViewById(R.id.desc);
        final TextView catTxt = imageLayout.findViewById(R.id.cat_txt);
        String imgUrl = socoDayModals.get(position).getImgUrl();
        if(imgUrl.equals("")){
            imageView.setImageResource(R.drawable.gallery_blnk);
        }
        else {
            Glide.with(context).load(socoDayModals.get(position).getImgUrl()).into(imageView);
        }
        uNameTxt.setText(socoDayModals.get(position).getuName());
        socoCrdt.setText(socoDayModals.get(position).getSocoCrdt()+" Socio Credit");
        desTxt.setText(socoDayModals.get(position).getDesStr());
        catTxt.setText(socoDayModals.get(position).getCatTxt());

        view.addView(imageLayout, 0);

        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }


}