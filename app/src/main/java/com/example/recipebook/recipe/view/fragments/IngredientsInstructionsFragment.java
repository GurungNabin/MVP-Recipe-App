package com.example.recipebook.recipe.view.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.recipebook.R;
import com.example.recipebook.recipe.model.Recipe;

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

        public Recipe collectDataFromFragment() {
        String recipeIngredients = edtRecipeIngredients.getText().toString();
        String recipeInstructions = edtRecipeInstruction.getText().toString();

        Log.d("RecipeLastFragment", "Recipe Ingredients: " + recipeIngredients);
        Log.d("RecipeLastFragment", "Recipe Instructions: " + recipeInstructions);

        ArrayList<String> ingredientsList = new ArrayList<>(Arrays.asList(recipeIngredients.split(",")));
        if (ingredientsList.isEmpty()) {
            Toast.makeText(getActivity(), "Ingredient is required", Toast.LENGTH_SHORT).show();
            return null;
        }
        ArrayList<String> instructionsList = new ArrayList<>(Arrays.asList(recipeInstructions.split("\n")));


        if(ingredientsList.isEmpty() || instructionsList.isEmpty()){
            Toast.makeText(getActivity(), "Enter the value", Toast.LENGTH_SHORT).show();
            return null;
        }


        return new Recipe(
                ingredientsList,
                instructionsList
        );
    }


}
