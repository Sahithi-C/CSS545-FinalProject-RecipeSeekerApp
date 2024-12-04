package com.example.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.database.RecipeDatabase;
import com.example.dao.RecipeDao;
import com.example.models.Recipe;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class RecipesListViewModel extends AndroidViewModel {
    private final RecipeDao recipeDao;
    private final MutableLiveData<List<String>> recipeTitles;
    private final ExecutorService executorService;

    public RecipesListViewModel(@NonNull Application application) {
        super(application);
        RecipeDatabase database = RecipeDatabase.getDatabase(application);
        recipeDao = database.recipeDao();
        executorService = Executors.newSingleThreadExecutor();
        recipeTitles = new MutableLiveData<>();
        loadRecipes();
    }

    private void loadRecipes() {
        executorService.execute(() -> {
            List<Recipe> recipes = recipeDao.getAllRecipes();
            List<String> titles = recipes.stream()
                    .map(recipe -> recipe.title)
                    .collect(Collectors.toList());
            recipeTitles.postValue(titles);
        });
    }

    public LiveData<List<String>> getRecipeTitles() {
        return recipeTitles;
    }

    public void refreshRecipes() {
        loadRecipes();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        executorService.shutdown();
    }
}