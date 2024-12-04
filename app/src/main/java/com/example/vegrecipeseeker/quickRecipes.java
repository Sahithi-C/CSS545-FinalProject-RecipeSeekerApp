package com.example.vegrecipeseeker;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.example.viewmodel.QuickRecipesViewModel;

import java.util.List;
import java.util.stream.Collectors;

public class quickRecipes extends AppCompatActivity {
    private QuickRecipesViewModel viewModel;
    private ListView listView;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_recipes);

        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Quick Recipes");
        }

        // Initialize ListView
        listView = findViewById(R.id.quickRecipesListView);

        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(QuickRecipesViewModel.class);

        // Observe and display recipes
        viewModel.getRecipesWithTime().observe(this, recipeTimeList -> {
            // Convert to display format
            List<String> displayList = recipeTimeList.stream()
                    .map(entry -> entry.title + " (" + entry.originalTime + ")")
                    .collect(Collectors.toList());

            adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_list_item_1, displayList);
            listView.setAdapter(adapter);
        });

        // Handle recipe selection
        listView.setOnItemClickListener((parent, view, position, id) -> {
            QuickRecipesViewModel.RecipeTimeEntry selectedRecipe =
                    viewModel.getRecipesWithTime().getValue().get(position);

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