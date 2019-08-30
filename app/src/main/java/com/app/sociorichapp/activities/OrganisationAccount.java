package com.app.sociorichapp.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.app.sociorichapp.R;
import com.app.sociorichapp.app_utils.ConstantMethods;

import java.util.ArrayList;


public class OrganisationAccount extends BaseActivity {
    private RadioGroup radioSexGroup, sector;
    TextView selectvalue;
    static String select_value, org_type, org_value, org_name, org_websitE, org_team, org_incorpo, org_address, orhabout;
    Button button;
    AlertDialog alertDialog1;
    CharSequence[] values = {" Automobile ", " Chemical ", " Construction/Real Estate ", " Education ", " Entertainment ", " Fashion/E-Commerce ", " Food ", " Healthcare ", " IT / Software ", " Others "};
    CharSequence[] values2 = {" Animals ", " Community & Development ", " Disasters ", " Education ", " Discrimination ", " Health ", " Homelessness+Poverty ", " Spiritual ", " Other ", " Environment "};
    CharSequence[] values3 = {" Educational Institutes ", " Government Department ", " Hospitals ", " Other ", " Political Parties ", " Proprietorship/Partnership "};
    AlertDialog dialog;
    EditText org_tvname, org_tvwebsite, org_tvyear, org_tvadress,aboutOrgEdt,orgnRNumEdt;
    Spinner spinner1;
    private TextView alreadyTxt;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ConstantMethods.setTitleAndBack(this, "Organization Signup");
        radioSexGroup = (RadioGroup) findViewById(R.id.radioSex);
        spinner1 = (Spinner) findViewById(R.id.spinner1);
        radioSexGroup = (RadioGroup) findViewById(R.id.radioSex);
        selectvalue = (TextView) findViewById(R.id.selectvalue);
        sector = (RadioGroup) findViewById(R.id.sector);
        org_tvname = findViewById(R.id.org_tvname);
        org_tvwebsite = findViewById(R.id.org_tvwebsite);
        org_tvyear = findViewById(R.id.org_tvyear);
        org_tvadress = findViewById(R.id.org_tvadress);
        aboutOrgEdt = findViewById(R.id.about_edt);
        orgnRNumEdt = findViewById(R.id.orgn_rnumber);
        alreadyTxt = findViewById(R.id.already_txt);

        radioSexGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                RadioButton rb = findViewById(checkedId);
                if (rb.getText().equals("Company")) {
                    CreateAlertDialogWithRadioButtonGroup();
                    org_type = "Company";
                } else if (rb.getText().equals("NGO")) {
                    org_type = "NGO";
                    ngo();
                } else if (rb.getText().equals("Other")) {
                    org_type = "Other";
                    other();
                }
                //textViewChoice.setText("You Selected " + rb.getText());
            }
        });
        alreadyTxt.setOnClickListener(v->startActivity(new Intent(this, LoginActivity.class)));
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_organisation_account;
    }

    public void submitdata(View arg) {
        org_name = org_tvname.getText().toString();
        org_websitE = org_tvwebsite.getText().toString();
        org_incorpo = org_tvyear.getText().toString();
        org_address = org_tvadress.getText().toString();
        String aboutOrStr = aboutOrgEdt.getText().toString();
        String orgnRnum = orgnRNumEdt.getText().toString();
        if (org_name.equals("") || org_websitE.equals("") || org_incorpo.equals("") || org_address.equals("")|| aboutOrStr.equals("")) {
            Toast.makeText(getApplicationContext(), "All field are mandatory", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(OrganisationAccount.this, AdminDetail.class);
            intent.putExtra("orgname", org_name);
            intent.putExtra("prgwebsite", org_websitE);
            intent.putExtra("orgadress", org_address);
            intent.putExtra("orgeyear", org_incorpo);
            intent.putExtra("orgtype", org_type);
            intent.putExtra("orgtypes", select_value);
            intent.putExtra("orgstrenth", org_team);
            intent.putExtra("aboutor_txt", aboutOrStr);
            intent.putExtra("orgn_rnum", orgnRnum);
            startActivity(intent);
        }


    }

    public void CreateAlertDialogWithRadioButtonGroup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(OrganisationAccount.this);
        builder.setTitle("Choose sector*");
        builder.setSingleChoiceItems(values, -1, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int item) {

                switch (item) {
                    case 0:
                        select_value = "Automobile";
                        selectvalue.setText(select_value);
                        break;
                    case 1:
                        select_value = "Chemical";
                        selectvalue.setText(select_value);
                        break;
                    case 2:
                        select_value = "Constructio/Real State";
                        selectvalue.setText(select_value);
                        break;
                    case 3:
                        select_value = "Education";
                        selectvalue.setText(select_value);
                        break;
                    case 4:
                        select_value = "Entertainment";
                        selectvalue.setText(select_value);
                        break;
                    case 5:
                        select_value = "Fashion/E-Commerce";
                        selectvalue.setText(select_value);
                        break;
                    case 6:
                        select_value = "Food";
                        selectvalue.setText(select_value);
                        break;
                    case 7:
                        select_value = "Healthcare";
                        selectvalue.setText(select_value);
                        break;
                    case 8:
                        select_value = "IT Software";
                        selectvalue.setText(select_value);
                        break;
                    case 9:
                        select_value = "Others";
                        selectvalue.setText(select_value);
                        break;
                }
                alertDialog1.dismiss();
            }
        });
        alertDialog1 = builder.create();
        alertDialog1.show();
    }

    public void other() {


        AlertDialog.Builder builder = new AlertDialog.Builder(OrganisationAccount.this);

        builder.setTitle("Choose your Entity  *");

        builder.setSingleChoiceItems(values3, -1, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int item) {

                switch (item) {
                    case 0:
                        select_value = "Educational Institutes";
                        selectvalue.setText(select_value);
                        break;
                    case 1:
                        select_value = "Government Department";
                        selectvalue.setText(select_value);
                        break;
                    case 2:
                        select_value = "Hospitals";
                        selectvalue.setText(select_value);
                        break;
                    case 3:
                        select_value = "Other";
                        selectvalue.setText(select_value);
                        break;
                    case 4:
                        select_value = "Political Parties";
                        selectvalue.setText(select_value);
                        break;
                    case 5:
                        select_value = "Proprietorship/Partnership";
                        selectvalue.setText(select_value);
                        break;


                }
                alertDialog1.dismiss();
            }
        });
        alertDialog1 = builder.create();
        alertDialog1.show();
    }

    public void ngo() {
        final ArrayList seletedItems = new ArrayList();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Causes");
        builder.setMultiChoiceItems(values2, null,
                new DialogInterface.OnMultiChoiceClickListener() {
                    // indexSelected contains the index of item (of which checkbox checked)
                    @Override
                    public void onClick(DialogInterface dialog, int indexSelected,
                                        boolean isChecked) {
                        if (isChecked) {
                            // If the user checked the item, add it to the selected items
                            // write your code when user checked the checkbox
                            seletedItems.add(indexSelected);
                        } else if (seletedItems.contains(indexSelected)) {
                            // Else, if the item is already in the array, remove it
                            // write your code when user Uchecked the checkbox
                            seletedItems.remove(Integer.valueOf(indexSelected));
                        }
                    }
                })
                // Set the action buttons
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //  Your code when user clicked on OK
                        //  You can write the code  to save the selected item here

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //  Your code when user clicked on Cancel

                    }
                });

        dialog = builder.create();//AlertDialog dialog; create like this outside onClick
        dialog.show();
    }
}
