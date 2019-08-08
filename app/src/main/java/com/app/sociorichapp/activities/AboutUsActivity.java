package com.app.sociorichapp.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.app.sociorichapp.R;
import com.app.sociorichapp.app_utils.ConstantMethods;

public class AboutUsActivity extends BaseActivity{
    private WebView aboutWeb;
    private ProgressDialog prDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String titleIs = intent.getStringExtra("title_is");
        ConstantMethods.setTitleAndBack(this,titleIs);
        aboutWeb = findViewById(R.id.about_web);
        aboutWeb.setWebViewClient(new MyWebViewClient());

        String url = intent.getStringExtra("url_is");
        aboutWeb.getSettings().setJavaScriptEnabled(true);
        aboutWeb.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        aboutWeb.loadUrl(url);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_about_us;
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
            prDialog = new ProgressDialog(AboutUsActivity.this);
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
