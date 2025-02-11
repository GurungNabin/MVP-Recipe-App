package com.example.recipebook;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class ViewPageActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe);



        // Load the first fragment initially
        if (savedInstanceState == null) {
            loadFragment(new RecipeNameFragment());
        }
    }

    public void loadFragment(Fragment fragment) {
        // Replacing current fragment with the new one
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null); // Add to back stack to allow back navigation
        transaction.commit();
    }


}
