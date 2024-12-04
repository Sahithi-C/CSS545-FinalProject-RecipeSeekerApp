package com.example.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.dao.RecipeDao;
import com.example.data.RecipeWithDetails;
import com.example.database.RecipeDatabase;
//import com.example.models.Allergen;
import com.example.models.Ingredient;
import com.example.models.Instruction;
import com.example.models.Recipe;
//import com.example.data.AllergenDao;
//import com.example.models.Allergen;
//import com.example.models.RecipeAllergenCrossRef;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class RecipeRepository {
    private final RecipeDao recipeDao;
    private final ExecutorService executorService;

    public RecipeRepository(Application application) {
        RecipeDatabase db = RecipeDatabase.getDatabase(application);
        recipeDao = db.recipeDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void insertRecipe(String title, String imagePath, String cookingTime,
                             String spiceLevel, List<String> mandatoryIngredients,
                             List<String> optionalIngredients, List<String> instructions) {
        executorService.execute(() -> {
            // Create Recipe
            Recipe recipe = new Recipe();
            recipe.title = title;
            recipe.imagePath = imagePath;
            recipe.cookingTime = cookingTime;
            recipe.spiceLevel = spiceLevel;

            // Create Ingredients
            List<Ingredient> ingredientList = new ArrayList<>();
            for (String mandatoryIngredient : mandatoryIngredients) {
                Ingredient ingredient = new Ingredient();
                ingredient.ingredientText = mandatoryIngredient;
                ingredient.isMandatory = true;
                ingredientList.add(ingredient);
            }
            for (String optionalIngredient : optionalIngredients) {
                Ingredient ingredient = new Ingredient();
                ingredient.ingredientText = optionalIngredient;
                ingredient.isMandatory = false;
                ingredientList.add(ingredient);
            }

            // Create Instructions
            List<Instruction> instructionList = new ArrayList<>();
            for (int i = 0; i < instructions.size(); i++) {
                Instruction instruction = new Instruction();
                instruction.stepNumber = i + 1;
                instruction.instructionText = instructions.get(i);
                instructionList.add(instruction);
            }

            recipeDao.insertRecipeWithDetails(recipe, ingredientList, instructionList);
        });
    }

    public LiveData<RecipeWithDetails> getRecipeByTitle(String title) {
        return new LiveData<RecipeWithDetails>() {
            @Override
            protected void onActive() {
                super.onActive();
                executorService.execute(() -> {
                    RecipeWithDetails recipe = recipeDao.getRecipeWithDetailsByTitle(title);
                    postValue(recipe);
                });
            }
        };
    }

    public LiveData<List<String>> getAllRecipeTitles() {
        return new LiveData<List<String>>() {
            @Override
            protected void onActive() {
                super.onActive();
                executorService.execute(() -> {
                    List<Recipe> recipes = recipeDao.getAllRecipes();
                    List<String> titles = recipes.stream()
                            .map(recipe -> recipe.title)
                            .collect(Collectors.toList());
                    postValue(titles);
                });
            }
        };
    }

    public void updateFavoriteStatus(long recipeId, boolean isFavorite) {
        executorService.execute(() -> {
            recipeDao.updateFavoriteStatus(recipeId, isFavorite);
        });
    }

    public LiveData<Integer> getFavoritesCount() {
        return new LiveData<Integer>() {
            @Override
            protected void onActive() {
                super.onActive();
                executorService.execute(() -> {
                    int count = recipeDao.getFavoritesCount();
                    postValue(count);
                });
            }
        };
    }

    public LiveData<List<String>> getFavoriteRecipes() {
        return new LiveData<List<String>>() {
            @Override
            protected void onActive() {
                super.onActive();
                executorService.execute(() -> {
                    List<Recipe> favorites = recipeDao.getFavoriteRecipes();
                    List<String> titles = favorites.stream()
                            .map(recipe -> recipe.title)
                            .collect(Collectors.toList());
                    postValue(titles);
                });
            }
        };
    }

//    public LiveData<List<Allergen>> getAllergensByRecipeId(long recipeId) {
//        return new LiveData<List<Allergen>>() {
//            @Override
//            protected void onActive() {
//                super.onActive();
//                executorService.execute(() -> {
//                    List<Allergen> allergens = recipeDao.getAllergensByRecipeId(recipeId);
//                    postValue(allergens);
//                });
//            }
//        };
//    }

//    public void insertAllergen(Allergen allergen) {
//        executorService.execute(() -> recipeDao.insertAllergen(allergen));
//    }
//
//    public void linkRecipeWithAllergen(long recipeId, long allergenId) {
//        executorService.execute(() -> {
//            RecipeAllergenCrossRef crossRef = new RecipeAllergenCrossRef(recipeId, allergenId);
//            recipeDao.insertRecipeAllergenCrossRef(crossRef);
//        });
//    }
//
//    public void updateAllergensForRecipe(long recipeId, List<Allergen> allergens) {
//        executorService.execute(() -> {
//            // Delete old allergens
//            recipeDao.deleteAllergensByRecipeId(recipeId);
//
//            // Insert new cross-references
//            for (Allergen allergen : allergens) {
//                RecipeAllergenCrossRef crossRef = new RecipeAllergenCrossRef(recipeId, allergen.id);
//                recipeDao.insertRecipeAllergenCrossRef(crossRef);
//            }
//        });
//    }
}
