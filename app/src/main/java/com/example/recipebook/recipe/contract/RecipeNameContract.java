package com.example.recipebook.recipe.contract;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

public interface RecipeNameContract {
    interface View{
        void showErrorMessage(String message);
        void showRecipeImage(Bitmap image);
        void updateChipSelection(ArrayList<String> selectedChips);
        void onRecipeSavedSuccessfully();

    }

    interface Presenter{
        void validateAndCollectRecipeData(String name, String cuisine, ArrayList<String> tags, ArrayList<String> mealTypes, ArrayList<String> images);
    }
}
