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

public class Register extends AppCompatActivity {

    EditText username;
    EditText password;
    Button registerClicked;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Intent intent = getIntent();

        username = findViewById(R.id.registerEmail);
        password = findViewById(R.id.registerPassword);
        registerClicked = findViewById(R.id.registerClicked);

        mAuth = FirebaseAuth.getInstance();

        registerClicked.setOnClickListener(view -> {
            createUser();
        });

//        if(mAuth.getCurrentUser() != null){
//            startActivity(new Intent(getApplicationContext(),MainActivity.class));
//            finish();
//        }
//
//        registerClicked.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String user = username.getText().toString();
//                String pwd = password.getText().toString();
//
//                if (TextUtils.isEmpty(user)) {
//                    username.setError("Email is required");
//                }
//
//                if (TextUtils.isEmpty(pwd)) {
//                    password.setError("Password is required");
//                }
//
//                if (pwd.length() < 6) {
//                    password.setError("Password must be at least 6 characters");
//                }
//
//                mAuth.createUserWithEmailAndPassword(user, pwd)
//                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                            @Override
//                            public void onComplete(@NonNull Task<AuthResult> task) {
//                                if (task.isSuccessful()) {
//                                    FirebaseUser user = mAuth.getCurrentUser();
//                                    goToMainActivity();
//
//                                } else {
//                                    Toast.makeText(Register.this, "Authentication failed. Password is <6 letters/this email is already registered!",
//                                            Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        });
//            }
//        });
//

    }

    //method to register a user
    private void createUser() {
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
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(Register.this,"User registered successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Register.this, Login.class));
                    }
                    else{
                        Toast.makeText(Register.this,"Registration Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public void goToMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
