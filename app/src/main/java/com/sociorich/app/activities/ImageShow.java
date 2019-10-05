package com.sociorich.app.activities;

import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.sociorich.app.R;
import com.bumptech.glide.Glide;

public class ImageShow extends AppCompatActivity {
            ImageView img1,img2,img3,img4;
            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.test);
                Display display = getWindowManager().getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
                int width = size.x;
                int sst=width-20;
                Log.d("Width is",String.valueOf(sst));
                Log.d("Width is",String.valueOf(width));
                int i=2;
                String imgUrl = "https://s3.ap-south-1.amazonaws.com/srch-dev-media/c771bf42-00bf-4828-81f5-0370deb19f41/c384419a-9efd-4299-a008-6748df91dfcd.jpg";
                FrameLayout frameLayout=new FrameLayout(this);
                if(i==1)
                {    ImageView imageView = new ImageView(this);
                    Glide.with(this).load(imgUrl).into(imageView);
                    imageView.setPadding(0,5,0,0);
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                    imageView.setLayoutParams(new FrameLayout.LayoutParams(width,400));
                    frameLayout.addView(imageView);
                }
                if(i==2)
                {    ImageView imageView = new ImageView(this);
                    Glide.with(this).load(imgUrl).into(imageView);
                    imageView.setPadding(0,5,0,0);
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                    imageView.setLayoutParams(new FrameLayout.LayoutParams(width/2,
                            400));
                    ImageView imageView1 = new ImageView(this);
                    Glide.with(this).load(imgUrl).into(imageView1);
                    imageView1.setScaleType(ImageView.ScaleType.FIT_XY);
                    imageView1.setLayoutParams(new FrameLayout.LayoutParams(width/2,
                            400));
                    imageView1.setX(width/2);
                    imageView1.setPadding(5,5,0,0);
                    frameLayout.addView(imageView);
                    frameLayout.addView(imageView1);
                }
                if(i==3)
                {
                    ImageView imageView = new ImageView(this);
                    Glide.with(this).load(imgUrl).into(imageView);
                    imageView.setPadding(0,5,0,0);
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                    imageView.setLayoutParams(new FrameLayout.LayoutParams(width/2,
                            400));
                    ImageView imageView1 = new ImageView(this);
                    Glide.with(this).load(imgUrl).into(imageView);
                    imageView1.setX(width/2);
                    imageView1.setPadding(5,5,0,0);
                    imageView1.setScaleType(ImageView.ScaleType.FIT_XY);
                    imageView1.setLayoutParams(new FrameLayout.LayoutParams(width/2,
                            200));
                    ImageView imageView2 = new ImageView(this);
                    Glide.with(this).load(imgUrl).into(imageView);
                    imageView2.setX(width/2);
                    imageView2.setY(200);
                    imageView2.setPadding(5,5,0,0);
                    imageView2.setScaleType(ImageView.ScaleType.FIT_XY);
                    imageView2.setLayoutParams(new FrameLayout.LayoutParams(width/2,
                            200));
                    frameLayout.addView(imageView);
                    frameLayout.addView(imageView1);
                    frameLayout.addView(imageView2);
                }
                if(i==4)
                {
                    //x=0,y=0
                    ImageView imageView = new ImageView(this);
                    Glide.with(this).load(imgUrl).into(imageView);
                    imageView.setPadding(0,5,0,0);
                    imageView.setLayoutParams(new FrameLayout.LayoutParams(sst/2,400));
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                    //x=200,y==0
                    ImageView imageView1 = new ImageView(this);
                    Glide.with(this).load(imgUrl).into(imageView1);
                    imageView1.setX(sst/2);
                    imageView1.setPadding(2,5,0,0);
                    imageView1.setLayoutParams(new FrameLayout.LayoutParams(sst/2,400/3));
                    imageView1.setScaleType(ImageView.ScaleType.FIT_XY);

                    ImageView imageView2 = new ImageView(this);
                    Glide.with(this).load(imgUrl).into(imageView2);
                    imageView2.setX(sst/2);
                    imageView2.setY(400/3);
                    imageView2.setPadding(2,5,0,0);
                    imageView2.setLayoutParams(new FrameLayout.LayoutParams(sst/2,400/3));
                    imageView2.setScaleType(ImageView.ScaleType.FIT_XY);

                    ImageView imageView3= new ImageView(this);
                    Glide.with(this).load(imgUrl).into(imageView3);
                    imageView3.setX(sst/2);
                    imageView3.setY((400/3+400/3));
                    imageView3.setPadding(2,5,0,0);
                    imageView3.setLayoutParams(new FrameLayout.LayoutParams(sst/2,400/3));
                    imageView3.setScaleType(ImageView.ScaleType.FIT_XY);


                    TextView textView=new TextView(this);
                    textView.setText("+ 6");
                    textView.setX(sst/2);
                    textView.setTextColor(Color.parseColor("#ffffff"));
                    textView.setTypeface(null, Typeface.BOLD);
                    textView.setY(400/3+400/3);
                    textView.setGravity(Gravity.CENTER);
                    textView.setTextSize(20);
                    textView.setLayoutParams(new FrameLayout.LayoutParams(sst/2,400/3));
                    frameLayout.addView(imageView);
                    frameLayout.addView(imageView1);
                    frameLayout.addView(imageView2);
                    frameLayout.addView(imageView3);
                    frameLayout.addView(textView);
                }

            }
        }