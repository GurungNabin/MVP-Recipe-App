

package com.example.recipebook;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.recipebook.recipe.RecipeFragment;
import com.example.recipebook.home.view.HomeFragment;
import com.example.recipebook.Post.PostFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        String navigateTo = getIntent().getStringExtra("navigateTo");

        Fragment selectedFragment = new HomeFragment();
        if ("foodFragment".equals(navigateTo)) {
            selectedFragment = new RecipeFragment();
            bottomNavigationView.setSelectedItemId(R.id.nav_recipe);
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, selectedFragment)
                .commitNow();

        bottomNavigationView.setOnItemSelectedListener(this::onNavigationItemSelected);
    }



    private boolean onNavigationItemSelected(MenuItem item) {
        Log.d("MainActivity", "Navigation item selected: " + item.getItemId());


        if (item.getItemId() == R.id.nav_home) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new HomeFragment())
                    .commit();
            return true;
        } else if (item.getItemId() == R.id.nav_recipe) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new RecipeFragment())
                    .commit();
            return true;
        } else if (item.getItemId() == R.id.nav_post) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new PostFragment())
                    .commit();
            return true;
        }
        return false;
    }
}
