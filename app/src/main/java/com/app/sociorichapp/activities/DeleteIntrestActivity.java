package com.app.sociorichapp.activities;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.sociorichapp.R;
import com.app.sociorichapp.adapters.DeleteIntrestAdapter;
import com.app.sociorichapp.app_utils.ConstantMethods;

import java.util.List;

public class DeleteIntrestActivity extends BaseActivity {
    private RecyclerView dltIntrsrtRcyl;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ConstantMethods.setTitleAndBack(this,"Delete Interest");
        dltIntrsrtRcyl = findViewById(R.id.dlt_intrst_rcyl);
        dltIntrsrtRcyl.setLayoutManager(new LinearLayoutManager(this));
        List<String> selectedIntrst = ConstantMethods.getArrayListShared(this,"interest_save");
        DeleteIntrestAdapter deleteIntrestAdapter = new DeleteIntrestAdapter(selectedIntrst,this);
        dltIntrsrtRcyl.setAdapter(deleteIntrestAdapter);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_delete_intrest;
    }
}
