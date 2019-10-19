package com.sociorich.app.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.sociorich.app.R;
import com.sociorich.app.testpac.MasonryAdapter;
import com.sociorich.app.testpac.SpacesItemDecoration;

public class Main extends AppCompatActivity {

    Toolbar mToolbar;
    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);

//        mToolbar = (Toolbar) findViewById(R.id.toolbar);
//
//        if (mToolbar != null) {
//            setSupportActionBar(mToolbar);
//            getSupportActionBar().setTitle("Staggered Grid");
//        }

        mRecyclerView = (RecyclerView) findViewById(R.id.masonry_grid);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        MasonryAdapter adapter = new MasonryAdapter(this);
        mRecyclerView.setAdapter(adapter);
        SpacesItemDecoration decoration = new SpacesItemDecoration(16);
        mRecyclerView.addItemDecoration(decoration);

    }

}