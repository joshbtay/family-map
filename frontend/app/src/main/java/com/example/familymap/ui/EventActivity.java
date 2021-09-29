package com.example.familymap.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;

import com.example.familymap.R;
import com.example.familymap.core.DataCache;

public class EventActivity extends AppCompatActivity {
    FragmentManager fm = getSupportFragmentManager();
    DataCache dataCache = DataCache.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);
        Bundle args = getIntent().getExtras();
        String eventID = args.getString("EventID");
        if(fragment == null) {
            MapsFragment mapFragment = new MapsFragment();
            mapFragment.setEventID(eventID);
            fm.beginTransaction().add(R.id.fragmentContainer, mapFragment).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        return true;
    }
}