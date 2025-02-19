package com.example.recipebook.food.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.recipebook.R;
import com.example.recipebook.food.contract.RecipeOtherContract;
import com.example.recipebook.food.presenter.RecipeOtherPresenter;
import com.example.recipebook.recipe.model.Recipe;

public class RecipeOtherFragment extends Fragment implements RecipeOtherContract.View {

    private EditText edtRecipePrepTime, edtRecipeCookTime, edtRecipeServings, edtCaloriesPerServing;
    private RadioButton rbEasy, rbMedium, rbHard;
    private RecipeOtherPresenter presenter;

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

        rbEasy = view.findViewById(R.id.radioButtonEasy);
        rbMedium = view.findViewById(R.id.radioButtonMedium);
        rbHard = view.findViewById(R.id.radioButtonHard);

        presenter = new RecipeOtherPresenter(this);

        return view;
    }
    public Recipe collectDataFromFragment() {
        String prepTime = edtRecipePrepTime.getText().toString();
        String cookTime = edtRecipeCookTime.getText().toString();
        String servings = edtRecipeServings.getText().toString();
        String calories = edtCaloriesPerServing.getText().toString();

        // Check if fields are empty
        if (prepTime.isEmpty() || cookTime.isEmpty() || servings.isEmpty() || calories.isEmpty() ||
                (!rbEasy.isChecked() && !rbMedium.isChecked() && !rbHard.isChecked())) {
            showValidationError("All fields are required");
            return null;
        }

        String difficulty = rbEasy.isChecked() ? "Easy" :
                rbMedium.isChecked() ? "Medium" :
                        rbHard.isChecked() ? "Hard" : "";

        return new Recipe(
                Integer.parseInt(prepTime),
                Integer.parseInt(cookTime),
                Integer.parseInt(servings),
                Integer.parseInt(calories),
                difficulty
        );
    }


    @Override
    public void showValidationError(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRecipeSaved(Recipe recipe) {
        Toast.makeText(getActivity(), "Recipe saved successfully!", Toast.LENGTH_SHORT).show();
    }
}
