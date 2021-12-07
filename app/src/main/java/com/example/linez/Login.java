package com.example.linez;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {


    EditText username;
    EditText password;
    Button loginClicked;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.loginEmail);
        password = findViewById(R.id.loginPassword);
        loginClicked = findViewById(R.id.loginClicked);

        mAuth = FirebaseAuth.getInstance();

        loginClicked.setOnClickListener(view -> {
            loginUser();
        });

    }

    private void loginUser() {
        String email = username.getText().toString();
        String password = username.getText().toString();

        //check if email and password empty
        if(TextUtils.isEmpty(email)){
            username.setError("Email cannot be empty!");
            username.requestFocus();
        }
        else if(TextUtils.isEmpty(password)){
            username.setError("Password cannot be empty!");
            username.requestFocus();
        }
        else{
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(Login.this,"User logged in successfully!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Login.this, search.class));
                    }
                    else{
                        Toast.makeText(Login.this,"Login Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

//    public void goToMainActivity(){
//        Intent intent = new Intent(this, MainActivity.class);
//        startActivity(intent);
//    }




}