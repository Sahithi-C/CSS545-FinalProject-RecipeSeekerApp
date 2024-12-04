package com.example.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.data.RecipeWithDetails;
import com.example.dao.RecipeDao;
import com.example.database.RecipeDatabase;
import com.example.models.Recipe;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FavouritesViewModel extends AndroidViewModel {
    private final RecipeDao recipeDao;
    private LiveData<List<Recipe>> favoriteRecipes;
    private final ExecutorService executorService;

    public FavouritesViewModel(Application application) {
        super(application);
        RecipeDatabase database = RecipeDatabase.getDatabase(application);
        recipeDao = database.recipeDao();
        executorService = Executors.newSingleThreadExecutor();

        // Convert List to LiveData
        favoriteRecipes = new MutableLiveData<>();
        loadFavoriteRecipes();
    }

    public void loadFavoriteRecipes() {
        executorService.execute(() -> {
            List<Recipe> recipes = recipeDao.getFavoriteRecipes();
            ((MutableLiveData<List<Recipe>>) favoriteRecipes).postValue(recipes);
        });
    }

    public LiveData<List<Recipe>> getFavoriteRecipes() {
        return favoriteRecipes;
    }

    public void removeFromFavorites(long recipeId) {
        executorService.execute(() -> {
            // Update favorite status in the database
            recipeDao.updateFavoriteStatus(recipeId, false);

            // Reload the favorite recipes list
            loadFavoriteRecipes();
        });
    }
}
