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
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.loginEmail);
        password = findViewById(R.id.loginPassword);
        loginClicked = findViewById(R.id.loginClicked);

        loginClicked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = username.getText().toString();
                String pwd = password.getText().toString();

                if (TextUtils.isEmpty(user)) {
                    username.setError("Email is required");
                }

                if (TextUtils.isEmpty(pwd)) {
                    password.setError("Password is required");
                }

                if (pwd.length() < 6) {
                    password.setError("Password must be at least 6 characters");
                }



                mAuth.signInWithEmailAndPassword(user,pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        }else{
                            Toast.makeText(Login.this, "Error!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }



        });


    }

//    public void goToMainActivity(){
//        Intent intent = new Intent(this, MainActivity.class);
//        startActivity(intent);
//    }




}