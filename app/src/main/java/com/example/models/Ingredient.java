package com.example.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "ingredients",
        foreignKeys = @ForeignKey(
                entity = Recipe.class,
                parentColumns = "id",
                childColumns = "recipe_id",
                onDelete = ForeignKey.CASCADE
        ))

public class Ingredient {
    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "recipe_id")
    public long recipeId;

    @ColumnInfo(name = "ingredient_text")
    public String ingredientText;

    @ColumnInfo(name = "is_mandatory")
    public boolean isMandatory;
}
