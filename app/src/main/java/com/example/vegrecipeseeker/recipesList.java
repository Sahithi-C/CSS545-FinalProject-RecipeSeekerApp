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

import com.example.viewmodel.RecipesListViewModel;

public class recipesList extends AppCompatActivity {
    private ListView recipesListView;
    private ArrayAdapter<String> adapter;
    private RecipesListViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.activity_recipes_list_land);
        } else {
            setContentView(R.layout.activity_recipes_list);
        }

        // Set up the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("All Recipes");
        }

        // Initialize ListView
        recipesListView = findViewById(R.id.recipesListView);

        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(RecipesListViewModel.class);

        // Create adapter
        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1);
        recipesListView.setAdapter(adapter);

        // Observe recipe titles
        viewModel.getRecipeTitles().observe(this, titles -> {
            adapter.clear();
            adapter.addAll(titles);
        });

        // Set click listener for recipe items
        recipesListView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedRecipe = adapter.getItem(position);
            Intent intent = new Intent(recipesList.this, RecipeDetailActivity.class);
            intent.putExtra("RECIPE_TITLE", selectedRecipe);
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

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh the list when returning to this activity
        viewModel.refreshRecipes();
    }
}