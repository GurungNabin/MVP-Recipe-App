package com.example.recipebook.food.presenter;

import android.text.TextUtils;
import android.util.Log;

import com.example.recipebook.food.contract.RecipeOtherContract;
import com.example.recipebook.recipe.model.Recipe;

public class RecipeOtherPresenter implements RecipeOtherContract.Presenter {

    private RecipeOtherContract.View view;

    public RecipeOtherPresenter(RecipeOtherContract.View view) {
        this.view = view;
    }

    @Override
    public void validateAndSaveRecipe(String prepTime, String cookTime, String servings, String calories, String difficulty) {
        int recipePrepTime = validateAndParseInt(prepTime, "Prep Time");
        int recipeCookTime = validateAndParseInt(cookTime, "Cook Time");
        int recipeServings = validateAndParseInt(servings, "Servings");
        int recipeCalories = validateAndParseInt(calories, "Calories");

        if (recipePrepTime < 0 || recipeCookTime < 0 || recipeServings <= 0 || recipeCalories < 0) {
            return;
        }

        Recipe recipe = new Recipe(recipePrepTime, recipeCookTime, recipeCalories, recipeServings, difficulty);
        Log.d("RecipePagePresenter", "Recipe saved: " + recipe.toString());

        view.onRecipeSaved(recipe);
    }

    private int validateAndParseInt(String value, String fieldName) {
        if (TextUtils.isEmpty(value)) {
            view.showValidationError(fieldName + " is required");
            return -1;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            view.showValidationError("Invalid " + fieldName + " value");
            return -1;
        }
    }
}
