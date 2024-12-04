package com.example.vegrecipeseeker;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.example.viewmodel.FavouritesViewModel;

public class Favourites extends AppCompatActivity {
    private FavouritesViewModel favouritesViewModel;
    private ListView favouritesListView;
    private TextView emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.activity_favourites_land);
        } else {
            setContentView(R.layout.activity_favourites);
        }

        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.favourites_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        // Initialize views
        initializeViews();

        // Initialize ViewModel
        favouritesViewModel = new ViewModelProvider(this).get(FavouritesViewModel.class);

        // Load and display favourites
        loadFavourites();
    }

    private void initializeViews() {
        favouritesListView = findViewById(R.id.favouritesListView);
        emptyView = findViewById(R.id.emptyFavouritesText);

        // Setup back button click listener
        findViewById(R.id.backButton).setOnClickListener(v -> onBackPressed());
    }

    private void loadFavourites() {
        favouritesViewModel.getFavoriteRecipes().observe(this, favoriteRecipes -> {
            if (favoriteRecipes.isEmpty()) {
                favouritesListView.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
            } else {
                favouritesListView.setVisibility(View.VISIBLE);
                emptyView.setVisibility(View.GONE);

                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_list_item_1,
                        favoriteRecipes.stream()
                                .map(recipe -> recipe.title)
                                .collect(java.util.stream.Collectors.toList())
                );
                favouritesListView.setAdapter(adapter);

                // Navigate to recipe details when a favourite is clicked
                favouritesListView.setOnItemClickListener((parent, view, position, id) -> {
                    String selectedRecipe = favoriteRecipes.get(position).title;
                    Intent intent = new Intent(Favourites.this, RecipeDetailActivity.class);
                    intent.putExtra("RECIPE_TITLE", selectedRecipe);
                    startActivity(intent);
                });
            }
        });
    }

    public void refreshFavorites() {
        favouritesViewModel.loadFavoriteRecipes();
    }
}