package com.lexoid.bakingapp.data;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;

import com.lexoid.bakingapp.data.models.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class DataLoader implements LifecycleObserver{
    private static final String BASE_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/";
    private RecipeData recipeData;

    public DataLoader(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        recipeData = retrofit.create(RecipeData.class);
    }

    public void getRecipes(final RecipeDataListener recipeDataListener){
        recipeData.getRecipes().enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                response.body().get(0).setImage("http://del.h-cdn.co/assets/16/32/1600x800/landscape-1470773544-delish-nutella-cool-whip-pie-1.jpg");
                response.body().get(1).setImage("http://d2gk7xgygi98cy.cloudfront.net/4-3-large.jpg");
                response.body().get(2).setImage("https://dessertswithbenefits.com/wp-content/uploads/2014/01/33.jpg");
                response.body().get(3).setImage("https://www.petitgastro.com.br/wp-content/uploads/2016/04/final2-Medium-1024x682.jpg");
                recipeDataListener.onRecipesReceive(response.body());
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                recipeDataListener.onError(t);
            }
        });
    }

    public interface RecipeDataListener{
        void onRecipesReceive(List<Recipe> recipes);
        void onError(Throwable t);
    }

    private interface RecipeData {
        @GET("baking.json")
        Call<List<Recipe>> getRecipes();
    }
}
