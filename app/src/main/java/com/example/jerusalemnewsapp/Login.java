package com.example.jerusalemnewsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends BaseActivity {
    private EditText emailEd,passwordEd;
    private FirebaseAuth mAuth;
    private ProgressBar pB ;
    Button loginbtn, creatbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        emailEd = findViewById(R.id.emailEd);
        passwordEd = findViewById(R.id.passwordEd);
        loginbtn = findViewById(R.id.loginbtn);
        creatbtn = findViewById(R.id.creatbtn);

        pB = (ProgressBar) findViewById(R.id.PBar);
        pB.setVisibility(View.INVISIBLE);

        mAuth = FirebaseAuth.getInstance();

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        creatbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUp();
            }
        });

    }

    public void login(){

        pB.setVisibility(View.INVISIBLE);

        String Email = emailEd.getText().toString().trim();
        String Pass = passwordEd.getText().toString().trim();

        if (Email.isEmpty()){
            Toast.makeText(this, "Please Add Email.", Toast.LENGTH_SHORT).show();
        }
        else if (Pass.isEmpty()){
            Toast.makeText(this, "Please Add Password.", Toast.LENGTH_SHORT).show();
        }else {

            mAuth.signInWithEmailAndPassword(Email,Pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {

                    Intent intent = new Intent(Login.this , MainActivity.class);
                    startActivity(intent);
                    finish();
                    pB.setVisibility(View.VISIBLE);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Toast.makeText(Login.this, "Error " + e.toString(), Toast.LENGTH_SHORT).show();
                    pB.setVisibility(View.VISIBLE);

                }
            });

        }

    }


    public void SignUp() {

        Intent intent = new Intent(Login.this , CreateAccount.class);
        startActivity(intent);
        finish();
    }
}
