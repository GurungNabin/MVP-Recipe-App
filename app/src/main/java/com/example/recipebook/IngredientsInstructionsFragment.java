package com.example.recipebook;

import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.recipebook.database.DBHelper;
import com.example.recipebook.model.Recipe;

import java.util.ArrayList;
import java.util.Arrays;

public class IngredientsInstructionsFragment extends Fragment {

    private EditText edtRecipeIngredients, edtRecipeInstruction;
    private DBHelper dbHelper;

    public IngredientsInstructionsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recipe_ingredients_instruction, container, false);

        edtRecipeIngredients = view.findViewById(R.id.idEdtRecipeIngredients);
        edtRecipeInstruction = view.findViewById(R.id.idEdtRecipeInstructions);

        dbHelper = new DBHelper(getActivity());

        Button confirmButton = view.findViewById(R.id.idBtnConfirm);
        confirmButton.setOnClickListener(v -> {
            String recipeIngredients = edtRecipeIngredients.getText().toString();
            String recipeInstructions = edtRecipeInstruction.getText().toString();

            Bundle bundle = getArguments();
            if(bundle != null){
                String recipeImage = bundle.getString("recipeImage");
                String recipeName = bundle.getString("recipeName");
                String recipeCuisine = bundle.getString("recipeCuisine");
                String recipeTags = bundle.getString("recipeTags");
                String recipeType = bundle.getString("recipeTypes");
                String recipeDifficulty = bundle.getString("recipeDifficulty");
                String recipePrepTime = bundle.getString("recipePrepTime");
                String recipeCookTime = bundle.getString("recipeCookTime");
                String recipeServings = bundle.getString("recipeServings");
                String recipeCaloriesPerServing = bundle.getString("recipeCaloriesPerServing");

                // Logging the retrieved values for debugging
                Log.d("DEBUG", "recipeCuisine: " + recipeCuisine);
                Log.d("DEBUG", "recipeTags: " + recipeTags);
                Log.d("DEBUG", "recipeName: " + recipeName);
                Log.d("DEBUG", "recipeType: " + recipeType);
                Log.d("DEBUG", "recipeDifficulty: " + recipeDifficulty);
                Log.d("DEBUG", "recipePrepTime: " + recipePrepTime);
                Log.d("DEBUG", "recipeCookTime: " + recipeCookTime);
                Log.d("DEBUG", "recipeServings: " + recipeServings);
                Log.d("DEBUG", "recipeCaloriesPerServing: " + recipeCaloriesPerServing);
                Log.d("DEBUG", "recipeInstructions: " + recipeInstructions);
                Log.d("DEBUG", "recipeIngredients: " + recipeIngredients);
                Log.d("DEBUG", "recipeImages: " + recipeImage);

                // Validate values before processing
                if (recipeName == null || recipeCuisine == null || recipeTags == null || recipeType == null) {
                    Toast.makeText(getActivity(), "Missing required fields.", Toast.LENGTH_SHORT).show();
                    return;
                }

                Recipe recipe = new Recipe();
                recipe.setImage(recipeImage);
                recipe.setName(recipeName);

                // Validate and handle null or empty values for list-type fields
                recipe.setCuisine(recipeCuisine);
                recipe.setTags(recipeTags.isEmpty() ? new ArrayList<>() : new ArrayList<>(Arrays.asList(recipeTags.split(","))));
                recipe.setMealType(recipeType.isEmpty() ? new ArrayList<>() : new ArrayList<>(Arrays.asList(recipeType.split(","))));

                // Handle ingredients and instructions
                recipe.setIngredients(recipeIngredients.isEmpty() ? new ArrayList<>() : new ArrayList<>(Arrays.asList(recipeIngredients.split(","))));
                recipe.setInstructions(recipeInstructions.isEmpty() ? new ArrayList<>() : new ArrayList<>(Arrays.asList(recipeInstructions.split(","))));

                try {
                    recipe.setPrepTimeMinutes(Integer.parseInt(recipePrepTime));
                    recipe.setCookTimeMinutes(Integer.parseInt(recipeCookTime));
                    recipe.setServings(Integer.parseInt(recipeServings));
                    recipe.setCaloriesPerServing(Integer.parseInt(recipeCaloriesPerServing));
                } catch (NumberFormatException e) {
                    Toast.makeText(getActivity(), "Invalid number format.", Toast.LENGTH_SHORT).show();
                    return;
                }

                recipe.setDifficulty(recipeDifficulty); // Difficulty can be a string, no validation required here

                // Insert the recipe into the database
                boolean success = dbHelper.insertRecipe(recipe);
                if (success) {
                    // Show a success message
                    Toast.makeText(getActivity(), "Recipe saved successfully!", Toast.LENGTH_SHORT).show();
                    if (getActivity() instanceof MainActivity) {
                        ((MainActivity) getActivity()).displayLocalData();
                    }
                    getActivity().finish();
                } else {
                    // Show an error message
                    Toast.makeText(getActivity(), "Failed to save recipe", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity(), "Bundle is null.", Toast.LENGTH_SHORT).show();
            }

        });
        return view;
    }
}
