package com.example.linez;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Calendar;
import java.util.TimeZone;


public class results extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        TextView placeName = findViewById(R.id.PlaceName);
        TextView waitTime = findViewById(R.id.WaitTime);
        TextView yourTime = findViewById(R.id.yourTime);

        TimeZone tz = TimeZone.getTimeZone("GMT-6");
        Calendar c = Calendar.getInstance(tz);
        String currentTimeHour = String.format("%02d" , c.get(Calendar.HOUR_OF_DAY));


        //List itemList = new ArrayList<>();

        Log.d("apple",currentTimeHour);

        Intent i = getIntent();

        String name = i.getStringExtra("name");

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




}