package com.example.jerusalemnewsapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.jerusalemnewsapp.Model.PostModel;
import com.example.jerusalemnewsapp.rclass.Constant;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class AddPostActivity extends BaseActivity {

    EditText titleEd, describctionEd;
    RelativeLayout loadingLY;
    ImageView postPhoto;
    Button add;
    Uri postPhotoUri;
    //    String postId;
    String userId;
    PostModel postModel;
    FirebaseFirestore fireStoreDB;
    FirebaseAuth auth;
    StorageReference storageRef;

    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;

    SharedPreferences.Editor editor;
    SharedPreferences app_preferences;
    int appTheme;
    int appColor;

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
        setContentView(R.layout.activity_add_post);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(appColor);


        postPhoto = findViewById(R.id.post_photo);
        titleEd = findViewById(R.id.titleEd);
        describctionEd = findViewById(R.id.describctionEd);
        loadingLY = findViewById(R.id.loadingLY);
        add = findViewById(R.id.add);

        postModel = new PostModel();

        fireStoreDB = FirebaseFirestore.getInstance();
        storageRef = FirebaseStorage.getInstance().getReference();

        postPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    if (getApplicationContext().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_DENIED) {
                        String[] permission = {Manifest.permission.READ_EXTERNAL_STORAGE};
                        requestPermissions(permission, PERMISSION_CODE);

                    } else {
                        pickImageFromGallery();
                    }
                } else {
                    pickImageFromGallery();
                }
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                userId = auth.getUid();
//                addModel.user_id = userId;
                checkData();
            }
        });

    }

    private void pickImageFromGallery() {

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickImageFromGallery();
                } else {
                    Toast.makeText(getApplicationContext(), "Permission denied...!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {

            postPhotoUri = data.getData();
            postPhoto.setImageURI(postPhotoUri);
        }
    }


    private void uploadPhoto(Uri photoUri) {


        StorageReference imgRef = storageRef.child("PostsImages" + "/" + UUID.randomUUID().toString());

        loadingLY.setVisibility(View.VISIBLE);
        UploadTask uploadTask = imgRef.putFile(photoUri);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                exception.printStackTrace();
//                GlobalHelper.hideProgressDialog();
            }
        }).addOnSuccessListener(taskSnapshot -> {

            imgRef.getDownloadUrl().addOnCompleteListener(task -> {
//                if (addModel == null)
//                    addModel = new AddModel();
                postModel.photo = task.getResult().toString();
                System.out.println("Log uploaded url " + postModel.photo);
//                checkData();
                sendPostToFirebase();
            });


        });
    }

    private void checkData() {

        String titleStr = titleEd.getText().toString();
        String describctionStr = describctionEd.getText().toString();

        boolean hasError = false;
        if (postPhotoUri == null) {
            Toast.makeText(this, getString(R.string.please_add_photo), Toast.LENGTH_SHORT).show();
            hasError = true;
        }
        if (titleStr.isEmpty()) {
            titleEd.setError(getString(R.string.invalid_input));
            hasError = true;
        }
        if (describctionStr.isEmpty()) {
            describctionEd.setError(getString(R.string.invalid_input));
            hasError = true;
        }
        if (hasError)
            return;

//        if (addModel == null)
//            addModel = new AddModel();
        postModel.title = titleStr;
        postModel.description = describctionStr;

        uploadPhoto(postPhotoUri);

//        sendpostToFirebase();
    }

    private void sendPostToFirebase() {

        String postId = fireStoreDB.collection(Constant.POST).document().getId(); // this is auto genrat

        fireStoreDB.collection(Constant.POST).document(postId).set(postModel, SetOptions.merge())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        loadingLY.setVisibility(View.GONE);
                        if (task.isSuccessful()) {

                            Toast.makeText(getApplicationContext(), getString(R.string.success_add_post), Toast.LENGTH_SHORT).show();
                            onBackPressed();

                        } else {
                            Toast.makeText(getApplicationContext(), getString(R.string.fail_add_post), Toast.LENGTH_SHORT).show();
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
            startActivity(new Intent(AddPostActivity.this, VideoActivity.class));
//            getSupportFragmentManager().beginTransaction().replace(R.id.container,new VideoFragment()).commit();
            return true;
        }

        if (id == R.id.action_about_jerusalem) {
            startActivity(new Intent(AddPostActivity.this, AboutJerusalemActivity.class));
            return true;
        }

        if (id == R.id.action_settings) {
            startActivity(new Intent(AddPostActivity.this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}