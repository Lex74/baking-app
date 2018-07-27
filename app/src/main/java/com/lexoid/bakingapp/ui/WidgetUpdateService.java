package com.lexoid.bakingapp.ui;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.lexoid.bakingapp.R;
import com.lexoid.bakingapp.data.SharedPrefHelper;
import com.lexoid.bakingapp.data.models.Ingredient;
import com.lexoid.bakingapp.data.models.Recipe;

import java.util.ArrayList;

public class WidgetUpdateService extends IntentService {

    public WidgetUpdateService() {
        super("WidgetUpdateService");
    }

    public static void startActionUpdateWidget(Context context){
        Intent intent = new Intent(context, WidgetUpdateService.class);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String recipeJson = SharedPrefHelper.getRecipeJson(this);
        Recipe recipe = null;

        StringBuilder stringBuilder = new StringBuilder();
        if (!recipeJson.equals("")){
            Gson gson = new Gson();
            recipe = gson.fromJson(recipeJson, Recipe.class);

            ArrayList<Ingredient> ingredients = recipe.getIngredients();
            for (Ingredient ingredient :
                    ingredients) {
                stringBuilder
                        .append(ingredient.getQuantity())
                        .append(" ")
                        .append(ingredient.getMeasure())
                        .append(" ")
                        .append(ingredient.getIngredient())
                        .append("\n");
            }
        }
        String ingredients = stringBuilder.toString();

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, RecipeWidgetProvider.class));

        RecipeWidgetProvider.updateAppWidget(this, recipe, ingredients, appWidgetManager, appWidgetIds);
    }
}
