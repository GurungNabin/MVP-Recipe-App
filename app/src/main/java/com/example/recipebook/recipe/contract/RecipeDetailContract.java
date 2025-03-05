package com.example.recipebook.recipe.contract;

import android.net.Uri;

import com.example.recipebook.recipe.model.Recipe;

import java.io.File;

public interface RecipeDetailContract {
    interface View{
        void showRecipeDetails(Recipe recipe);
        // pdf
        void onDownloadSuccess(Uri pdfFile);
//        void onDownloadSuccess(File pdfFile);
        void onDownloadFailure(String error);
        // progress bar
        void showProgressBar(boolean show);
        void updateProgressBar(int progress);

    }

    interface Presenter{
        void loadRecipeDetails(Recipe recipe);
        void downloadRecipeAsPdf(Recipe recipe);
    }
}
