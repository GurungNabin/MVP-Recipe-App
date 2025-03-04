package com.example.recipebook.recipe.contract;

import com.example.recipebook.recipe.model.Recipe;

import java.io.File;

public interface RecipeDetailContract {
    interface View{
        void showRecipeDetails(Recipe recipe);
        // pdf
        void onDownloadSuccess(File pdfFile);
        void onDownloadFailure(String error);

    }

    interface Presenter{
        void loadRecipeDetails(Recipe recipe);
        void downloadRecipeAsPdf(Recipe recipe);
    }
}
