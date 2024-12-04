package com.example.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "recipes")

public class Recipe {
    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "title")
    public String title;

    @ColumnInfo(name = "image_path")
    public String imagePath;

    @ColumnInfo(name = "is_favorite")
    public boolean isFavorite;

    @ColumnInfo(name = "cooking_time")
    public String cookingTime;

    @ColumnInfo(name = "spice_level")
    public String spiceLevel;

//    @ColumnInfo(name = "allergens")
//    public String allergens;
}


