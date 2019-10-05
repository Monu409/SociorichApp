package com.sociorich.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.sociorich.app.R;
import com.sociorich.app.app_utils.ConstantMethods;

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
