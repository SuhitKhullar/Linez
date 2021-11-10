package com.example.linez;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class EntrancePage extends AppCompatActivity {

    private Button loginButton;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrance_page);

        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);

    }

    public void goToRegisterScreen(){
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
    }

    public void goToLoginScreen(){
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }


    public void onClick(View view) {
        goToRegisterScreen();
    }

    public void onClick2(View view) {
        goToLoginScreen();
    }
}