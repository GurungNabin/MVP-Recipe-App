package com.example.recipebook;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import androidx.fragment.app.Fragment;
public class RecipeOtherFragment extends Fragment {

    private EditText edtRecipePrepTime, edtRecipeCookTime, edtRecipeServings, edtCaloriesPerServing;
    private RadioButton rbEasy, rbMedium, rbHard;

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

        Button nextButton = view.findViewById(R.id.idBtnNext2);
        nextButton.setOnClickListener(v -> {

            String recipePrepTime = edtRecipePrepTime.getText().toString();
            String recipeCookTime = edtRecipeCookTime.getText().toString();
            String recipeServings = edtRecipeServings.getText().toString();
            String recipeCaloriesPerServing = edtCaloriesPerServing.getText().toString();

            // Check which difficulty is selected
            String difficulty = "";
            if (rbEasy.isChecked()) {
                difficulty = "Easy";
            } else if (rbMedium.isChecked()) {
                difficulty = "Medium";
            } else if (rbHard.isChecked()) {
                difficulty = "Hard";
            }

            // In RecipeOtherFragment (onCreateView or onViewCreated)
            Bundle bundle = getArguments();
            if (bundle != null) {
                String recipeName = bundle.getString("recipeName");
                String recipeCuisine = bundle.getString("recipeCuisine");
                String recipeTags = bundle.getString("recipeTags");
                String recipeType = bundle.getString("recipeTypes");
                String recipeImage = bundle.getString("recipeImage");

                // Log the values to check if the data is correctly passed
                Log.d("DEBUG", "Received recipeName: " + recipeName);
                Log.d("DEBUG", "Received recipeCuisine: " + recipeCuisine);
                Log.d("DEBUG", "Received recipeTags: " + recipeTags);
                Log.d("DEBUG", "Received recipeType: " + recipeType);
                Log.d("DEBUG", "Received recipeImage: " + recipeImage);

                // If all values are correct, you can proceed with the next steps
            }

            // You can also store the selected difficulty in the Bundle

            bundle.putString("recipePrepTime", recipePrepTime);
            bundle.putString("recipeCookTime", recipeCookTime);
            bundle.putString("recipeServings", recipeServings);
            bundle.putString("recipeCaloriesPerServing", recipeCaloriesPerServing);
            bundle.putString("recipeDifficulty", difficulty);

            // Load the next fragment (IngredientsInstructionsFragment)
            IngredientsInstructionsFragment nextFragment = new IngredientsInstructionsFragment();
            nextFragment.setArguments(bundle); // Pass the data to the next fragment if needed
            ((ViewPageActivity) getActivity()).loadFragment(nextFragment);
        });

        return view;
    }
}

