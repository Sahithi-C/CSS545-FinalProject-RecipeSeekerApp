package com.example.vegrecipeseeker;

import static androidx.core.content.ContextCompat.startActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import android.view.ViewGroup;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import androidx.appcompat.widget.Toolbar;

public class homeScreen extends BaseActivity {

    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_screen);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,  R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Set home as checked in navigation view by default
        navigationView.setCheckedItem(R.id.nav_home);

        navigationView.setNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                // Just close the drawer when home is selected
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
//            else if (itemId == R.id.nav_settings) {
//                Intent settingsIntent = new Intent(homeScreen.this, settings.class);
//                startActivity(settingsIntent);
//                drawerLayout.closeDrawer(GravityCompat.START);
//                return true;
//            }
            else if (itemId == R.id.nav_about) {
                showAboutUsDialog();
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
            else if (itemId == R.id.nav_logout) {
                new AlertDialog.Builder(this)
                        .setTitle("Logout")
                        .setMessage("Are you sure you want to logout?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            Intent intent = new Intent(homeScreen.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        })
                        .setNegativeButton("No", null)
                        .show();
                return true;
            }

            return false;
        });

        // Initialize buttons
        Button recipesListButton = findViewById(R.id.recipesListButton);
        recipesListButton.setOnClickListener(v -> {
            Intent intent = new Intent(homeScreen.this, recipesList.class);
            startActivity(intent);
        });

        Button favouritesButton = findViewById(R.id.favouritesButton);
        favouritesButton.setOnClickListener(v -> {
            Intent intent = new Intent(homeScreen.this, Favourites.class);
            startActivity(intent);
        });

        Button quickRecipesButton = findViewById(R.id.quickRecipesButton);
        quickRecipesButton.setOnClickListener(v -> {
            Intent intent = new Intent(homeScreen.this, quickRecipes.class);
            startActivity(intent);
        });

        Button spiceLevelButton = findViewById(R.id.spiceLevelButton);
        spiceLevelButton.setOnClickListener(v -> {
            Intent intent = new Intent(homeScreen.this, spiceLevel.class);
            startActivity(intent);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Search here");

        searchView.setOnSearchClickListener(view -> {
            Intent intent = new Intent(homeScreen.this, SearchActivity.class);
            startActivity(intent);
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void showAboutUsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("About Us");
        builder.setMessage("Welcome to Vegetarian Recipe Seeker!\n\n" +
                "We're here to make your cooking journey simple, quick, and safe. Our app is designed to help you find delicious recipes without the hassle of scrolling through lengthy videos or websites.\n\n" +
                "Your safety is our priority – we highlight allergens and important warnings before you view any recipe, so you can cook with confidence. Plus, with the option to save all your favorite recipes in one place, meal planning becomes effortless and organized.\n\n" +
                "Whether you're meal prepping or just looking for quick inspiration, we're here to make your kitchen time more enjoyable.\n\n" +
                "Happy cooking!");
        builder.setPositiveButton("OK", null);
        builder.create().show();
    }
}