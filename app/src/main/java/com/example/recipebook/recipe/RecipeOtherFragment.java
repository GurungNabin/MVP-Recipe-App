package com.example.recipebook.recipe;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;

import androidx.fragment.app.Fragment;

import com.example.recipebook.R;
import com.example.recipebook.model.Recipe;

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
        int recipeServings = Integer.parseInt(edtRecipeServings.getText().toString());
        int recipePrepTime = Integer.parseInt(edtRecipePrepTime.getText().toString());
        int recipeCookTime = Integer.parseInt(edtRecipeCookTime.getText().toString());
        int recipeCaloriesPerServing = Integer.parseInt(edtCaloriesPerServing.getText().toString());

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

}
