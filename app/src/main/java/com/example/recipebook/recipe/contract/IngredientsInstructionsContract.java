package com.example.recipebook.recipe.contract;

import com.example.recipebook.recipe.model.Recipe;

import java.util.ArrayList;

public interface IngredientsInstructionsContract {
    interface View{
        void showIngredientsInstructions(Recipe recipe);
        void showError(String message);
    }
    interface Presenter{
        void collectDataAndValidate(ArrayList<String> ingredients, ArrayList<String> instruction);
    }
}
