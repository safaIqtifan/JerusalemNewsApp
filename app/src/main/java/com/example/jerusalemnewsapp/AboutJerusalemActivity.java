package com.example.jerusalemnewsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.jerusalemnewsapp.Adapter.PostsAdapter;
import com.example.jerusalemnewsapp.Model.PostModel;
import com.example.jerusalemnewsapp.rclass.Constant;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AboutJerusalemActivity extends BaseActivity {

    ProgressBar loadingLY;
    RecyclerView rv;
    SwipeRefreshLayout swipeRefreshLY;
    FloatingActionButton addPostBtn;
    List<PostModel> postModelsList;
    PostsAdapter adapter;

    SharedPreferences.Editor editor;
    SharedPreferences app_preferences;
    int appTheme;
    int appColor;

    FirebaseFirestore fireStoreDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        app_preferences = PreferenceManager.getDefaultSharedPreferences(this);
        appColor = app_preferences.getInt("color", 0);
        appTheme = app_preferences.getInt("theme", 0);

        if (appTheme == 0) {
            setTheme(Constant.theme);
        } else {
            setTheme(appTheme);
        }
        setContentView(R.layout.activity_about_jerusalem);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(appColor);

        loadingLY = findViewById(R.id.loadingLY);
        swipeRefreshLY = findViewById(R.id.swipeToRefreshLY);
        rv = findViewById(R.id.rv);
        addPostBtn = findViewById(R.id.addPostBtn);

        fireStoreDB = FirebaseFirestore.getInstance();

        rv.setLayoutManager(new LinearLayoutManager(this));

        postModelsList = new ArrayList<>();

        adapter = new PostsAdapter(this, postModelsList);
        rv.setAdapter(adapter);

        swipeRefreshLY.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchData(false);
            }
        });

        addPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AboutJerusalemActivity.this, AddPostActivity.class));

            }
        });

        fetchData(true);


    }

    public void fetchData(boolean showLoading) {

        if (showLoading) {
            loadingLY.setVisibility(View.VISIBLE);
            swipeRefreshLY.setVisibility(View.GONE);
        }
        fireStoreDB.collection(Constant.POST).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                loadingLY.setVisibility(View.GONE);
                swipeRefreshLY.setRefreshing(false);
                if (task.isSuccessful()) {
                    swipeRefreshLY.setVisibility(View.VISIBLE);
                    postModelsList.clear();

                    for (DocumentSnapshot document : task.getResult().getDocuments()) {
                        PostModel postModel = document.toObject(PostModel.class);
                        postModelsList.add(postModel);
                    }
                    adapter.list = postModelsList;
                    adapter.notifyDataSetChanged();

                } else {
                    Toast.makeText(AboutJerusalemActivity.this, getString(R.string.fail_get_data), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_video) {
            startActivity(new Intent(AboutJerusalemActivity.this, VideoActivity.class));
//            getSupportFragmentManager().beginTransaction().replace(R.id.container,new VideoFragment()).commit();
            return true;
        }

        if (id == R.id.action_about_jerusalem) {
            startActivity(new Intent(AboutJerusalemActivity.this, AboutJerusalemActivity.class));
            return true;
        }

        if (id == R.id.action_settings) {
            startActivity(new Intent(AboutJerusalemActivity.this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}