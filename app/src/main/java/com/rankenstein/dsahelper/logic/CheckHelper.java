package com.rankenstein.dsahelper.logic;

import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rankenstein.dsahelper.ui.CalculatorActivity;

import java.util.ArrayList;

public class CheckHelper {
    public static ArrayList<Check> getChecks() {
        String serializedList = CalculatorActivity.prefsChecks.getString(Constants.KEY_CHECKS, "");
        Gson gson = new Gson();
        return serializedList.isEmpty() ? new ArrayList<>() : gson.fromJson(serializedList, TypeToken.getParameterized(ArrayList.class, Check.class).getType());

    }

    public static void appendCheck(Check check) {
        Gson gson = new Gson();
        ArrayList<Check> checks = getChecks();
        checks.add(check);
        SharedPreferences.Editor e = CalculatorActivity.prefsChecks.edit();
        e.putString(Constants.KEY_CHECKS,gson.toJson(checks));
        e.apply();
    }

    public static void deleteCheck(int index) {
        Gson gson = new Gson();
        ArrayList<Check> checks = getChecks();
        checks.remove(index);
        SharedPreferences.Editor e = CalculatorActivity.prefsChecks.edit();
        e.putString(Constants.KEY_CHECKS,gson.toJson(checks));
        e.apply();
    }

}
