package com.example.linez;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class search extends AppCompatActivity {

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        mAuth = FirebaseAuth.getInstance();

        SearchView searchView = findViewById(R.id.searchView);
        ListView listView = findViewById(R.id.list);



        populateSearchView(generateLocations(), listView);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user==null){
            startActivity(new Intent(search.this, Login.class));
        }

    }

    public ArrayList<LinezLocation> generateLocations(){

        ArrayList<LinezLocation> places = new ArrayList<LinezLocation>();

        places.add(new LinezLocation(43.07525196698477, -89.39651031646711, "Chipotle", 30));
        places.add(new LinezLocation(43.0708749120671, -89.39853524433501, "Nicholas Recreation Center", 30));
        places.add(new LinezLocation(43.07633573329389, -89.39956247021655, "Strada", 30));
        places.add(new LinezLocation(43.0721235311657, -89.40792344433494, "Ginger Root", 30));

        return places;
    }

    public void populateSearchView(ArrayList<LinezLocation> places, ListView listView){
        ArrayList<String> names = new ArrayList<String>();
        for(LinezLocation location : places){
            names.add(location.getName());
        }

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, names);
        listView.setAdapter(arrayAdapter);

    }
}