package com.example.recipebook.food.presenter;

import android.widget.Toast;

import com.example.recipebook.food.contract.RecipeNameContract;
import com.example.recipebook.recipe.model.Recipe;

import java.util.ArrayList;
import java.util.List;

public class RecipeNamePresenter implements RecipeNameContract.Presenter {
    private RecipeNameContract.View view;

    public RecipeNamePresenter(RecipeNameContract.View view) {
        this.view = view;
    }

    @Override
    public void validateAndCollectRecipeData(String name, String cuisine, ArrayList<String> tags, ArrayList<String> mealTypes, ArrayList<String> images) {
        if (name.isEmpty()) {
            view.showErrorMessage("Recipe name is required");
            return;
        }
        if (cuisine.isEmpty()) {
            view.showErrorMessage("Cuisine is required");
            return;
        }
        if (tags.isEmpty()){
            view.showErrorMessage("Tags is required");
            return;
        }
        if (mealTypes.isEmpty()) {
            view.showErrorMessage("Please select at least one meal type");
            return;
        }
        if (images.isEmpty()) {
            view.showErrorMessage("At least one image is required");
            return;
        }
        Recipe recipe = new Recipe(name, cuisine, tags, mealTypes, images);
        view.onRecipeSavedSuccessfully();
    }
}
