package com.example.recipebook.recipe.presenter;

import android.util.Log;

import androidx.fragment.app.Fragment;

import com.example.recipebook.recipe.contract.RecipePageContract;
import com.example.recipebook.recipe.view.IngredientsInstructionsFragment;
import com.example.recipebook.recipe.view.RecipeNameFragment;
import com.example.recipebook.recipe.view.RecipeOtherFragment;
import com.example.recipebook.recipe.database.DBHelper;
import com.example.recipebook.recipe.model.Recipe;

import java.util.ArrayList;
import java.util.List;

public class RecipePagePresenter implements RecipePageContract.Presenter {

    private RecipePageContract.View view;
    private Recipe recipe;
    private DBHelper dbHelper;
    private List<Recipe> allRecipes;

    public RecipePagePresenter(RecipePageContract.View view,DBHelper dbHelper) {
        this.view = view;
        this.recipe = new Recipe();
        this.dbHelper = dbHelper;
        loadAllRecipes();
    }


    @Override
    public void handleNext(Fragment currentFragment) {
        if (currentFragment instanceof RecipeNameFragment) {
            RecipeNameFragment fragment = (RecipeNameFragment) currentFragment;
            Recipe data = fragment.collectDataFromNameFragment();
            if (data != null) {
                recipe.setName(data.getName());
                recipe.setCuisine(data.getCuisine());
                recipe.setTags(data.getTags());
                recipe.setMealType(data.getMealType());
                recipe.setImage(data.getImage());
                view.loadFragment(new RecipeOtherFragment());
            }
        } else if (currentFragment instanceof RecipeOtherFragment) {
            RecipeOtherFragment fragment = (RecipeOtherFragment) currentFragment;
            Recipe data = fragment.collectDataFromFragment();
            if (data != null) {
                recipe.setServings(data.getServings());
                recipe.setPrepTimeMinutes(data.getPrepTimeMinutes());
                recipe.setCookTimeMinutes(data.getCookTimeMinutes());
                recipe.setCaloriesPerServing(data.getCaloriesPerServing());
                recipe.setDifficulty(data.getDifficulty());
                view.loadFragment(new IngredientsInstructionsFragment());
            }
        } else if (currentFragment instanceof IngredientsInstructionsFragment) {
            IngredientsInstructionsFragment fragment = (IngredientsInstructionsFragment) currentFragment;
            Recipe data = fragment.onSaveButtonClick();
            if (data != null) {
                recipe.setIngredients(data.getIngredients());
                recipe.setInstructions(data.getInstructions());
                saveToDatabase();
            }
        }
    }

    @Override
    public void saveToDatabase() {
        try {
            boolean isSuccess = dbHelper.insertRecipe(recipe);
            Log.d("RecipePagePresenter", "Database insert success: " + isSuccess);
            if (isSuccess) {
                view.navigateToMainScreen();
            } else {
                view.showToast("Failed to save recipe");
            }
        } catch (Exception e) {
            e.printStackTrace();
            view.showToast("Error: " + e.getMessage());
        }
    }



    private void loadAllRecipes() {
        try {
            allRecipes = dbHelper.getAllRecipe(0,0); // Assume getAllRecipes() returns a List<Recipe>
        } catch (Exception e) {
            e.printStackTrace();
            view.showToast("Error loading recipes: " + e.getMessage());
        }
    }


    private List<Recipe> filterRecipes(String query){
        List<Recipe> filteredRecipes = new ArrayList<>();
        for(Recipe recipe: allRecipes){
            if(recipe.getName().toLowerCase().contains(query.toLowerCase())){
                filteredRecipes.add(recipe);
            }
        }
        return  filteredRecipes;
    }
}
