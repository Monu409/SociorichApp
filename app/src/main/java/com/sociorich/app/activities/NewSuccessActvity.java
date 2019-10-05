package com.sociorich.app.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import com.sociorich.app.R;


public class NewSuccessActvity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_success_actvity);
    }
    public void update(View arg){
        Intent intent = new Intent(NewSuccessActvity.this, LoginActivity.class);

        startActivity(intent);
    }
}
