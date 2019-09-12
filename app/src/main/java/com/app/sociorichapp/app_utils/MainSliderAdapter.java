//package com.app.sociorichapp.app_utils;
//
//import java.util.List;
//
//import ss.com.bannerslider.adapters.SliderAdapter;
//import ss.com.bannerslider.viewholder.ImageSlideViewHolder;
//
//public class MainSliderAdapter extends SliderAdapter {
//    private List<String> urlList;
//
//    public MainSliderAdapter(List<String> urlList){
//        this.urlList = urlList;
//    }
//
//    @Override
//    public int getItemCount() {
//        return urlList.size();
//    }
//
//    @Override
//    public void onBindImageSlide(int position, ImageSlideViewHolder viewHolder) {
//        for(int i=0;i<urlList.size();i++){
//            viewHolder.bindImageSlide(urlList.get(position));
//        }
//    }
//}