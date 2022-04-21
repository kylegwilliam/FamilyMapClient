package com.example.familymapclient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import model.Event;

public class EventActivity extends AppCompatActivity {

    private String eventID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //put the lines.

        //spouse lines
        //life story

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);


        eventID = getIntent().getExtras().getSerializable("eventID").toString();


        FragmentManager fragmentManager = this.getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.lineMap);

        if(fragment == null) {

            fragment = createMapFragment(eventID);

            fragmentManager.beginTransaction()
                    .add(R.id.lineMap, fragment)
                    .commit();

        }

    }
    private Fragment createMapFragment(String eventID) {
        MapFragment fragment = new MapFragment(eventID, true);
        return fragment;
    }

}

