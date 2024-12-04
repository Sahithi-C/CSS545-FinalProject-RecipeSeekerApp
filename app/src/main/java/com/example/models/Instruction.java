package com.example.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "instructions",
        foreignKeys = @ForeignKey(
                entity = Recipe.class,
                parentColumns = "id",
                childColumns = "recipe_id",
                onDelete = ForeignKey.CASCADE
        ))

public class Instruction {
    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "recipe_id")
    public long recipeId;

    @ColumnInfo(name = "step_number")
    public int stepNumber;

    @ColumnInfo(name = "instruction_text")
    public String instructionText;
}

