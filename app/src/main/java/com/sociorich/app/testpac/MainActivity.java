package com.sociorich.app.testpac;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.sociorich.app.R;
import com.sociorich.app.app_utils.SpannedGridLayoutManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    // ArrayList for person names
    ArrayList personNames = new ArrayList<>(Arrays.asList("Person 1", "Person 2", "Person 3", "Person 4", "Person 5", "Person 6", "Person 7", "Person 8", "Person 9", "Person 10", "Person 11", "Person 12", "Person 13", "Person 14"));
//    ArrayList personImages = new ArrayList<>(Arrays.asList(R.drawable.person1, R.drawable.person2, R.drawable.person3, R.drawable.person4, R.drawable.person5, R.drawable.person6, R.drawable.person7, R.drawable.person1, R.drawable.person2, R.drawable.person3, R.drawable.person4, R.drawable.person5, R.drawable.person6, R.drawable.person7));
    List<String> personImages = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        // get the reference of RecyclerView
        personImages.add("https://s3.ap-south-1.amazonaws.com/sociorich-prod-media/76cf5223-e06e-43db-8a39-f5bfccf45a7a/0e0dcf3e-bbdd-46a3-9da0-97f20235849f.jpg");
        personImages.add("https://s3.ap-south-1.amazonaws.com/sociorich-prod-media/76cf5223-e06e-43db-8a39-f5bfccf45a7a/19395b9b-ef35-401f-a49c-27e4227d7f20.jpg");
        personImages.add("https://s3.ap-south-1.amazonaws.com/sociorich-prod-media/76cf5223-e06e-43db-8a39-f5bfccf45a7a/4162940c-4c79-4cf1-8d86-848a02ed4667.jpg");
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        // set a StaggeredGridLayoutManager with 3 number of columns and vertical orientation
//        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
        SpannedGridLayoutManager manager = new SpannedGridLayoutManager(
                new SpannedGridLayoutManager.GridSpanLookup() {
                    @Override
                    public SpannedGridLayoutManager.SpanInfo getSpanInfo(int position) {
                        // Conditions for 2x2 items
                        if (position % 6 == 0 || position % 6 == 4) {
                            return new SpannedGridLayoutManager.SpanInfo(2, 2);
                        } else {
                            return new SpannedGridLayoutManager.SpanInfo(1, 1);
                        }
                    }
                },
                3, // number of columns
                1f // how big is default item
        );
        recyclerView.setLayoutManager(manager); // set LayoutManager to RecyclerView
        //  call the constructor of CustomAdapter to send the reference and data to Adapter
        CustomAdapter customAdapter = new CustomAdapter(MainActivity.this, personImages);
        recyclerView.setAdapter(customAdapter); // set the Adapter to RecyclerView
    }
}