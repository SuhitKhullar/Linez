package com.example.linez;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Calendar;
import java.util.TimeZone;


public class results extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 12;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private String name;

    LocationManager locationManager;
    LocationListener locationListener;

    List itemList;
    //DateFormat timeFormat = DateFormat.getDateInstance(DateFormat.SHORT, Locale.US);
    //Date rightNow = Calendar.getInstance().getTime();
    //Calendar rightNow = Calendar.getInstance();
    //Calendar calendar = Calendar.getInstance();
    //SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss z");
    //sdf.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
    //System.out.println(sdf.format(calendar.getTime()));
    //int currentHourIn24Format = rightNow.get(Calendar.HOUR_OF_DAY);
    //TimeZone tz = TimeZone.getTimeZone("America/Los_Angeles");

    //String apple = String(currentHourIn24Format);

    TextView timerView;
    long startTime = 0;
    long elapsedTime = 0;

    //runs without a timer by reposting this handler at the end of the runnable
    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            long ms = System.currentTimeMillis() - startTime;
            int sec = (int) (ms / 1000);
            int min = sec / 60;
            sec = sec % 60;

            timerView.setText(String.format("%d:%02d", min, sec));
            timerHandler.postDelayed(this, 500);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        timerView = (TextView) findViewById(R.id.timerView);

        Button b = (Button) findViewById(R.id.start);
        b.setText("start");

        b.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Button b = (Button) v;
                if (b.getText().equals("stop")) {
                    elapsedTime = System.currentTimeMillis() - startTime;
                    timerHandler.removeCallbacks(timerRunnable);
                    b.setText("start");
                } else {
                    startTime = System.currentTimeMillis() - elapsedTime;
                    timerHandler.postDelayed(timerRunnable, 0);
                    b.setText("stop");
                }
            }
        });

        TextView placeName = findViewById(R.id.PlaceName);
        TextView waitTime = findViewById(R.id.WaitTime);
        TextView yourTime = findViewById(R.id.timerView);

        TimeZone tz = TimeZone.getTimeZone("GMT-6");
        Calendar c = Calendar.getInstance(tz);
        String currentTimeHour = String.format("%02d", c.get(Calendar.HOUR_OF_DAY));


        //List itemList = new ArrayList<>();

        Log.d("apple", currentTimeHour);
        Intent i = getIntent();

        name = i.getStringExtra("name");

        Double wait = i.getDoubleExtra("wait", 15.00);

        //waitTime.setText("0");

        placeName.setText(name);
        yourTime.setText(" ");
        //waitTime.setText(String.valueOf(wait) + " minutes");
/*
        Map<String, Object> user = new HashMap<>();
        user.put(currentTimeHour,yourTime.getText());

        // Add a new document with a generated ID
        db.collection("restaurants").document(name).collection(name)
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });*/

/*
        readData(new FireStoreCallback() {
            @Override
            public void onCallback(List<String> list) {

                Log.d("cat", list.toString());
                int total = 0;
                Log.d("dog", Integer.toString(list.size()));
//                for (int i = 0; i < list.size() ;i++){
//                    Log.d("dog", Integer.toString(list.size()));
//                }

                //double averageTime = (total * 1.0) / list.size();
                //waitTime.setText((int) averageTime);
            }
        });
    }*/
        readData(new FireStoreCallback() {
            @Override
            public void onCallback(List<String> list) {

                int total = 0;
                for (int i = 0; i < list.size(); i++) {
                    if (!list.get(i).equals(" ")) {
                        total = total + Integer.parseInt(list.get(i));
                    }
                }

                //Log.d("total", String.valueOf(total));

                double averageTime = (total * 1.0) / list.size();

                int avg = (int) averageTime;

                waitTime.setText("Estimated wait time: " + String.valueOf(avg) + " minutes");
            }
        });
    }

    private void readData(FireStoreCallback fireStoreCallback) {

        TextView placeName = findViewById(R.id.PlaceName);
        String name = placeName.getText().toString();

        itemList = new ArrayList<>();

        TimeZone tz = TimeZone.getTimeZone("GMT-6");
        Calendar c = Calendar.getInstance(tz);
        String currentTimeHour = String.format("%02d", c.get(Calendar.HOUR_OF_DAY));

        db.collection("restaurants").document(name).collection(name)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //Log.d("pear", document.getId() + " => " + document.getData());

                                if (document.getData().containsKey(currentTimeHour)) {
                                    String itemName = document.getString(currentTimeHour);
                                    itemList.add(itemName);
                                }
                            }
                            fireStoreCallback.onCallback(itemList);
                        } else {
                            Log.d("pear", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public void homeButton(View view) {
        startActivity(new Intent(results.this, search.class));
    }

    public void logoutOnClick(View view) {
        startActivity(new Intent(results.this, EntrancePage.class));
    }


    private interface FireStoreCallback {
        void onCallback(List<String> List);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getMyLocation();
            }
        }
    }

    private LatLng getMyLocation() {
        final LatLng[] curLocation = new LatLng[1];
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {

                curLocation[0] = new LatLng(location.getLatitude(), location.getLongitude());
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        if (Build.VERSION.SDK_INT < 23) {
            startListening();
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (location != null) {
                    curLocation[0] = new LatLng(location.getLatitude(), location.getLongitude());
                }
            }
        }

        return curLocation[0];
    }

    public void startListening() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        }
    }

    public double getDistance(LatLng loc1, LatLng loc2) {
        double lon1 = Math.toRadians(loc1.longitude);
        double lat1 = Math.toRadians(loc1.latitude);

        double lon2 = Math.toRadians(loc2.longitude);
        double lat2 = Math.toRadians(loc2.latitude);

        double a = Math.pow(Math.sin((lat2 - lat1) / 2), 2) +
                Math.cos(lat1) * Math.cos(lat2)
                        * Math.pow(Math.sin((lon2 - lon1) / 2), 2);

        double c = 2 * Math.asin(Math.sqrt(a));
        double radius = 6371;
        return (c * radius);
    }

    public void onSubmitClick(View view) {
        LatLng userLocation = getMyLocation();
        Log.i("results", "got user location successfully");

        LatLng restaurantLocation = null;
        ArrayList<LinezLocation> places = search.generateLocations();
        for (LinezLocation place : places) {
            if (name.equals(place.getName()))
                restaurantLocation = new LatLng(place.getLatitude(), place.getLongitude());
        }

        Log.i("results", "got restaurant location successfully");

        assert restaurantLocation != null;
        assert userLocation != null;
        double distance = getDistance(userLocation, restaurantLocation);
        Log.i("results", "distance: " + String.valueOf(distance));

        if (distance >= 0.03) {
            ExampleDialog exampleDialog = new ExampleDialog();
            exampleDialog.show(getSupportFragmentManager(), "example dialog");
        } else {
            TextView yourTime = findViewById(R.id.timerView);

            TimeZone tz = TimeZone.getTimeZone("GMT-6");
            Calendar c = Calendar.getInstance(tz);

            String currentTimeHour = String.format("%02d", c.get(Calendar.HOUR_OF_DAY));

            Map<String, Object> user = new HashMap<>();

            String time = (String) yourTime.getText();
            String[] colonArray = time.split(":");


            user.put(currentTimeHour, colonArray[0]);

            // Add a new document with a generated ID
            db.collection("restaurants").document(name).collection(name)
                    .add(user)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error adding document", e);
                        }
                    });
            SuccessDialog successDialog = new SuccessDialog();
            successDialog.show(getSupportFragmentManager(), "success dialog");
        }
    }
}
