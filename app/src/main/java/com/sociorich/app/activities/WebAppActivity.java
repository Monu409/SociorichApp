package com.sociorich.app.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.sociorich.app.R;
import com.sociorich.app.app_utils.ConstantMethods;

public class WebAppActivity extends AppCompatActivity {
    private WebView aboutWeb;
    private ProgressDialog prDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_app);
//        Intent intent = getIntent();
//        String titleIs = intent.getStringExtra("title_is");
//        ConstantMethods.setTitleAndBack(this,titleIs);
        aboutWeb = findViewById(R.id.about_web);
        aboutWeb.setWebViewClient(new MyWebViewClient());

//        String url = intent.getStringExtra("url_is");
        aboutWeb.getSettings().setJavaScriptEnabled(true);
        aboutWeb.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        aboutWeb.loadUrl("https://www.sociorich.com/");
    }


    private class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            prDialog = new ProgressDialog(WebAppActivity.this);
            prDialog.setMessage("Please wait ...");
            prDialog.show();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if(prDialog!=null){
                prDialog.dismiss();
            }
        }
    }
}
