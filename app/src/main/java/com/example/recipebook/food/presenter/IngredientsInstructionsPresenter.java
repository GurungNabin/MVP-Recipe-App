package com.example.recipebook.food.presenter;

import com.example.recipebook.food.contract.IngredientsInstructionsContract;
import com.example.recipebook.recipe.model.Recipe;

import java.util.ArrayList;
import java.util.Arrays;

public class IngredientsInstructionsPresenter implements IngredientsInstructionsContract.Presenter {

    private IngredientsInstructionsContract.View view;

    public IngredientsInstructionsPresenter(IngredientsInstructionsContract.View view) {
        this.view = view;
    }


    public void showIngredientsInstructions(Recipe recipe) {

    }

    public void showError(String message) {

    }

    @Override
    public void collectDataAndValidate(ArrayList<String> ingredients, ArrayList<String> instruction) {
        ArrayList<String> ingredientsList = new ArrayList<>(Arrays.asList(ingredients.toString()));
        ArrayList<String> instructionsList = new ArrayList<>(Arrays.asList(instruction.toString()));

        if (ingredientsList.isEmpty()) {
            view.showError("Ingredient is required");
            return;
        }
        if (instructionsList.isEmpty()) {
            view.showError("Instructions are required");
            return;
        }

        // Create a Recipe object with the collected data
        Recipe recipe = new Recipe(ingredientsList, instructionsList);
        view.showIngredientsInstructions(recipe);
    }
}
