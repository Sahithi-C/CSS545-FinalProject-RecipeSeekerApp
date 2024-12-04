package com.example.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.dao.RecipeDao;
//import com.example.data.AllergenDao;
//import com.example.models.Allergen;
import com.example.models.Ingredient;
import com.example.models.Instruction;
import com.example.models.Recipe;
//import com.example.models.RecipeAllergenCrossRef;

@Database(entities = {Recipe.class, Ingredient.class, Instruction.class,
        //Allergen.class, RecipeAllergenCrossRef.class
}, version = 1)
public abstract class RecipeDatabase extends RoomDatabase {
    public abstract RecipeDao recipeDao();
    //public abstract AllergenDao allergenDao();

    private static volatile RecipeDatabase INSTANCE;

    public static RecipeDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (RecipeDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            RecipeDatabase.class,
                            "recipe_database"
                    ).build();
                }
            }
        }
        return INSTANCE;
    }
}


