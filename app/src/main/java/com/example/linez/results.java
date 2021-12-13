package com.example.linez;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.LayoutDirection;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
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
import java.util.concurrent.atomic.AtomicReference;


public class results extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 12;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private String name;

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
                    timerHandler.removeCallbacks(timerRunnable);
                    b.setText("start");
                } else {
                    startTime = System.currentTimeMillis();
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
        String currentTimeHour = String.format("%02d" , c.get(Calendar.HOUR_OF_DAY));


        //List itemList = new ArrayList<>();

        Log.d("apple",currentTimeHour);

        Intent i = getIntent();

        name = i.getStringExtra("name");

        //Double wait = i.getDoubleExtra("wait", 15.00);
        //waitTime.setText("0");

        placeName.setText(name);

        yourTime.setText("4");
        //waitTime.setText(String.valueOf(wait) + " minutes");

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
                });


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


    }

    private void readData(FireStoreCallback fireStoreCallback){

        TextView placeName = findViewById(R.id.PlaceName);
        String name = placeName.getText().toString();

        List itemList = new ArrayList<>();

        TimeZone tz = TimeZone.getTimeZone("GMT-6");
        Calendar c = Calendar.getInstance(tz);
        String currentTimeHour = String.format("%02d" , c.get(Calendar.HOUR_OF_DAY));

        db.collection("restaurants").document(name).collection(name)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //Log.d("pear", document.getId() + " => " + document.getData());

                                if (document.getData().containsKey(currentTimeHour)) {
                                    itemList.add(document.getData().values());
                                }

                                fireStoreCallback.onCallback(itemList);
                            }
                        } else {
                            Log.d("pear", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private interface FireStoreCallback{
        void onCallback(List<String> List);
    }

    public LatLng getMyLocation() {
        final LatLng[] location = new LatLng[1];
        int permission = ActivityCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION);
        if (permission == PackageManager.PERMISSION_DENIED){
            Log.i("Main Activity", "permission denied");
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
        else{
            mFusedLocationProviderClient.getLastLocation()
                    .addOnCompleteListener(this, task -> {
                        Location mLastKnownLocation = task.getResult();
                        Log.i("Main Activity", task.getResult() + " ");
                        if (task.isSuccessful() && mLastKnownLocation != null){
                            Log.i("Main Activity", task.getResult() + "successful");
                            location[0] = new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());
                        }
                    });
        }
        return location[0];
    }

    public double getDistance(LatLng loc1, LatLng loc2){
        double lon1 = Math.toRadians(loc1.longitude);
        double lat1 = Math.toRadians(loc1.latitude);

        double lon2 = Math.toRadians(loc2.longitude);
        double lat2 = Math.toRadians(loc2.latitude);

        double a = Math.pow(Math.sin((lat2 - lat1)/2), 2) +
                Math.cos(lat1) * Math.cos(lat2)
                * Math.pow(Math.sin((lon2 - lon1)/2), 2);

        double c = 2 * Math.asin(Math.sqrt(a));
        double radius = 6371;
        return (c * radius);
    }

    public void startClick(View view) {
        LatLng userLocation = getMyLocation();
        Log.i("results", "got user location successfully");

        LatLng restaurantLocation = null;
        ArrayList<LinezLocation> places = search.generateLocations();
        for (LinezLocation place:places){
            if (name.equals(place.getName()))
                restaurantLocation = new LatLng(place.getLatitude(), place.getLongitude());
        }

        Log.i("results", "got restaurant location successfully");

        assert restaurantLocation != null;
        assert userLocation != null;
        double distance = getDistance(userLocation, restaurantLocation);
        Log.i("results", "distance: " + String.valueOf(distance));

        if (distance <= 0.03){
            //TODO: start timer
        }
        else{
            // "error must be within 100ft of restaurant to report line times"
        }
    }
}