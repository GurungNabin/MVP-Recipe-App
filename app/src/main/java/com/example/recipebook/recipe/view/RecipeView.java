package com.example.recipebook.recipe.view;

import com.example.recipebook.recipe.model.Recipe;

import java.util.List;

public interface RecipeView {
   void showLoading();
   void hideLoading();
   void showDatabaseRecipes(List<Recipe> recipes);
   void showError(String message);
   void navigateToRecipeDetails(Recipe recipe);


}

