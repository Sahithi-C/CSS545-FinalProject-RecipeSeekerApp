package com.example.data;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.models.Ingredient;
import com.example.models.Instruction;
import com.example.models.Recipe;

import java.util.List;

public class RecipeWithDetails {
    @Embedded
    public Recipe recipe;

    @Relation(
            parentColumn = "id",
            entityColumn = "recipe_id"
    )
    public List<Ingredient> ingredients;

    @Relation(
            parentColumn = "id",
            entityColumn = "recipe_id"
    )
    public List<Instruction> instructions;
}
