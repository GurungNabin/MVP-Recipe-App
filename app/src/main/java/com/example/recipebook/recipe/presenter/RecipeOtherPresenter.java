package com.example.recipebook.recipe.presenter;

import com.example.recipebook.recipe.contract.RecipeOtherContract;
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

        view.onRecipeSaved(recipe);
    }

    private int validateAndParseInt(String value, String fieldName) {
        if (value == null || value.isEmpty()) {
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
