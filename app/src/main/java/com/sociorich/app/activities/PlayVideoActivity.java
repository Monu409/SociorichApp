package com.sociorich.app.activities;

import android.os.Bundle;

import com.sociorich.app.R;
import com.sociorich.app.app_utils.ConstantMethods;

import tcking.github.com.giraffeplayer2.VideoView;

public class PlayVideoActivity extends BaseActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ConstantMethods.setTitleAndBack(this,"Media Post");
        String vdoUrl = getIntent().getStringExtra("video_url");
        VideoView videoView = findViewById(R.id.video_view);
        videoView.setVideoPath(vdoUrl).getPlayer().start();
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_play_video;
    }
}
