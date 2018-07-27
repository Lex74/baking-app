package com.lexoid.bakingapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lexoid.bakingapp.R;
import com.lexoid.bakingapp.data.SharedPrefHelper;
import com.lexoid.bakingapp.data.models.Recipe;
import com.lexoid.bakingapp.data.models.Step;
import com.lexoid.bakingapp.ui.adapter.IngredientsAdapter;
import com.lexoid.bakingapp.ui.adapter.StepsAdapter;

import java.util.ArrayList;

/**
 * An activity representing a list of Steps. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link StepDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class StepListActivity extends AppCompatActivity implements StepsAdapter.StepClickListener {
    public static final String ARG_RECIPE = "recipe";

    private Recipe recipe;
    private boolean twoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_list);

        Toolbar appBarLayout = findViewById(R.id.toolbar);
        setSupportActionBar(appBarLayout);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        TextView title = findViewById(R.id.title);

        if (findViewById(R.id.step_detail_container) != null) {
            twoPane = true;
        }

        if (getIntent().getExtras() != null){
            recipe = getIntent().getParcelableExtra(ARG_RECIPE);
            title.setText(recipe.getName());
        } else {
            finish();
        }

        RecyclerView stepRecyclerView = findViewById(R.id.step_list);
        RecyclerView ingredientsRecyclerView = findViewById(R.id.ingredient_list);
        setupIngredientRecyclerView(ingredientsRecyclerView);
        setupStepRecyclerView(stepRecyclerView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.step_list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        } else if (id == R.id.to_widget_menu_item){
            Gson gson = new Gson();
            String jsonRecipe = gson.toJson(recipe);
            SharedPrefHelper.saveRecipe(this, jsonRecipe);
            WidgetUpdateService.startActionUpdateWidget(this);
            Toast.makeText(this, R.string.recipe_selected, Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupStepRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new StepsAdapter(recipe.getSteps(), this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    private void setupIngredientRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new IngredientsAdapter(recipe.getIngredients()));
    }

    @Override
    public void onStepClick(ArrayList<Step> steps, int position) {
        Step selectedStep = steps.get(position);
        if (twoPane){
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction()
                    .replace(R.id.step_detail_container,
                            StepDetailFragment.getFragment(selectedStep),
                            selectedStep.getShortDescription())
                    .commit();
        } else {
            Intent intent = new Intent(this, StepDetailActivity.class);
            intent.putParcelableArrayListExtra(StepDetailFragment.ARG_STEPS, steps);
            intent.putExtra(StepDetailFragment.ARG_STEP_ID, position);

            startActivity(intent);
        }
    }


}
