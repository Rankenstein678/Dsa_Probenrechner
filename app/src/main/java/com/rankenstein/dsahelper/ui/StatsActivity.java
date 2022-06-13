package com.rankenstein.dsahelper.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.snackbar.Snackbar;
import com.rankenstein.dsahelper.R;
import com.rankenstein.dsahelper.logic.Constants;

import java.util.Objects;

//Das Fenster um die Eigenschaftswerte zu bearbeiten
public class StatsActivity extends AppCompatActivity {
    private EditText numMU, numKL, numIN, numCH, numFF, numGE, numKO, numKK;

    private boolean changesSaved;
    private SharedPreferences sharedPrefs;

    //Initialisiert alle Felder und lädt das Layout.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        //Lädt zwischen App-Starts erhaltene Informationen aus dem lokalen Speicher
        sharedPrefs = getSharedPreferences(Constants.PREFERENCE_FILE_STATS, MODE_PRIVATE);

        initViews();

        //Versucht den Titel der App Bar zu ändern.
        try {
            Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.stats_activity_title));
        } catch (NullPointerException e) {
            Log.d("DSA-Probenrechner", "Couldn't get Action Bar");
        }
    }


    //Lädt die Elemente der Aktivität und legt ihr Verhalten fest.
    private void initViews() {
        //Lädt den Speicherknopf
        Button saveBtn = findViewById(R.id.btnSave);
        saveBtn.setOnClickListener((View v) -> {
            saveStats();
            Snackbar.make(saveBtn, getText(R.string.saved_changes), Snackbar.LENGTH_SHORT).show();
        });

        //Initialisiert die Felder des Objekts mit den Textfeldern.
        numMU = findViewById(R.id.numMU);
        numKL = findViewById(R.id.numKL);
        numIN = findViewById(R.id.numIN);
        numCH = findViewById(R.id.numCH);
        numFF = findViewById(R.id.numFF);
        numGE = findViewById(R.id.numGE);
        numKO = findViewById(R.id.numKO);
        numKK = findViewById(R.id.numKK);

        EditText[] ets = new EditText[]{numMU, numKL, numIN, numCH, numFF, numGE, numKO, numKK};
        for (EditText et : ets) {
            //Nötig da Variablen in inneren Klassen effektiv final sein müssen
            et.addTextChangedListener(new TextWatcher() {
                @Override
                public void afterTextChanged(Editable editable) {
                    changesSaved = false;
                }

                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }
            });
        }
    }

    //Speichert die Eigenschaften im Lokalen Speicher (Shared Preferences).
    private void saveStats() {
        changesSaved = true;

        SharedPreferences.Editor editor = sharedPrefs.edit();
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

    //Lädt die gespeicherten Eigenschaften und setzt sie in die Textfelder ein.
    private void loadStats() {
        String inMU = String.valueOf(sharedPrefs.getInt(Constants.MU, 0));
        String inKL = String.valueOf(sharedPrefs.getInt(Constants.KL, 0));
        String inIN = String.valueOf(sharedPrefs.getInt(Constants.IN, 0));
        String inCH = String.valueOf(sharedPrefs.getInt(Constants.CH, 0));
        String inFF = String.valueOf(sharedPrefs.getInt(Constants.FF, 0));
        String inGE = String.valueOf(sharedPrefs.getInt(Constants.GE, 0));
        String inKO = String.valueOf(sharedPrefs.getInt(Constants.KO, 0));
        String inKK = String.valueOf(sharedPrefs.getInt(Constants.KK, 0));

        numMU.setText(inMU);
        numKL.setText(inKL);
        numIN.setText(inIN);
        numCH.setText(inCH);
        numFF.setText(inFF);
        numGE.setText(inGE);
        numKO.setText(inKO);
        numKK.setText(inKK);
    }

    //Warnt bei ungespeicherten Änderungen.
    @Override
    public void onBackPressed() {
        //Gleicht aktuelle Werte mit den gespeicherten ab
        if (changesSaved) {
            super.onBackPressed();
        } else {
            //Erstellt das Dialogfenster.
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getString(R.string.discard_changes));
            builder.setCancelable(true);

            //Erstellt beide Knöpfe
            builder.setPositiveButton(getString(R.string.save), (dialogInterface, i) -> {
                saveStats();
                dialogInterface.dismiss();
                StatsActivity.super.onBackPressed();
            });
            builder.setNegativeButton(getString(R.string.discard), (dialogInterface, i) -> {
                dialogInterface.dismiss();
                StatsActivity.super.onBackPressed();
            });

            //Erstellt den Dialog.
            builder.show();
        }
    }

    //Lädt die Eigenschaften beim Start.
    @Override
    protected void onStart() {
        loadStats();
        changesSaved = true;
        super.onStart();
    }
}