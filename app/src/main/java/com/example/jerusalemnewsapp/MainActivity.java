package com.example.jerusalemnewsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    RecyclerView farmsRV;
    FloatingActionButton addPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        farmsRV = findViewById(R.id.farmsRV);
        addPost = findViewById(R.id.add_post);

        farmsRV.setLayoutManager(new LinearLayoutManager((this)));
        NewsAdapter adapter = new NewsAdapter(this, farmModelList);
        farmsRV.setAdapter(adapter);

        addPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}