package com.example.recipebook.recipe.presenter;

import com.example.recipebook.recipe.model.Recipe;

import java.util.List;

public interface RecipePresenter {
    void loadRecipes();
    void onRecipeSelected(Recipe recipe);


    void showSearchRecipe(String newText, List<Recipe> recipeList);

}
