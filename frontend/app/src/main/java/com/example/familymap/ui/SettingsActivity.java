package com.example.familymap.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

import com.example.familymap.R;
import com.example.familymap.core.DataCache;

import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity {

    private Button logoutButton;
    private Switch lifeLine;
    private Switch familyLine;
    private Switch spouseLine;
    private Switch fatherSide;
    private Switch motherSide;
    private Switch maleEvents;
    private Switch femaleEvents;
    private Intent data = new Intent();
    private ArrayList<Boolean> initialSettings;
    private ArrayList<Boolean> currentSettings;
    public final static String LOGOUT = "logout";
    public final static String CHANGES = "changes";
    DataCache dataCache;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        dataCache = DataCache.getInstance();
        initialSettings = new ArrayList<>();
        getSettings();
        currentSettings = copySettings();

        lifeLine = (Switch) findViewById(R.id.lifeStoryLinesSwitch);
        lifeLine.setChecked(initialSettings.get(0));
        lifeLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentSettings.set(0, lifeLine.isChecked());
                setChangesResult();
            }
        });

        familyLine = (Switch) findViewById(R.id.familyTreeLinesSwitch);
        familyLine.setChecked(initialSettings.get(1));
        familyLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentSettings.set(1, familyLine.isChecked());
                setChangesResult();
            }
        });

        spouseLine = (Switch) findViewById(R.id.spouseLinesSwitch);
        spouseLine.setChecked(initialSettings.get(2));
        spouseLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentSettings.set(2, spouseLine.isChecked());
                setChangesResult();
            }
        });


        fatherSide = (Switch) findViewById(R.id.fatherSideSwitch);
        fatherSide.setChecked(initialSettings.get(3));
        fatherSide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentSettings.set(3, fatherSide.isChecked());
                setChangesResult();
            }
        });

        motherSide = (Switch) findViewById(R.id.motherSideSwitch);
        motherSide.setChecked(initialSettings.get(4));
        motherSide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentSettings.set(4, motherSide.isChecked());
                setChangesResult();
            }
        });

        maleEvents = (Switch) findViewById(R.id.maleEventsSwitch);
        maleEvents.setChecked(initialSettings.get(5));
        maleEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentSettings.set(5, maleEvents.isChecked());
                setChangesResult();
            }
        });

        femaleEvents = (Switch) findViewById(R.id.femaleEventsSwitch);
        femaleEvents.setChecked(initialSettings.get(6));
        femaleEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentSettings.set(6, femaleEvents.isChecked());
                setChangesResult();
            }
        });





        logoutButton = (Button) findViewById(R.id.logout);
        logoutButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                setLogoutResult();
                finish();
            }
        });
    }

    private ArrayList<Boolean> copySettings() {
        ArrayList<Boolean> copy = new ArrayList<>();
        for (Boolean setting : initialSettings){
            copy.add(setting);
        }
        return copy;
    }

    private void getSettings() {
        initialSettings.add(dataCache.getLifeLine());
        initialSettings.add(dataCache.getFamilyLine());
        initialSettings.add(dataCache.getSpouseLine());
        initialSettings.add(dataCache.getFatherSide());
        initialSettings.add(dataCache.getMotherSide());
        initialSettings.add(dataCache.getMaleEvents());
        initialSettings.add(dataCache.getFemaleEvents());
    }

    private void setSettings(){
        dataCache.setLifeLine(currentSettings.get(0));
        dataCache.setFamilyLine(currentSettings.get(1));
        dataCache.setSpouseLine(currentSettings.get(2));
        dataCache.setFatherSide(currentSettings.get(3));
        dataCache.setMotherSide(currentSettings.get(4));
        dataCache.setMaleEvents(currentSettings.get(5));
        dataCache.setFemaleEvents(currentSettings.get(6));
    }

    private void setLogoutResult(){
        data.putExtra(LOGOUT, true);
        setResult(RESULT_OK, data);
    }

    private void setChangesResult(){
        boolean changed = false;
        setSettings();
        for(int i = 0; i < initialSettings.size(); i++){
            if(initialSettings.get(i) != currentSettings.get(i)){
                changed = true;
            }
        }
        data.putExtra(CHANGES, changed);
        setResult(RESULT_OK, data);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        return true;
    }




}