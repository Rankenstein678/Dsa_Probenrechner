package com.rankenstein.dsahelper.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import com.rankenstein.dsahelper.R;

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
        saveBtn.setOnClickListener((View v) -> saveStats());
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
                getString(R.string.stats_file_key), MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        //TODO: Konstanten Klasse
        editor.putInt("MU", Integer.parseInt(numMU.getText().toString()));
        editor.putInt("KL", Integer.parseInt(numKL.getText().toString()));
        editor.putInt("IN", Integer.parseInt(numIN.getText().toString()));
        editor.putInt("CH", Integer.parseInt(numCH.getText().toString()));
        editor.putInt("FF", Integer.parseInt(numFF.getText().toString()));
        editor.putInt("GE", Integer.parseInt(numGE.getText().toString()));
        editor.putInt("KO", Integer.parseInt(numKO.getText().toString()));
        editor.putInt("KK", Integer.parseInt(numKK.getText().toString()));
        editor.apply();
    }

    private void loadStats() {
        SharedPreferences prefs = getSharedPreferences(
                getString(R.string.stats_file_key), MODE_PRIVATE);
        numMU.setText(String.valueOf(prefs.getInt("MU", 0)));
        numKL.setText(String.valueOf(prefs.getInt("KL", 0)));
        numIN.setText(String.valueOf(prefs.getInt("IN", 0)));
        numCH.setText(String.valueOf(prefs.getInt("CH", 0)));
        numFF.setText(String.valueOf(prefs.getInt("FF", 0)));
        numGE.setText(String.valueOf(prefs.getInt("GE", 0)));
        numKO.setText(String.valueOf(prefs.getInt("KO", 0)));
        numKK.setText(String.valueOf(prefs.getInt("KK", 0)));

    }

    @Override
    protected void onStart() {
        loadStats();
        super.onStart();
    }
}