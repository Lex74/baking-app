package com.lexoid.bakingapp.data.models;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.lexoid.bakingapp.data.DataLoader;

import java.util.List;

public class RecipeViewModel extends ViewModel {

    MutableLiveData<List<Recipe>> data;

    public LiveData<List<Recipe>> getData(){
        if (data == null){
            data = new MutableLiveData<>();
            loadData();
        }
        return data;
    }

    private void loadData(){
        DataLoader dataLoader = new DataLoader();
        dataLoader.getRecipes(new DataLoader.RecipeDataListener() {
            @Override
            public void onRecipesReceive(List<Recipe> recipes) {
                data.postValue(recipes);
            }

            @Override
            public void onError(Throwable t) {

            }
        });
    }
}
