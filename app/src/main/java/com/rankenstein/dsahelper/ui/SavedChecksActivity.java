package com.rankenstein.dsahelper.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rankenstein.dsahelper.R;
import com.rankenstein.dsahelper.logic.Check;
import com.rankenstein.dsahelper.logic.Constants;

import java.util.ArrayList;
import java.util.Objects;

public class SavedChecksActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_checks);

        try {
            Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.saved_checks_activity_title);
        } catch (NullPointerException e) {
            Log.d("DSA-Probenrechner", "Couldn't get Action Bar");
        }
    }

    @Override
    protected void onStart() {
        RecyclerView rec = findViewById(R.id.rec);

        Gson gson = new Gson();
        String serializedList = CalculatorActivity.prefsChecks.getString(Constants.KEY_CHECKS, "");
        ArrayList<Check> checks;
        if (serializedList.isEmpty()) checks = new ArrayList<>();
        else checks = gson.fromJson(serializedList, TypeToken.getParameterized(ArrayList.class, Check.class).getType());
        if (checks.isEmpty()) {
            findViewById(R.id.txtEmpty).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.txtEmpty).setVisibility(View.GONE);
        }

        rec.setAdapter(new SavedChecksAdapter(checks,this,rec));
        rec.setLayoutManager(new LinearLayoutManager(this));
        super.onStart();
    }

    public void loadCheck(Check c) {
        Intent intent = new Intent(SavedChecksActivity.this, CalculatorActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("check",c.getE1()+","+c.getE2()+","+c.getE3()+","+c.getTaw()+","+c.getMod());
        startActivity(intent);
    }
}