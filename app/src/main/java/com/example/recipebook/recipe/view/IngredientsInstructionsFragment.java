package com.example.recipebook.recipe.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.recipebook.R;
import com.example.recipebook.recipe.contract.IngredientsInstructionsContract;
import com.example.recipebook.recipe.presenter.IngredientsInstructionsPresenter;
import com.example.recipebook.recipe.model.Recipe;

import java.util.ArrayList;
import java.util.Arrays;

public class IngredientsInstructionsFragment extends Fragment implements IngredientsInstructionsContract.View {

    private IngredientsInstructionsContract.Presenter presenter;
    private EditText edtRecipeIngredients, edtRecipeInstruction;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recipe_ingredients_instruction, container, false);

        presenter = new IngredientsInstructionsPresenter(this);

        edtRecipeIngredients = view.findViewById(R.id.idEdtRecipeIngredients);
        edtRecipeInstruction = view.findViewById(R.id.idEdtRecipeInstructions);



        return view;
    }

    public Recipe onSaveButtonClick() {
        String recipeIngredients = edtRecipeIngredients.getText().toString();
        String recipeInstructions = edtRecipeInstruction.getText().toString();

        // Convert the ingredients and instructions into ArrayList<String>
        ArrayList<String> ingredientsList = new ArrayList<>(Arrays.asList(recipeIngredients.split(",")));
        ArrayList<String> instructionsList = new ArrayList<>(Arrays.asList(recipeInstructions.split(",")));

        // Pass the ArrayList<String> instead of String to the presenter
        presenter.collectDataAndValidate(ingredientsList, instructionsList);

        return new Recipe(ingredientsList, instructionsList);
    }




    @Override
    public void showIngredientsInstructions(Recipe recipe) {
        Toast.makeText(getActivity(), "Recipe saved successfully!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showError(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }
}
