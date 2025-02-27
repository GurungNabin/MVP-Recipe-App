package com.example.recipebook.recipe.presenter;

import com.example.recipebook.recipe.view.RecipeDetailsView;
import com.example.recipebook.recipe.model.Recipe;

public class RecipeDetailsPresenter {
    private RecipeDetailsView view;

    public RecipeDetailsPresenter(RecipeDetailsView view) {
        this.view = view;
    }

    public void loadRecipeDetails(Recipe recipe){
        if(recipe != null){
            view.showRecipeDetails(recipe);
        }
    }
}
