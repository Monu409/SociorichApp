package com.sociorich.app.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sociorich.app.R;
import com.sociorich.app.adapters.QualityAdapter;
import com.sociorich.app.app_utils.ConstantMethods;

import java.util.ArrayList;
import java.util.List;

public class QualityActivity extends BaseActivity {
    RecyclerView qltyList;
    LinearLayout addLay;
    List<String> qltyStrList;
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
        addLay.setOnClickListener(a->{
            if(qltyStrList.size()>=2){
                Toast.makeText(this, "You can add only 3 qualities", Toast.LENGTH_SHORT).show();
            }else {
                showEdittext();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        qltyStrList = ConstantMethods.getArrayListShared(QualityActivity.this,"quality_key");
        if(qltyStrList==null){
            qltyStrList = new ArrayList<>();
        }
        QualityAdapter qualityAdapter = new QualityAdapter(qltyStrList,this);
        qltyList.setAdapter(qualityAdapter);
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
                if(YouEditTextStr.isEmpty()){
                    Toast.makeText(QualityActivity.this, "Please enter quality", Toast.LENGTH_SHORT).show();
                    alert.setCancelable(false);
                }
                else {
                    List<String> dataList = ConstantMethods.getArrayListShared(QualityActivity.this, "quality_key");
                    if (dataList == null) {
                        dataList = new ArrayList<>();
                    }
                    dataList.add(YouEditTextStr);
                    ConstantMethods.saveArrayListShared(dataList, QualityActivity.this, "quality_key");
                    List<String> qltyStrList = ConstantMethods.getArrayListShared(QualityActivity.this, "quality_key");
                    if (qltyStrList == null) {
                        qltyStrList = new ArrayList<>();
                    }
                    QualityAdapter qualityAdapter = new QualityAdapter(qltyStrList, QualityActivity.this);
                    qltyList.setAdapter(qualityAdapter);
                    Toast.makeText(QualityActivity.this, "Successfully Added", Toast.LENGTH_SHORT).show();
                }
            }
        });

        alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });

        alert.show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("test","onpause");
    }
}
