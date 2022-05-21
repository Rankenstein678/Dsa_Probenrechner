package com.rankenstein.dsahelper.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.snackbar.Snackbar;
import com.rankenstein.dsahelper.R;
import com.rankenstein.dsahelper.logic.Constants;

import java.util.Objects;

public class StatsActivity extends AppCompatActivity {
    private EditText numMU, numKL, numIN, numCH, numFF, numGE, numKO, numKK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        initViews();
        try {
            Objects.requireNonNull(getSupportActionBar()).setTitle("Eigenschaften");
        } catch (NullPointerException e) {
            System.out.println("Couldn't get Action Bar");
        }
    }


    private void initViews() {
        Button saveBtn = findViewById(R.id.btnSave);
        saveBtn.setOnClickListener((View v) -> {
            saveStats();
            Snackbar.make(saveBtn,"Änderungen gespeichert!",Snackbar.LENGTH_SHORT).show();
        });
        numMU = findViewById(R.id.numMU);
        numKL = findViewById(R.id.numKL);
        numIN = findViewById(R.id.numIN);
        numCH = findViewById(R.id.numCH);
        numFF = findViewById(R.id.numFF);
        numGE = findViewById(R.id.numGE);
        numKO = findViewById(R.id.numKO);
        numKK = findViewById(R.id.numKK);

    }

    private void saveStats() {
        SharedPreferences prefs = getSharedPreferences(
                Constants.PREFERENCE_FILE_STATS, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        //TODO: Konstanten Klasse
        editor.putInt(Constants.MU, Integer.parseInt(numMU.getText().toString()));
        editor.putInt(Constants.KL, Integer.parseInt(numKL.getText().toString()));
        editor.putInt(Constants.IN, Integer.parseInt(numIN.getText().toString()));
        editor.putInt(Constants.CH, Integer.parseInt(numCH.getText().toString()));
        editor.putInt(Constants.FF, Integer.parseInt(numFF.getText().toString()));
        editor.putInt(Constants.GE, Integer.parseInt(numGE.getText().toString()));
        editor.putInt(Constants.KO, Integer.parseInt(numKO.getText().toString()));
        editor.putInt(Constants.KK, Integer.parseInt(numKK.getText().toString()));
        editor.apply();
    }

    private void loadStats() {
        SharedPreferences prefs = getSharedPreferences(
                Constants.PREFERENCE_FILE_STATS, MODE_PRIVATE);
        numMU.setText(String.valueOf(prefs.getInt(Constants.MU, 0)));
        numKL.setText(String.valueOf(prefs.getInt(Constants.MU, 0)));
        numIN.setText(String.valueOf(prefs.getInt(Constants.MU, 0)));
        numCH.setText(String.valueOf(prefs.getInt(Constants.MU, 0)));
        numFF.setText(String.valueOf(prefs.getInt(Constants.MU, 0)));
        numGE.setText(String.valueOf(prefs.getInt(Constants.MU, 0)));
        numKO.setText(String.valueOf(prefs.getInt(Constants.MU, 0)));
        numKK.setText(String.valueOf(prefs.getInt(Constants.MU, 0)));

    }

    @Override
    protected void onStart() {
        loadStats();
        super.onStart();
    }
}