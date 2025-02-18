package com.example.recipebook.recipe.view.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.recipebook.R;
import com.example.recipebook.recipe.model.Recipe;

public class RecipeOtherFragment extends Fragment {

     EditText edtRecipePrepTime, edtRecipeCookTime, edtRecipeServings, edtCaloriesPerServing;
     RadioButton rbEasy, rbMedium, rbHard;

    public RecipeOtherFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_other, container, false);

        edtRecipePrepTime = view.findViewById(R.id.idEdtRecipePrepTime);
        edtRecipeCookTime = view.findViewById(R.id.idEdtRecipeCookTime);
        edtRecipeServings = view.findViewById(R.id.idEdtRecipeServings);
        edtCaloriesPerServing = view.findViewById(R.id.idEdtRecipeCaloriesPerServing);

        // Bind the RadioButtons
        rbEasy = view.findViewById(R.id.radioButtonEasy);
        rbMedium = view.findViewById(R.id.radioButtonMedium);
        rbHard = view.findViewById(R.id.radioButtonHard);

        // No button for navigation in this fragment
        return view;
    }

    public Recipe collectDataFromFragment() {
//        int recipeServings = Integer.parseInt(edtRecipeServings.getText().toString());
//        int recipePrepTime = Integer.parseInt(edtRecipePrepTime.getText().toString());
//        int recipeCookTime = Integer.parseInt(edtRecipeCookTime.getText().toString());
//        int recipeCaloriesPerServing = Integer.parseInt(edtCaloriesPerServing.getText().toString());


        int recipeServings = validateAndParseInt(edtRecipeServings, "Servings", 0);  // Default value set to 4
        int recipePrepTime = validateAndParseInt(edtRecipePrepTime, "Prep Time",0);  // Default value set to 0
        int recipeCookTime = validateAndParseInt(edtRecipeCookTime, "Cook Time",0);  // Default value set to 0
        int recipeCaloriesPerServing = validateAndParseInt(edtCaloriesPerServing, "Calories Per Serving",0);  // Default value set to 200


        // Check which difficulty is selected
        String difficulty = "";
        if (rbEasy.isChecked()) {
            difficulty = "Easy";
        } else if (rbMedium.isChecked()) {
            difficulty = "Medium";
        } else if (rbHard.isChecked()) {
            difficulty = "Hard";
        }


        return new Recipe(
        recipePrepTime,
                recipeCookTime,
                recipeCaloriesPerServing,
                recipeServings,
                difficulty
        );
    }

    // Helper method to safely parse the EditText value to an integer
    private int validateAndParseInt(EditText editText, String fieldName, int defaultValue) {
        String input = editText.getText().toString().trim();

        // Check if the input is empty
        if (input.isEmpty()) {
            Toast.makeText(getActivity(), fieldName + " is required", Toast.LENGTH_SHORT).show();
            return defaultValue; // Return default value if input is empty
        }

        // Try parsing the input as an integer
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            Log.e("RecipeOtherFragment", "Invalid number format for " + fieldName + ": " + input, e);
            Toast.makeText(getActivity(), "Invalid " + fieldName + " value. Please enter a valid number.", Toast.LENGTH_SHORT).show();
            return defaultValue; // Return default value if parsing fails
        }
    }

}
