package com.rankenstein.dsahelper.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

import java.text.DecimalFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    //TODO: Unabhäbig von Text der Knöpfe machen
    private int mod, taw;

    private ArrayList<String> stats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        stats = new ArrayList<>();
        taw = 0;
        mod = 0;
        initViews();
    }

    private void initViews() {
        Button btnTaw = findViewById(R.id.btn_taw);
        btnTaw.setOnClickListener((View v) -> createDialog("TaW/ZfW eingeben", (Integer value) -> {
            this.taw = value;
            btnTaw.setText(getString(R.string.talentwert, taw));
            if (stats.size() == 3) calculateChance();
        }));
        Button btnMod = findViewById(R.id.btn_mod);
        btnMod.setOnClickListener((View v) -> createDialog("Modifikator eingeben", (Integer value) -> {
            this.mod = value;
            btnMod.setText(getString(R.string.modifikator, mod));
            if (stats.size() == 3) calculateChance();
        }));

        Button btnMU = findViewById(R.id.btnMU);
        btnMU.setOnClickListener((View v) -> addStat(btnMU.getText().toString()));
        Button btnKL = findViewById(R.id.btnKL);
        btnKL.setOnClickListener((View v) -> addStat(btnKL.getText().toString()));
        Button btnIN = findViewById(R.id.btnIN);
        btnIN.setOnClickListener((View v) -> addStat(btnIN.getText().toString()));
        Button btnCH = findViewById(R.id.btnCH);
        btnCH.setOnClickListener((View v) -> addStat(btnCH.getText().toString()));
        Button btnFF = findViewById(R.id.btnFF);
        btnFF.setOnClickListener((View v) -> addStat(btnFF.getText().toString()));
        Button btnGE = findViewById(R.id.btnGE);
        btnGE.setOnClickListener((View v) -> addStat(btnGE.getText().toString()));
        Button btnKO = findViewById(R.id.btnKO);
        btnKO.setOnClickListener((View v) -> addStat(btnKO.getText().toString()));
        Button btnKK = findViewById(R.id.btnKK);
        btnKK.setOnClickListener((View v) -> addStat(btnKK.getText().toString()));

        this.findViewById(R.id.btnBack).setOnClickListener((View v) -> removeStat());
        this.findViewById(R.id.btnClear).setOnClickListener((View v) -> {
            while (!stats.isEmpty()) removeStat();
            taw = 0;
            ((Button) findViewById(R.id.btn_taw)).setText(getString(R.string.talentwert, taw));
            mod = 0;
            ((Button) findViewById(R.id.btn_mod)).setText(getString(R.string.modifikator, mod));
        });
    }

    private void removeStat() {
        if (stats.isEmpty()) return;
        if (stats.size() <= 3) {
            switch (stats.size()) {
                case 1:
                    findViewById(R.id.img_stat_1).setVisibility(View.GONE);
                    break;
                case 2:
                    findViewById(R.id.img_stat_2).setVisibility(View.GONE);

                    break;
                case 3:
                    findViewById(R.id.img_stat_3).setVisibility(View.GONE);
                    break;
            }

        }
        ((TextView) findViewById(R.id.txtResult)).setText("Chance:\ndurchschnittliche TaP*:");
        stats.remove(stats.size() - 1);
    }

    private void addStat(String stat) {
        stats.add(stat);
        if (stats.size() <= 3) {
            switch (stats.size()) {
                case 1:
                    ImageView imgStat1 = findViewById(R.id.img_stat_1);
                    imgStat1.setImageResource(getResources().getIdentifier(("w20" + stat.toLowerCase()), "drawable", MainActivity.this.getPackageName()));
                    imgStat1.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    ImageView imgStat2 = findViewById(R.id.img_stat_2);
                    imgStat2.setImageResource(getResources().getIdentifier(("w20" + stat.toLowerCase()), "drawable", MainActivity.this.getPackageName()));
                    imgStat2.setVisibility(View.VISIBLE);
                    break;
                case 3:
                    ImageView imgStat3 = findViewById(R.id.img_stat_3);
                    imgStat3.setImageResource(getResources().getIdentifier(("w20" + stat.toLowerCase()), "drawable", MainActivity.this.getPackageName()));
                    imgStat3.setVisibility(View.VISIBLE);
                    calculateChance();
                    break;
            }

        }
    }

    private void calculateChance() {
        SharedPreferences prefs = getSharedPreferences(getString(R.string.stats_file_key), MODE_PRIVATE);
        int e1 = prefs.getInt(stats.get(0), 0);
        int e2 = prefs.getInt(stats.get(1), 0);
        int e3 = prefs.getInt(stats.get(2), 0);
        double[] results = ChanceLogic.calculateChance(e1, e2, e3, taw, mod);

        DecimalFormat df = new DecimalFormat("00.##");
        String chance = df.format(results[0] * 100) + "%\n";
        int avrgTap = (int) Math.round(results[1]);
        String result = "Chance: " + chance + "durchschnittliche TaP*: " + avrgTap;
        ((TextView) findViewById(R.id.txtResult)).setText(result);
    }

    private void createDialog(String title, Consumer<Integer> field) { //Consumer um Feld aus Lambda zu aktualisieren
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setCancelable(true);

        View inflatedView = LayoutInflater.from(this).inflate(R.layout.text_input_number, findViewById(R.id.content), false);
        final EditText txtInput = inflatedView.findViewById(R.id.input);
        builder.setView(inflatedView);

        builder.setPositiveButton("OK", (dialog, which) -> {
            dialog.dismiss();
            field.accept(Integer.parseInt(txtInput.getText().toString()));
        });
        builder.setNegativeButton("Abbrechen", (dialog, which) -> dialog.cancel());

        builder.show();
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.action_stats) {
            Intent intent = new Intent(MainActivity.this, StatsActivity.class);
            startActivity(intent);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.app_bar_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

}