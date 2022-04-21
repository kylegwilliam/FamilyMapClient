package com.example.familymapclient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    public TextView LogoutID;
    Switch LifeStorySwitch;
    Switch SpouseStorySwitch;
    Switch FamilyTreeSwitch;
    Switch FathersLineSwitch;
    Switch MothersLineSwitch;
    Switch MaleEventsSwitch;
    Switch FemaleEventsSwitch;

    boolean SpouseLinesResult = true;
    boolean LifeStoryResult = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        SharedPreferences sharedPreferences = getSharedPreferences("save", MODE_PRIVATE);

        DataCache dataCache = DataCache.getInstance();
        
        LifeStorySwitch = findViewById(R.id.lifeStorySwitch);
        LifeStorySwitch.setChecked(sharedPreferences.getBoolean("lifeValue", true));
        LifeStorySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    SharedPreferences.Editor editor = getSharedPreferences("save", MODE_PRIVATE).edit();
                    editor.putBoolean("lifeValue", true);
                    editor.apply();
                    LifeStorySwitch.setChecked(true);
                    LifeStoryResult = true;

                    dataCache.setStorySwitch(true);

                }
                else {
                    SharedPreferences.Editor editor = getSharedPreferences("save", MODE_PRIVATE).edit();
                    editor.putBoolean("lifeValue", false);
                    editor.apply();
                    LifeStorySwitch.setChecked(false);
                    LifeStoryResult = false;

                    dataCache.setStorySwitch(false);
                }
            }
        });

        SpouseStorySwitch = findViewById(R.id.spouseLinesSwitch);
        SpouseStorySwitch.setChecked(sharedPreferences.getBoolean("spouseValue", true));
        SpouseStorySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    SharedPreferences.Editor editor = getSharedPreferences("save", MODE_PRIVATE).edit();
                    editor.putBoolean("spouseValue", true);
                    editor.apply();
                    SpouseStorySwitch.setChecked(true);

                    dataCache.setSpouseSwitch(true);
                }
                else {
                    SharedPreferences.Editor editor = getSharedPreferences("save", MODE_PRIVATE).edit();
                    editor.putBoolean("spouseValue", false);
                    editor.apply();
                    SpouseStorySwitch.setChecked(false);

                    dataCache.setSpouseSwitch(false);
                }
            }
        });

        FamilyTreeSwitch = findViewById(R.id.familyTreeSwitch);
        FamilyTreeSwitch.setChecked(sharedPreferences.getBoolean("familyTreeValue", true));
        FamilyTreeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    SharedPreferences.Editor editor = getSharedPreferences("save", MODE_PRIVATE).edit();
                    editor.putBoolean("familyTreeValue", true);
                    editor.apply();
                    FamilyTreeSwitch.setChecked(true);
                }
                else {
                    SharedPreferences.Editor editor = getSharedPreferences("save", MODE_PRIVATE).edit();
                    editor.putBoolean("familyTreeValue", false);
                    editor.apply();
                    FamilyTreeSwitch.setChecked(false);

                }
            }
        });

        FathersLineSwitch = findViewById(R.id.fathersLineSwitch);
        FathersLineSwitch.setChecked(sharedPreferences.getBoolean("fathersLineValue", true));
        FathersLineSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    SharedPreferences.Editor editor = getSharedPreferences("save", MODE_PRIVATE).edit();
                    editor.putBoolean("fathersLineValue", true);
                    editor.apply();
                    FathersLineSwitch.setChecked(true);
                }
                else {
                    SharedPreferences.Editor editor = getSharedPreferences("save", MODE_PRIVATE).edit();
                    editor.putBoolean("fathersLineValue", false);
                    editor.apply();
                    FathersLineSwitch.setChecked(false);
                }
            }
        });

        MothersLineSwitch = findViewById(R.id.mothersLineSwitch);
        MothersLineSwitch.setChecked(sharedPreferences.getBoolean("mothersLineValue", true));
        MothersLineSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    SharedPreferences.Editor editor = getSharedPreferences("save", MODE_PRIVATE).edit();
                    editor.putBoolean("mothersLineValue", true);
                    editor.apply();
                    MothersLineSwitch.setChecked(true);
                }
                else {
                    SharedPreferences.Editor editor = getSharedPreferences("save", MODE_PRIVATE).edit();
                    editor.putBoolean("mothersLineValue", false);
                    editor.apply();
                    MothersLineSwitch.setChecked(false);
                }
            }
        });

        MaleEventsSwitch = findViewById(R.id.maleEventsSwitch);
        MaleEventsSwitch.setChecked(sharedPreferences.getBoolean("maleEventsValue", true));
        MaleEventsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    SharedPreferences.Editor editor = getSharedPreferences("save", MODE_PRIVATE).edit();
                    editor.putBoolean("maleEventsValue", true);
                    editor.apply();
                    MaleEventsSwitch.setChecked(true);
                }
                else {
                    SharedPreferences.Editor editor = getSharedPreferences("save", MODE_PRIVATE).edit();
                    editor.putBoolean("maleEventsValue", false);
                    editor.apply();
                    MaleEventsSwitch.setChecked(false);
                }
            }
        });

        FemaleEventsSwitch = findViewById(R.id.femaleEventsSwitch);
        FemaleEventsSwitch.setChecked(sharedPreferences.getBoolean("femaleEventsValue", true));
        FemaleEventsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    SharedPreferences.Editor editor = getSharedPreferences("save", MODE_PRIVATE).edit();
                    editor.putBoolean("femaleEventsValue", true);
                    editor.apply();
                    FemaleEventsSwitch.setChecked(true);
                }
                else {
                    SharedPreferences.Editor editor = getSharedPreferences("save", MODE_PRIVATE).edit();
                    editor.putBoolean("femaleEventsValue", false);
                    editor.apply();
                    FemaleEventsSwitch.setChecked(false);
                }
            }
        });


        LogoutID = findViewById(R.id.LogoutID);
        LogoutID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//                fragmentTransaction.replace(R.id.settingsMenu, new LoginFragment()).commit();

                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);

            }

        });
    }



}