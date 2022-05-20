package com.rankenstein.dsahelper.logic;

public class Check {
    private String name;
    private String e1;
    private String e2;
    private String e3;
    private int taw;
    private int mod;

    private double passChance;

    private double avrgTap;

    public Check(String name, String e1, String e2, String e3, int taw, int mod, double passChance, double avrgTap) {
        this.name = name;
        this.e1 = e1;
        this.e2 = e2;
        this.e3 = e3;
        this.taw = taw;
        this.mod = mod;
        this.passChance = passChance;
        this.avrgTap = avrgTap;
    }

    public Check(String e1, String e2, String e3, int taw, int mod, double passChance, double avrgTap) {
        this("", e1, e2, e3, taw, mod, passChance, avrgTap);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public int getMod() {
        return mod;
    }

    public void setMod(int mod) {
        this.mod = mod;
    }

    public double getPassChance() {
        return passChance;
    }

    public void setPassChance(double passChance) {
        this.passChance = passChance;
    }

    public double getAvrgTap() {
        return avrgTap;
    }

    public void setAvrgTap(double avrgTap) {
        this.avrgTap = avrgTap;
    }
}
