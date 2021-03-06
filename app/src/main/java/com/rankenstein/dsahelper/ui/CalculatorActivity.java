package com.rankenstein.dsahelper.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Consumer;
import com.rankenstein.dsahelper.R;
import com.rankenstein.dsahelper.logic.ChanceLogic;
import com.rankenstein.dsahelper.logic.Check;
import com.rankenstein.dsahelper.logic.CheckHelper;
import com.rankenstein.dsahelper.logic.Constants;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Objects;

//Das Hauptfenster der App
public class CalculatorActivity extends AppCompatActivity {
    private int mod, taw;

    private ArrayList<String> stats;

    private boolean canCalculate;

    public static SharedPreferences prefsStats, prefsChecks;

    //Initialisiert alle Felder und lädt das Layout.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        prefsStats = getSharedPreferences(Constants.PREFERENCE_FILE_STATS, MODE_PRIVATE);
        prefsChecks = getSharedPreferences(Constants.PREFERENCE_FILE_CHECKS, MODE_PRIVATE);

        stats = new ArrayList<>();
        taw = 0;
        mod = 0;
        canCalculate = false; //Nötig, um nach einem Eigenschaftsupdate die Chance neu zu berechnen (siehe onRestart)
        initViews();

        //Versucht den Titel der App Bar zu ändern.
        try {
            Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.main_activity_title);
        } catch (NullPointerException e) {
            Log.d("DSA-Probenrechner", "Couldn't get Action Bar");
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
            taw = value;
            btnTaw.setText(getString(R.string.talentwert, taw));
            if (stats.size() == 3) calculateChance();
        }));
        Button btnMod = findViewById(R.id.btn_mod);
        btnMod.setOnClickListener((View v) -> createDialog(getString(R.string.dialog_mod_title), (Integer value) -> {
            //Ersetzt den Modifikator mit dem neuen Wert.
            mod = value;
            btnMod.setText(getString(R.string.modifikator, mod));
            if (stats.size() == 3) calculateChance();
        }));

        //Lädt die Zurück-Tasten
        findViewById(R.id.btnBack).setOnClickListener((View v) -> removeStat());
        findViewById(R.id.btnClear).setOnClickListener((View v) -> clear());
    }

    @Override
    protected void onStart() {
        String ex = getIntent().getStringExtra("check");
        if (ex != null) {
            String[] i = getIntent().getStringExtra("check").split(",");
            Check c = new Check("", i[0], i[1], i[2], Integer.parseInt(i[3]), Integer.parseInt(i[4]));
            loadCheck(c);
        }
        super.onStart();
    }

    private void clear() {
        //Entfernt alle Eigenschaften der Liste und setzt TaW und Mod zurück.
        while (!stats.isEmpty()) removeStat();
        taw = 0;
        ((Button) findViewById(R.id.btn_taw)).setText(getString(R.string.talentwert, taw));
        mod = 0;
        ((Button) findViewById(R.id.btn_mod)).setText(getString(R.string.modifikator, mod));
    }

    //Entfernt eine Eigenschaft und das dazugehörige Bild. Setzt auch das Ergebnis zurück.
    private void removeStat() {
        canCalculate = false;
        if (stats.isEmpty()) return;
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
        ((TextView) findViewById(R.id.txtResult)).setText(result); //Leert das Ergebnis-Feld
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
        int e1 = prefsStats.getInt(stats.get(0), 0);
        int e2 = prefsStats.getInt(stats.get(1), 0);
        int e3 = prefsStats.getInt(stats.get(2), 0);
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
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        //Erstellt den Dialog.
        dialog.show();
    }

    public void loadCheck(Check c) {
        clear();
        addStat(c.getE1());
        addStat(c.getE2());
        addStat(c.getE3());
        taw = c.getTaw();
        mod = c.getMod();
        calculateChance();
    }

    //Errechnet die neuen Chancen nach Neustart der Aktivität, um potenzielle Änderungen der Eigenschaften widerzuspiegeln, beispielsweise nach der Rückkehr aus der Stats Aktivität.
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
        } else if (item.getItemId() == R.id.action_list) {
            Intent intent = new Intent(CalculatorActivity.this, SavedChecksActivity.class);
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.save) {
            if (!canCalculate) {
                Toast.makeText(this, "Gib 3 Eigenschaften an", Toast.LENGTH_SHORT).show();
                return false;
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Name");
            builder.setCancelable(true);
            View inflatedView = LayoutInflater.from(this).inflate(R.layout.text_input, findViewById(R.id.content), false);
            final EditText txtInput = inflatedView.findViewById(R.id.input);
            builder.setView(inflatedView);
            builder.setPositiveButton(R.string.ok, (dialog, which) -> {
                String name = !txtInput.getText().toString().isEmpty() ? txtInput.getText().toString() : getString(R.string.new_check);
                CheckHelper.appendCheck(new Check(name, stats.get(0), stats.get(1), stats.get(2), taw, mod));
            });
            builder.setNegativeButton(R.string.cancel, (dialog, which) -> dialog.cancel());
            AlertDialog dialog = builder.create();
            txtInput.requestFocus();
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            dialog.show();

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