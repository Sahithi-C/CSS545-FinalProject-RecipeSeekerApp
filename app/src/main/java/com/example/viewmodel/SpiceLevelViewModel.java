package com.example.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.database.RecipeDatabase;
import com.example.dao.RecipeDao;
import com.example.models.Recipe;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class SpiceLevelViewModel extends AndroidViewModel {
    private final RecipeDao recipeDao;
    private final MutableLiveData<List<RecipeSpiceEntry>> recipesWithSpiceLevel;
    private final ExecutorService executorService;

    public SpiceLevelViewModel(@NonNull Application application) {
        super(application);
        RecipeDatabase database = RecipeDatabase.getDatabase(application);
        recipeDao = database.recipeDao();
        executorService = Executors.newSingleThreadExecutor();

        recipesWithSpiceLevel = new MutableLiveData<>();
        loadRecipesWithSpiceLevel();
    }

    private void loadRecipesWithSpiceLevel() {
        executorService.execute(() -> {
            List<Recipe> recipes = recipeDao.getAllRecipes();
            List<RecipeSpiceEntry> sortedRecipes = recipes.stream()
                    .map(recipe -> new RecipeSpiceEntry(recipe.title, recipe.spiceLevel))
                    .sorted(Comparator.comparing(entry -> entry.spiceLevel))
                    .collect(Collectors.toList());

            recipesWithSpiceLevel.postValue(sortedRecipes);
        });
    }

    public LiveData<List<RecipeSpiceEntry>> getRecipesWithSpiceLevel() {
        return recipesWithSpiceLevel;
    }

    public static class RecipeSpiceEntry {
        public String title;
        public String spiceLevel;

        public RecipeSpiceEntry(String title, String spiceLevel) {
            this.title = title;
            this.spiceLevel = spiceLevel;
        }
    }
}