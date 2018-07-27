package com.lexoid.bakingapp.ui;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.widget.RemoteViews;

import com.google.gson.Gson;
import com.lexoid.bakingapp.R;
import com.lexoid.bakingapp.data.SharedPrefHelper;
import com.lexoid.bakingapp.data.models.Ingredient;
import com.lexoid.bakingapp.data.models.Recipe;

import java.util.ArrayList;

import static com.lexoid.bakingapp.ui.StepListActivity.ARG_RECIPE;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeWidgetProvider extends AppWidgetProvider {

    public static void updateAppWidget(Context context, @Nullable Recipe recipe, String ingredients, AppWidgetManager appWidgetManager,
                                       int[] appWidgetIds) {
        for (int appWidgetId :
                appWidgetIds) {
            // Construct the RemoteViews object
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget);

            PendingIntent pendingIntent;
            if (recipe != null) {
                views.setTextViewText(R.id.widget_title, recipe.getName());
                views.setTextViewText(R.id.widget_ingredients, ingredients);

                Intent stepsIntent = new Intent(context, StepListActivity.class);
                stepsIntent.putExtra(ARG_RECIPE, recipe);
                pendingIntent = PendingIntent.getActivity(context, 0, stepsIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            } else {
                views.setTextViewText(R.id.widget_title, "Select recipe");
                Intent mainIntent = new Intent(context, MainActivity.class);
                pendingIntent = PendingIntent.getActivity(context, 0, mainIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            }

            views.setOnClickPendingIntent(R.id.widget_layout, pendingIntent);
            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        WidgetUpdateService.startActionUpdateWidget(context);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

