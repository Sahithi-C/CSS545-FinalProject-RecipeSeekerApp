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

public class QuickRecipesViewModel extends AndroidViewModel {
    private final RecipeDao recipeDao;
    private final MutableLiveData<List<RecipeTimeEntry>> recipesWithTime;
    private final ExecutorService executorService;

    public QuickRecipesViewModel(@NonNull Application application) {
        super(application);
        RecipeDatabase database = RecipeDatabase.getDatabase(application);
        recipeDao = database.recipeDao();
        executorService = Executors.newSingleThreadExecutor();

        recipesWithTime = new MutableLiveData<>();
        loadRecipesWithTime();
    }

    private void loadRecipesWithTime() {
        executorService.execute(() -> {
            List<Recipe> recipes = recipeDao.getAllRecipes();
            List<RecipeTimeEntry> sortedRecipes = recipes.stream()
                    .map(recipe -> new RecipeTimeEntry(
                            recipe.title,
                            recipe.cookingTime,
                            parseTimeToMinutes(recipe.cookingTime)
                    ))
                    .sorted(Comparator.comparing(entry -> entry.timeInMinutes))
                    .collect(Collectors.toList());

            recipesWithTime.postValue(sortedRecipes);
        });
    }

    private int parseTimeToMinutes(String cookingTime) {
        try {
            String numericPart = cookingTime.replaceAll("[^0-9]", "");
            return Integer.parseInt(numericPart);
        } catch (Exception e) {
            return Integer.MAX_VALUE;
        }
    }

    public LiveData<List<RecipeTimeEntry>> getRecipesWithTime() {
        return recipesWithTime;
    }

    public static class RecipeTimeEntry {
        public String title;
        public String originalTime;
        public int timeInMinutes;

        public RecipeTimeEntry(String title, String originalTime, int timeInMinutes) {
            this.title = title;
            this.originalTime = originalTime;
            this.timeInMinutes = timeInMinutes;
        }
    }
}