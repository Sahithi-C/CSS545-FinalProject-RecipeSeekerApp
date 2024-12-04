package com.example.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.data.RecipeWithDetails;
//import com.example.models.Allergen;
import com.example.repository.RecipeRepository;

import java.util.List;

public class RecipeViewModel extends AndroidViewModel {
    private final RecipeRepository repository;

    public RecipeViewModel(Application application) {
        super(application);
        repository = new RecipeRepository(application);
    }

    public LiveData<RecipeWithDetails> getRecipeByTitle(String title) {
        return repository.getRecipeByTitle(title);
    }

    public LiveData<Integer> getFavoritesCount() {
        return repository.getFavoritesCount();
    }

    public void updateFavoriteStatus(long recipeId, boolean isFavorite) {
        repository.updateFavoriteStatus(recipeId, isFavorite);
    }

//    public LiveData<List<Allergen>> getAllergensByRecipeId(long recipeId) {
//        return repository.getAllergensByRecipeId(recipeId);
//    }
}