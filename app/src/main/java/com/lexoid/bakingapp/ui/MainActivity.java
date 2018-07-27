package com.lexoid.bakingapp.ui;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.lexoid.bakingapp.R;
import com.lexoid.bakingapp.data.DataLoader;
import com.lexoid.bakingapp.data.models.RecipeViewModel;
import com.lexoid.bakingapp.data.models.Recipe;
import com.lexoid.bakingapp.ui.adapter.RecipeAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements
        DataLoader.RecipeDataListener,
        RecipeAdapter.ItemClickListener{
    @BindView(R.id.recipe_recycler)
    RecyclerView recipeRecycler;

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;
    private DataLoader dataLoader;

    @Nullable
    SimpleIdlingResource idlingResource = new SimpleIdlingResource();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        recipeRecycler.setItemAnimator(new DefaultItemAnimator());

        dataLoader = new DataLoader();

        initSwipeRefreshLayout();

        getData();
    }

    @VisibleForTesting
    public IdlingResource getIdlingResource() {
        return idlingResource;
    }

    private void getData(){
        if (idlingResource != null) {
            idlingResource.setIdleState(false);
        }
        dataLoader.getRecipes(this);
    }

    private void initSwipeRefreshLayout() {
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        swipeRefreshLayout.setRefreshing(true);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });
    }

    @Override
    public void onRecipesReceive(List<Recipe> recipes) {
        swipeRefreshLayout.setRefreshing(false);
        if (idlingResource != null) {
            idlingResource.setIdleState(true);
        }
        recipeRecycler.setAdapter(new RecipeAdapter(recipes, MainActivity.this));
    }

    @Override
    public void onError(Throwable t) {
        idlingResource.setIdleState(false);
        swipeRefreshLayout.setRefreshing(false);
        Toast.makeText(this, t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onItemClick(Recipe recipe) {
        Intent intent = new Intent(this, StepListActivity.class);
        intent.putExtra(StepListActivity.ARG_RECIPE, recipe);
        startActivity(intent);
    }
}
