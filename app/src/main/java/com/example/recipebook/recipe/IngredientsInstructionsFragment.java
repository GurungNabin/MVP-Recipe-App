package com.example.recipebook.recipe;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.example.recipebook.R;
import com.example.recipebook.model.Recipe;

import java.util.ArrayList;
import java.util.Arrays;

public class IngredientsInstructionsFragment extends Fragment {

     EditText edtRecipeIngredients, edtRecipeInstruction;

    public IngredientsInstructionsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recipe_ingredients_instruction, container, false);

        edtRecipeIngredients = view.findViewById(R.id.idEdtRecipeIngredients);
        edtRecipeInstruction = view.findViewById(R.id.idEdtRecipeInstructions);

        return view;
    }

    // Helper method to collect data from the fragment
    // Helper method to collect data from the fragment
    public Recipe collectDataFromFragment() {
        String recipeIngredients = edtRecipeIngredients.getText().toString();
        String recipeInstructions = edtRecipeInstruction.getText().toString();

        Log.d("RecipeLastFragment", "Recipe Ingredients: " + recipeIngredients);
        Log.d("RecipeLastFragment", "Recipe Instructions: " + recipeInstructions);

        // Split the string by commas or newlines and store them in an ArrayList
        ArrayList<String> ingredientsList = new ArrayList<>(Arrays.asList(recipeIngredients.split(",")));
        ArrayList<String> instructionsList = new ArrayList<>(Arrays.asList(recipeInstructions.split("\n")));


        return new Recipe(
                ingredientsList,
                instructionsList
        );
    }


}
