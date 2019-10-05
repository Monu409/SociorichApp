package com.sociorich.app.activities;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sociorich.app.R;
import com.sociorich.app.adapters.DeleteIntrestAdapter;
import com.sociorich.app.app_utils.ConstantMethods;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class DeleteIntrestActivity extends BaseActivity {
    private RecyclerView dltIntrsrtRcyl;
    List<String> selectedIntrst = new ArrayList<>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ConstantMethods.setTitleAndBack(this,"Delete Interest");
        dltIntrsrtRcyl = findViewById(R.id.dlt_intrst_rcyl);
        dltIntrsrtRcyl.setLayoutManager(new LinearLayoutManager(this));
        String jsonCatArr = getIntent().getStringExtra("cat_arr");
        List<String> keyCat = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(jsonCatArr);
            for(int i=0;i<jsonArray.length();i++){
                String catId = jsonArray.getString(i);
                keyCat.add(catId);
                String catName = DashboardActivity.catMap.get(catId);
                selectedIntrst.add(catName);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        DeleteIntrestAdapter deleteIntrestAdapter = new DeleteIntrestAdapter(selectedIntrst,keyCat,this);
        dltIntrsrtRcyl.setAdapter(deleteIntrestAdapter);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_delete_intrest;
    }
}
