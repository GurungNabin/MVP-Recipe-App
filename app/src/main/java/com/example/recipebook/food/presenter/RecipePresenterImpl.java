package com.example.recipebook.food.presenter;

import com.example.recipebook.food.view.RecipeView;
import com.example.recipebook.recipe.model.Recipe;
import com.example.recipebook.recipe.repository.IRecipeRepository;

import java.util.List;

public class RecipePresenterImpl implements RecipePresenter {

    private RecipeView view;
    private IRecipeRepository repository;
    private boolean isLoading = false;

    public RecipePresenterImpl(RecipeView view, IRecipeRepository repository){
        this.view = view;
        this.repository = repository;
    }

    @Override
    public void loadRecipes() {
        if(isLoading) return;
        view.showLoading();
        isLoading = true;
        List<Recipe> recipes = repository.getNextPage();
        if(recipes != null && !recipes.isEmpty()){
            view.showDatabaseRecipes(recipes);
        }else{
            view.showError("No recipes available");
        }
        view.hideLoading();
        isLoading = false;


    }

    @Override
    public void onRecipeSelected(Recipe recipe) {
        view.navigateToRecipeDetails(recipe);

    }
}
