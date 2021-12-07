package com.example.linez;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class search extends AppCompatActivity {

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        mAuth = FirebaseAuth.getInstance();

        generateLocations();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user==null){
            startActivity(new Intent(search.this, Login.class));
        }

    }

    public void generateLocations(){
        LinezLocation chipotle = new LinezLocation(43.07525196698477, -89.39651031646711, "Chipotle", 30);
        LinezLocation nick = new LinezLocation(43.0708749120671, -89.39853524433501, "Nicholas Recreation Center", 30);
        LinezLocation strada = new LinezLocation(43.07633573329389, -89.39956247021655, "Strada", 30);
        LinezLocation gingerRoot = new LinezLocation(43.0721235311657, -89.40792344433494, "Ginger Root", 30);
    }
}