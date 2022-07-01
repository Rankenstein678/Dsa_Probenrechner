package com.rankenstein.dsahelper.logic;

import android.content.SharedPreferences;
import com.rankenstein.dsahelper.ui.CalculatorActivity;

public class Check {
    private String name, e1, e2, e3;
    private int taw, mod, avrgTap;

    private double chance;

    public Check(String name, String e1, String e2, String e3, int taw, int mod) {
        this.name = name;
        this.e1 = e1;
        this.e2 = e2;
        this.e3 = e3;
        this.taw = taw;
        this.mod = mod;

        calculate();
    }

    public void calculate() {
        SharedPreferences prefs = CalculatorActivity.prefsStats;
        int numE1 = prefs.getInt(e1, 0);
        int numE2 = prefs.getInt(e1, 0);
        int numE3 = prefs.getInt(e1, 0);
        chance = ChanceLogic.calculateChance(numE1, numE2, numE3, taw, mod)[0];
    }

    public String getE1() {
        return e1;
    }

    public void setE1(String e1) {
        this.e1 = e1;
    }

    public String getE2() {
        return e2;
    }

    public void setE2(String e2) {
        this.e2 = e2;
    }

    public String getE3() {
        return e3;
    }

    public void setE3(String e3) {
        this.e3 = e3;
    }

    public int getTaw() {
        return taw;
    }

    public void setTaw(int taw) {
        this.taw = taw;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAvrgTap() {
        return avrgTap;
    }

    public void setAvrgTap(int avrgTap) {
        this.avrgTap = avrgTap;
    }

    public double getChance() {
        return chance;
    }

    public void setChance(double chance) {
        this.chance = chance;
    }

    public int getMod() {return mod;}

    public void setMod(int mod) {this.mod = mod;}
}
