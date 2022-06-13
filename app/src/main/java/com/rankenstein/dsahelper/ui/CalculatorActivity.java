package com.rankenstein.dsahelper.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Consumer;
import com.rankenstein.dsahelper.R;
import com.rankenstein.dsahelper.logic.ChanceLogic;
import com.rankenstein.dsahelper.logic.Constants;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Objects;

//Das Hauptfenster der App
public class CalculatorActivity extends AppCompatActivity {
    private int mod, taw;

    private ArrayList<String> stats;

    private boolean canCalculate;

    //Initialisiert alle Felder und lädt das Layout.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);
        stats = new ArrayList<>();
        taw = 0;
        mod = 0;
        canCalculate = false; //Nötig, um nach einem Eigenschaftsupdate die Chance neu zu berechnen (siehe onRestart)
        initViews();

        //Versucht den Titel der App Bar zu ändern.
        try {
            Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.main_activity_title);
        } catch (NullPointerException e) {
            Log.d("DSA-Probenrechner","Couldn't get Action Bar");
        }

    }

    //Lädt die Elemente der Aktivität und legt ihr Verhalten fest.
    private void initViews() {
        //Lädt die Eigenschaftstasten.
        Button btnMU = findViewById(R.id.btnMU);
        btnMU.setOnClickListener((View v) -> addStat(Constants.MU));
        Button btnKL = findViewById(R.id.btnKL);
        btnKL.setOnClickListener((View v) -> addStat(Constants.KL));
        Button btnIN = findViewById(R.id.btnIN);
        btnIN.setOnClickListener((View v) -> addStat(Constants.IN));
        Button btnCH = findViewById(R.id.btnCH);
        btnCH.setOnClickListener((View v) -> addStat(Constants.CH));
        Button btnFF = findViewById(R.id.btnFF);
        btnFF.setOnClickListener((View v) -> addStat(Constants.FF));
        Button btnGE = findViewById(R.id.btnGE);
        btnGE.setOnClickListener((View v) -> addStat(Constants.GE));
        Button btnKO = findViewById(R.id.btnKO);
        btnKO.setOnClickListener((View v) -> addStat(Constants.KO));
        Button btnKK = findViewById(R.id.btnKK);
        btnKK.setOnClickListener((View v) -> addStat(Constants.KK));

        //Lädt die Talentwert/Modifikator Tasten.
        Button btnTaw = findViewById(R.id.btn_taw);
        btnTaw.setOnClickListener((View v) -> createDialog(getString(R.string.dialog_taw_title), (Integer value) -> {
            //Ersetzt den Talentwert mit dem neuen Wert.
            this.taw = value;
            btnTaw.setText(getString(R.string.talentwert, taw));
            if (stats.size() == 3) calculateChance();
        }));
        Button btnMod = findViewById(R.id.btn_mod);
        btnMod.setOnClickListener((View v) -> createDialog(getString(R.string.dialog_mod_title), (Integer value) -> {
            //Ersetzt den Modifikator mit dem neuen Wert.
            this.mod = value;
            btnMod.setText(getString(R.string.modifikator, mod));
            if (stats.size() == 3) calculateChance();
        }));

        //Lädt die Zurück-Tasten
        this.findViewById(R.id.btnBack).setOnClickListener((View v) -> removeStat());
        this.findViewById(R.id.btnClear).setOnClickListener((View v) -> {
            //Entfernt alle Eigenschaften der Liste und setzt TaW und Mod zurück.
            while (!stats.isEmpty()) removeStat();
            taw = 0;
            ((Button) findViewById(R.id.btn_taw)).setText(getString(R.string.talentwert, taw));
            mod = 0;
            ((Button) findViewById(R.id.btn_mod)).setText(getString(R.string.modifikator, mod));
        });
    }

    //Entfernt eine Eigenschaft und das dazugehörige Bild. Setzt auch das Ergebnis zurück.
    private void removeStat() {
        if (stats.isEmpty()) return;
        canCalculate = false;
        if (stats.size() <= 3) {
            switch (stats.size()) {
                case 1:
                    findViewById(R.id.img_stat_1).setVisibility(View.INVISIBLE);
                    break;
                case 2:
                    findViewById(R.id.img_stat_2).setVisibility(View.INVISIBLE);

                    break;
                case 3:
                    findViewById(R.id.img_stat_3).setVisibility(View.INVISIBLE);
                    break;
            }

        }
        String result = getString(R.string.chance, "") + "\n" + getString(R.string.avrg_tap, "");
        ((TextView) findViewById(R.id.txtResult)).setText(result);
        stats.remove(stats.size() - 1);
    }

    //Fügt eine Eigenschaft und das dazugehörige Bild hinzu.
    private void addStat(String stat) {
        if (stats.size() < 3) {
            stats.add(stat);
            switch (stats.size()) {
                case 1:
                    ImageView imgStat1 = findViewById(R.id.img_stat_1);
                    //Ändert das Bild des ImageViews. Die ID der Bildressource wird aus "w20" und der Eigenschaft zusammengesetzt.
                    imgStat1.setImageResource(getResources().getIdentifier(("w20" + stat.toLowerCase()), "drawable", CalculatorActivity.this.getPackageName()));
                    imgStat1.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    ImageView imgStat2 = findViewById(R.id.img_stat_2);
                    //Ändert das Bild des ImageViews. Die ID der Bildressource wird aus "w20" und der Eigenschaft zusammengesetzt.
                    imgStat2.setImageResource(getResources().getIdentifier(("w20" + stat.toLowerCase()), "drawable", CalculatorActivity.this.getPackageName()));
                    imgStat2.setVisibility(View.VISIBLE);
                    break;
                case 3:
                    ImageView imgStat3 = findViewById(R.id.img_stat_3);
                    //Ändert das Bild des ImageViews. Die ID der Bildressource wird aus "w20" und der Eigenschaft zusammengesetzt.
                    imgStat3.setImageResource(getResources().getIdentifier(("w20" + stat.toLowerCase()), "drawable", CalculatorActivity.this.getPackageName()));
                    imgStat3.setVisibility(View.VISIBLE);
                    canCalculate = true;
                    calculateChance();
                    break;
            }

        }
    }

    //Errechnet die Chance und updated das UI
    private void calculateChance() {
        //Lädt die Eigenschaftswerte aus dem lokalen Speicher (Shared Preferences).
        SharedPreferences prefs = getSharedPreferences(Constants.PREFERENCE_FILE_STATS, MODE_PRIVATE);
        int e1 = prefs.getInt(stats.get(0), 0);
        int e2 = prefs.getInt(stats.get(1), 0);
        int e3 = prefs.getInt(stats.get(2), 0);
        //Lädt das Ergebnis
        double[] results = ChanceLogic.calculateChance(e1, e2, e3, taw, mod);

        //Formatiert die Ergebnisse in einen String und lädt ihn in das Ergebnisfeld.
        DecimalFormat df = new DecimalFormat("##.##");
        String chance = df.format(results[0] * 100) + "%\n";
        String avrgTap = String.valueOf((int) Math.round(results[1]));
        String result = getString(R.string.chance, chance) + getString(R.string.avrg_tap, avrgTap);
        ((TextView) findViewById(R.id.txtResult)).setText(result);
    }

    //Erstellt einen Eingabedialog. Der Consumer wird genutzt, um ein beliebiges Feld aus der Methode zu aktualisieren.
    private void createDialog(String title, Consumer<Integer> updateField) {
        //Erstellt den Dialog-Builder.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setCancelable(true);

        //Lädt das Layout in den Dialog.
        View inflatedView = LayoutInflater.from(this).inflate(R.layout.text_input_number, findViewById(R.id.content), false);
        final EditText txtInput = inflatedView.findViewById(R.id.input);
        builder.setView(inflatedView);

        //Erstellt die beiden Knöpfe.
        builder.setPositiveButton(R.string.ok, (dialog, which) -> {
            dialog.dismiss();
            int value = txtInput.getText().toString().isEmpty() ? 0 : Integer.parseInt(txtInput.getText().toString());
            updateField.accept(value);
        });
        builder.setNegativeButton(R.string.cancel, (dialog, which) -> dialog.cancel());

        //Öffnet die Tastatur
        AlertDialog dialog = builder.create();
        txtInput.requestFocus();
        dialog.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        //Erstellt den Dialog.
        dialog.show();
    }

    //Errechnet die neuen Chancen nach Neustart, um potenzielle Änderungen der Eigenschaften widerzuspiegeln
    @Override
    protected void onRestart() {
        if (canCalculate) {
            calculateChance();
        }
        super.onRestart();
    }

    //Handelt die Knöpfe in der App Bar.
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.action_stats) {
            //Lädt das neue Fenster (Aktivität)
            Intent intent = new Intent(CalculatorActivity.this, StatsActivity.class);
            startActivity(intent);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }


    }

    //Lädt das Optionsmenü in der App Bar.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.app_bar_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

}