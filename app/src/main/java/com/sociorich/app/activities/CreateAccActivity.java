package com.sociorich.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sociorich.app.R;
import com.sociorich.app.app_utils.ConstantMethods;

/**
 * Created by PC on 7/1/2019.
 */

public class CreateAccActivity extends BaseActivity {
    private LinearLayout indivisualView,organization_view;
    private TextView loginTxt;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ConstantMethods.setTitleAndBack(this,"Signup");
        indivisualView = findViewById(R.id.indivisual_view);
        organization_view = findViewById(R.id.organization_view);
        loginTxt = findViewById(R.id.login_txt);
        indivisualView.setOnClickListener(v->startActivity(new Intent(this,IndivisualSignupActivity.class)));
        organization_view.setOnClickListener(v->startActivity(new Intent(this,OrganisationAccount.class)));
        loginTxt.setOnClickListener(v->startActivity(new Intent(this, LoginActivity.class)));
        String str1 = "Create your Account As";
        String penalty = " INDIVIDUAL";
        String penalty2 = " ORGANIZATION";


        String strb = "<b><font color='#ef633f'>"+ penalty +"</font></b>";
        String strb2 = "<b><font color='#ef633f'>"+ penalty2 +"</font></b>";
//        String strc = "<u><b><font color='#081137'>"+ promise + "</font></b></u>";
        String strd = str1 +strb;
        String strd2 = str1 +strb2;
        TextView indvTxt = findViewById(R.id.indv_txt);
        TextView orgnTxt = findViewById(R.id.orgn_txt);
        indvTxt.setText(Html.fromHtml(strd));
        orgnTxt.setText(Html.fromHtml(strd2));

        String str11 = "Existing user? ";
        String penalty1 = "Login here";
        String strb21 = "<u><b><font color='#ef633f'>"+ penalty1 +"</font></b></u>";
        String strd1 = str11 +strb21;
        loginTxt.setText(Html.fromHtml(strd1));
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_create_account;
    }
}
