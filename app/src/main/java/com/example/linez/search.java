package com.example.linez;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class search extends AppCompatActivity   {

    FirebaseAuth mAuth;

    private GoogleMap mMap;

    ListView listView;

    ArrayAdapter<String> adapter;

    ArrayList<String> names;

    ArrayList<LinezLocation> places;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        mAuth = FirebaseAuth.getInstance();

        listView = findViewById(R.id.listView);
        places = generateLocations();


        names = new ArrayList<>();
        for(LinezLocation location : places){
            names.add(location.getName());
        }
        adapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, names);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent goToResults = new Intent(search.this, results.class);

                goToResults.putExtra("name",places.get(position).getName());
                goToResults.putExtra("wait",places.get(position).getCurWait());

                startActivity(goToResults);
            }
        });

        populateMap(places);

    }

    private void populateMap(ArrayList<LinezLocation> places) {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(googleMap -> {
            mMap = googleMap;
            //code to display marker

            for(LinezLocation location : places) {
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                googleMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(location.getName()));
            }
            float zoomLevel = 14.0f; //This goes up to 21
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(43.0731, -89.4012), zoomLevel));
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user==null){
            startActivity(new Intent(search.this, Login.class));
        }

    }

    public static ArrayList<LinezLocation> generateLocations(){

        ArrayList<LinezLocation> places = new ArrayList<>();

        places.add(new LinezLocation(43.07525196698477, -89.39651031646711, "Chipotle", 30));
        places.add(new LinezLocation(43.0708749120671, -89.39853524433501, "Nicholas Recreation Center", 30));
        places.add(new LinezLocation(43.07633573329389, -89.39956247021655, "Strada", 30));
        places.add(new LinezLocation(43.0721235311657, -89.40792344433494, "Ginger Root", 30));

        return places;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate menu with items using MenuInflator
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        // Initialise menu item search bar
        // with id and take its object
        MenuItem searchViewItem
                = menu.findItem(R.id.search_bar);
        SearchView searchView
                = (SearchView) MenuItemCompat
                .getActionView(searchViewItem);

        // attach setOnQueryTextListener
        // to search view defined above
        searchView.setOnQueryTextListener(
                new SearchView.OnQueryTextListener() {

                    // Override onQueryTextSubmit method
                    // which is call
                    // when submitquery is searched

                    @Override
                    public boolean onQueryTextSubmit(String query)
                    {
                        // If the list contains the search query
                        // than filter the adapter
                        // using the filter method
                        // with the query as its argument
                        if (names.contains(query)) {
                            adapter.getFilter().filter(query);
                        }
                        else {
                            // Search query not found in List View
                            Toast
                                    .makeText(search.this,
                                            "Not found",
                                            Toast.LENGTH_LONG)
                                    .show();
                        }
                        return false;
                    }

                    // This method is overridden to filter
                    // the adapter according to a search query
                    // when the user is typing search
                    @Override
                    public boolean onQueryTextChange(String newText)
                    {
                        adapter.getFilter().filter(newText);
                        return false;
                    }
                });

        return super.onCreateOptionsMenu(menu);
    }

    public void goHome(View view) {
        startActivity(new Intent(search.this, EntrancePage.class));
    }

    public void goProfile(View view) {
        startActivity(new Intent(search.this, Profile.class));
    }
}