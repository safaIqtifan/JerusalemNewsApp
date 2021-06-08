package com.example.jerusalemnewsapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

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

public class AddPost extends AppCompatActivity {

    EditText titleEd ,describctionEd;
    ImageView farmPhoto;
    Button add ;
    Uri farmPhotoUri;
    FirebaseFirestore fireStoreDB;
    String postId;
    FirebaseAuth auth;
    String userId;
    StorageReference storageRef;
    AddModel addModel;
    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        farmPhoto = findViewById(R.id.post_photo);
        titleEd = findViewById(R.id.titleEd);
        describctionEd = findViewById(R.id.describctionEd);
        add = findViewById(R.id.add);

        fireStoreDB = FirebaseFirestore.getInstance();
        storageRef = FirebaseStorage.getInstance().getReference();

        String titleStr = titleEd.getText().toString();
        String describctionStr = describctionEd.getText().toString();
        addModel = new AddModel();
        addModel.title = titleStr;
        addModel.description = describctionStr;

        farmPhoto.setOnClickListener(new View.OnClickListener() {
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
                uploadPhoto(farmPhotoUri);
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

            farmPhotoUri = data.getData();
            farmPhoto.setImageURI(farmPhotoUri);
        }
    }


    private void uploadPhoto(Uri photoUri) {

        StorageReference imgRef = storageRef.child("PostsImages" + "/" + UUID.randomUUID().toString());

        UploadTask uploadTask = imgRef.putFile(photoUri);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                exception.printStackTrace();
//                GlobalHelper.hideProgressDialog();
            }
        }).addOnSuccessListener(taskSnapshot -> {

            imgRef.getDownloadUrl().addOnCompleteListener(task -> {

                addModel.photo = task.getResult().toString();
                System.out.println("Log uploaded url " + addModel.photo);
                sendpostToFirebase();
            });


        });
    }

    private void sendpostToFirebase() {

        postId = fireStoreDB.collection(Constants.POST).document().getId(); // this is auto genrat

        fireStoreDB.collection(Constants.POST).document(postId).set(addModel, SetOptions.merge())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {

                    //sendCategoriesToFirebase();
                    startActivity(new Intent(AddPost.this, MainActivity.class));

                } else {
//                    GlobalHelper.hideProgressDialog();
                    Toast.makeText(getApplicationContext(), "fail_add_farm", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

//    private void sendCategoriesToFirebase() {
//        if (catIndex < selectedCategories.size()) {
//            CategoryModel categoryModel = selectedCategories.get(catIndex);
//
//            fireStoreDB.collection(Constants.FB_FARMS).document(farmId).collection(Constants.FB_CATEGORIES)
//                    .document(String.valueOf(categoryModel.id)).set(categoryModel,
//                    SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
//                @Override
//                public void onComplete(@NonNull Task<Void> task) {
//                    if (task.isSuccessful()) {
//                        catIndex++;
//                        sendCategoriesToFirebase();
//                    } else {
//                        Toast.makeText(getActivity(), getString(R.string.fail_category), Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });
//        } else {
//            GlobalHelper.hideProgressDialog();
//            Toast.makeText(getActivity(), getString(R.string.succes_add_farm), Toast.LENGTH_SHORT).show();
//            Navigation.findNavController(viewGroup).popBackStack(R.id.navigation_home, false);
//
//        }
//    }

}