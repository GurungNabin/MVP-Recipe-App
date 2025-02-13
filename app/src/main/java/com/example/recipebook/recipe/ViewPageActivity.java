package com.example.recipebook.recipe;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.recipebook.MainActivity;
import com.example.recipebook.R;
import com.example.recipebook.database.DBHelper;
import com.example.recipebook.model.Recipe;

import java.util.ArrayList;

public class ViewPageActivity extends AppCompatActivity {

    Recipe myRecipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe);
        myRecipes = new Recipe();
        if (savedInstanceState == null) {
            loadFragment(new RecipeNameFragment());
        }

        Button nextButton = findViewById(R.id.idBtnNext);

        nextButton.setOnClickListener(v -> {
            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);

            if (currentFragment instanceof RecipeNameFragment) {
                // Collect data from RecipeNameFragment
                RecipeNameFragment recipeNameFragment = (RecipeNameFragment) currentFragment;
                Recipe data = recipeNameFragment.collectDataFromNameFragment();
                if (data != null) {
                    myRecipes.setName(data.getName());
                    myRecipes.setCuisine(data.getCuisine());
                    myRecipes.setTags(data.getTags());
                    myRecipes.setMealType(data.getMealType());
                    myRecipes.setImage(data.getImage());
                    RecipeOtherFragment otherFragment = new RecipeOtherFragment();
                    loadFragment(otherFragment);
                }
            } else if (currentFragment instanceof RecipeOtherFragment) {
                // Collect data from RecipeOtherFragment
                RecipeOtherFragment recipeOtherFragment = (RecipeOtherFragment) currentFragment;
                Recipe dataFromOtherFragment = recipeOtherFragment.collectDataFromFragment();

                if (dataFromOtherFragment != null) {
                    myRecipes.setServings(dataFromOtherFragment.getServings());
                    myRecipes.setPrepTimeMinutes(dataFromOtherFragment.getPrepTimeMinutes());
                    myRecipes.setCookTimeMinutes(dataFromOtherFragment.getCookTimeMinutes());
                    myRecipes.setCaloriesPerServing(dataFromOtherFragment.getCaloriesPerServing());
                    myRecipes.setDifficulty(dataFromOtherFragment.getDifficulty());
                    IngredientsInstructionsFragment lastFragment = new IngredientsInstructionsFragment();


                    loadFragment(lastFragment);
                }

            } else if (currentFragment instanceof IngredientsInstructionsFragment) {
                // Collect data from IngredientsInstructionFragment
                IngredientsInstructionsFragment ingredientsInstructionFragment = (IngredientsInstructionsFragment) currentFragment;
                Recipe dataFromIngredientsInstructionFragment = ingredientsInstructionFragment.collectDataFromFragment();
                Log.d("ViewPageActivity", "Data from RecipeFinalFragment: " + dataFromIngredientsInstructionFragment); // Log the bundle data


                if (dataFromIngredientsInstructionFragment != null) {
                    // Pass data to the next fragment
                    myRecipes.setIngredients(dataFromIngredientsInstructionFragment.getIngredients());
                    myRecipes.setInstructions(dataFromIngredientsInstructionFragment.getInstructions());
                    IngredientsInstructionsFragment nextFragment = new IngredientsInstructionsFragment();

                    loadFragment(nextFragment);
                }
                // Insert data into the database (implementation depends on your database setup)
                saveToDatabase();
            }
        });
    }

    public void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
    }

    private void saveToDatabase() {
        // Save all data to your database
        DBHelper dbHelper = new DBHelper(this);


        try {
            boolean isSuccess = dbHelper.insertRecipe(myRecipes);
            Log.d("ViewPageActivity", "Database insert success: " + isSuccess);

            if (isSuccess) {
                navigateToMainScreen();
            } else {
                Toast.makeText(this, "Failed to save recipe", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }


    private void navigateToMainScreen() {
        // You can use an Intent to navigate to the main screen
        Intent intent = new Intent(ViewPageActivity.this, MainActivity.class);
        startActivity(intent);
        finish(); // Finish this activity to prevent going back
    }
}
