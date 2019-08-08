package com.app.sociorichapp.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;

public abstract class BaseActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResourceId()); 
    } 
    protected abstract int getLayoutResourceId();

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
} 