package com.example.recipebook.recipe.presenter;

import com.example.recipebook.recipe.model.Recipe;

public interface RecipePresenter {
    void loadRecipes();
    void onRecipeSelected(Recipe recipe);

}
