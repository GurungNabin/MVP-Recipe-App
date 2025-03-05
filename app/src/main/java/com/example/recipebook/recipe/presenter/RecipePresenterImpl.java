package com.example.recipebook.recipe.presenter;

import android.util.Log;

import com.example.recipebook.recipe.model.ApiRecipe;
import com.example.recipebook.recipe.view.RecipeView;
import com.example.recipebook.recipe.model.Recipe;
import com.example.recipebook.recipe.repository.IRecipeRepository;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.ListItem;
import com.itextpdf.layout.element.Table;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
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

        Log.d("LOAD_RECIPES", "Loaded recipes from repository: " + recipes.size());

        if(recipes != null && !recipes.isEmpty()){

            view.showDatabaseRecipes(recipes);
            Log.d("LOAD RECIPES","Loaded recipes from database:"+ recipes.size());
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


    @Override
    public void showSearchRecipe(String query, List<Recipe> allRecipes) {
        Log.d("SEARCH", "Query: " + query);  // Log query

        List<Recipe> filteredRecipes = new ArrayList<>();

        if (query.isEmpty()) {
            loadRecipes();  // Fetch data from the DB again
            Log.d("SEARCH", "Search query is empty, loading recipes.");

        } else {

            for (Recipe recipe : allRecipes) {
                if (recipe.getName().toLowerCase().contains(query.toLowerCase())) {
                    filteredRecipes.add(recipe);
                    loadRecipes();
                }
            }
        }

        if (query.isEmpty()) {
            view.showFilteredRecipes(allRecipes);
        } else {
            view.showFilteredRecipes(filteredRecipes);
        }
    }






}
