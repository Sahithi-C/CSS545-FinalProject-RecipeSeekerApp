package com.example.vegrecipeseeker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.example.viewmodel.SearchViewModel;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    private ListView listView;
    private ArrayAdapter<String> arrayAdapter;
    private SearchViewModel searchViewModel;
    private SearchView searchView;
    private String savedSearchQuery = "";
    private int savedScrollPosition = 0;
    private static final String PREFS_NAME = "SearchActivityPrefs";
    private static final String KEY_SEARCH_QUERY = "search_query";
    private static final String KEY_SCROLL_POSITION = "scroll_position";
    private Menu optionsMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Initialize ViewModel
        searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);

        // Add sample data if the database is empty
        searchViewModel.getAllRecipeTitles().observe(this, titles -> {
            if (titles.isEmpty()) {
                searchViewModel.insertSampleData();
            }
        });

        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Setup ListView
        listView = findViewById(R.id.listView);
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<>());
        listView.setAdapter(arrayAdapter);

        // Observe recipe titles
        searchViewModel.getAllRecipeTitles().observe(this, titles -> {
            arrayAdapter.clear();
            arrayAdapter.addAll(titles);
        });

        // Handle recipe selection
        listView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedRecipe = arrayAdapter.getItem(position);
            Intent intent = new Intent(SearchActivity.this, RecipeDetailActivity.class);
            intent.putExtra("RECIPE_TITLE", selectedRecipe);
            startActivity(intent);
        });

        // Restore state from either savedInstanceState or SharedPreferences
        restoreState(savedInstanceState);
    }

    private void restoreState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            // Restore from saved instance state (for configuration changes)
            savedSearchQuery = savedInstanceState.getString(KEY_SEARCH_QUERY, "");
            savedScrollPosition = savedInstanceState.getInt(KEY_SCROLL_POSITION, 0);
        } else {
            // Restore from SharedPreferences (for app suspension/resume)
            SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            savedSearchQuery = prefs.getString(KEY_SEARCH_QUERY, "");
            savedScrollPosition = prefs.getInt(KEY_SCROLL_POSITION, 0);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Apply saved state if search view is initialized
        if (searchView != null && !savedSearchQuery.isEmpty()) {
            searchView.setQuery(savedSearchQuery, true);
        }
        // Restore scroll position
        if (listView != null) {
            listView.setSelection(savedScrollPosition);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Save state to SharedPreferences
        saveCurrentState();
    }

    private void saveCurrentState() {
        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
        editor.putString(KEY_SEARCH_QUERY, savedSearchQuery);
        editor.putInt(KEY_SCROLL_POSITION, listView.getFirstVisiblePosition());
        editor.apply();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_menu, menu);
        this.optionsMenu = menu;
        MenuItem menuItem = menu.findItem(R.id.search);
        searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Search here");

        // Restore search query if available
        if (!savedSearchQuery.isEmpty()) {
            menuItem.expandActionView();
            searchView.setQuery(savedSearchQuery, false);
            searchView.setIconified(false);
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                savedSearchQuery = newText;
                searchViewModel.searchRecipes(newText).observe(SearchActivity.this, filteredTitles -> {
                    arrayAdapter.clear();
                    arrayAdapter.addAll(filteredTitles);
                });
                return false;
            }
        });

        // Set up SearchView expansion listener
        menuItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                savedSearchQuery = ""; // Clear saved query when search is closed
                saveCurrentState();
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(SearchActivity.this, homeScreen.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_SEARCH_QUERY, savedSearchQuery);
        outState.putInt(KEY_SCROLL_POSITION, listView.getFirstVisiblePosition());
    }
}