package com.example.vegrecipeseeker;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.example.viewmodel.SpiceLevelViewModel;

import java.util.List;
import java.util.stream.Collectors;

public class spiceLevel extends AppCompatActivity {
    private SpiceLevelViewModel viewModel;
    private ListView listView;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.activity_spice_level_land);
        } else {
            setContentView(R.layout.activity_spice_level);
        }

        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Spice Level Recipes");
        }

        // Initialize ListView
        listView = findViewById(R.id.spiceLevelListView);

        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(SpiceLevelViewModel.class);

        // Observe and display recipes
        viewModel.getRecipesWithSpiceLevel().observe(this, recipeSpiceList -> {
            // Convert to display format
            List<String> displayList = recipeSpiceList.stream()
                    .map(entry -> entry.title + " (Spice Level: " + entry.spiceLevel + ")")
                    .collect(Collectors.toList());

            adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_list_item_1, displayList);
            listView.setAdapter(adapter);
        });

        // Handle recipe selection
        listView.setOnItemClickListener((parent, view, position, id) -> {
            SpiceLevelViewModel.RecipeSpiceEntry selectedRecipe =
                    viewModel.getRecipesWithSpiceLevel().getValue().get(position);

            Intent intent = new Intent(this, RecipeDetailActivity.class);
            intent.putExtra("RECIPE_TITLE", selectedRecipe.title);
            startActivity(intent);
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}