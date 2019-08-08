package com.app.sociorichapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.app.sociorichapp.R;
import com.app.sociorichapp.app_utils.ConstantMethods;
import com.app.sociorichapp.modals.LoginActivity;

public class SuccessActvity extends BaseActivity {
    private Button goToLogin;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ConstantMethods.setTitleAndBack(this,"Success");
        goToLogin = findViewById(R.id.go_to_login);
        goToLogin.setOnClickListener(v->{
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_success;
    }
}
