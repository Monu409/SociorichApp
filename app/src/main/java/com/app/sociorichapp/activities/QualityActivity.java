package com.app.sociorichapp.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.EditText;
import android.widget.LinearLayout;
import com.app.sociorichapp.R;
import com.app.sociorichapp.adapters.QualityAdapter;
import com.app.sociorichapp.app_utils.ConstantMethods;
import java.util.ArrayList;
import java.util.List;

public class QualityActivity extends BaseActivity {
    RecyclerView qltyList;
    LinearLayout addLay;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ConstantMethods.setTitleAndBack(this,"Qualities");
        addLay = findViewById(R.id.add_lay);
        qltyList = findViewById(R.id.qulity_list);
        qltyList.setLayoutManager(new LinearLayoutManager(this));
//        List<String> qltyStrList = ConstantMethods.getArrayListShared(QualityActivity.this,"quality_key");
//        if(qltyStrList==null){
//            qltyStrList = new ArrayList<>();
//        }
//        QualityAdapter qualityAdapter = new QualityAdapter(qltyStrList,this);
//        qltyList.setAdapter(qualityAdapter);
//        addLay.setOnClickListener(a->showEdittext());
    }

    @Override
    protected void onResume() {
        super.onResume();
        List<String> qltyStrList = ConstantMethods.getArrayListShared(QualityActivity.this,"quality_key");
        if(qltyStrList==null){
            qltyStrList = new ArrayList<>();
        }
        QualityAdapter qualityAdapter = new QualityAdapter(qltyStrList,this);
        qltyList.setAdapter(qualityAdapter);
        addLay.setOnClickListener(a->showEdittext());
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_quality;
    }

    private void showEdittext(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final EditText edittext = new EditText(this);
        alert.setMessage("You can add upto 3 qualities");
        alert.setTitle("Add more qualities");

        alert.setView(edittext);

        alert.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String YouEditTextStr = edittext.getText().toString();
                List<String> dataList = ConstantMethods.getArrayListShared(QualityActivity.this,"quality_key");
                if(dataList==null){
                    dataList = new ArrayList<>();
                }
                dataList.add(YouEditTextStr);
                ConstantMethods.saveArrayListShared(dataList,QualityActivity.this,"quality_key");
            }
        });

        alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });

        alert.show();
    }
}
