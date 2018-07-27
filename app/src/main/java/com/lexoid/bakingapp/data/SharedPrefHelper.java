package com.lexoid.bakingapp.data;

import android.content.Context;
import android.preference.PreferenceManager;

public class SharedPrefHelper {
    private static final String RECIPE_KEY = "recipe";

    public static void saveRecipe(Context context, String jsonRecipe){
        PreferenceManager.getDefaultSharedPreferences(context).edit()
                .putString(RECIPE_KEY, jsonRecipe)
                .apply();
    }

    public static String getRecipeJson(Context context){
        String defVal = "";
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(RECIPE_KEY, defVal);
    }
}
