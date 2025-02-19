package com.example.recipebook.food.contract;

import com.example.recipebook.recipe.model.Recipe;

public interface RecipeOtherContract {

    interface View {
        void showValidationError(String message);
        void onRecipeSaved(Recipe recipe);
    }

    interface Presenter {
        void validateAndSaveRecipe(String prepTime, String cookTime, String servings, String calories, String difficulty);
    }
}
