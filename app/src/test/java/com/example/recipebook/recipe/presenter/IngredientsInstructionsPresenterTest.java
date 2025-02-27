package com.example.recipebook.recipe.presenter;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.example.recipebook.recipe.contract.IngredientsInstructionsContract;
import com.example.recipebook.recipe.model.Recipe;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.ArrayList;

public class IngredientsInstructionsPresenterTest {

    private IngredientsInstructionsContract.View mockView;
    private IngredientsInstructionsPresenter presenter;

    @Before
    public void setUp() {
        mockView = mock(IngredientsInstructionsContract.View.class);
        presenter = new IngredientsInstructionsPresenter(mockView);
    }

    @Test
    public void testValidateWithEmptyIngredients() {
        ArrayList<String> ingredients = new ArrayList<>();
        ArrayList<String> instructions = new ArrayList<>();
        instructions.add("Cook for 5 minutes");

        presenter.collectDataAndValidate(ingredients, instructions);

        verify(mockView).showError("Ingredient is required");
//        verify(mockView, never()).showIngredientsInstructions(any());
    }

    @Test
    public void testValidateWithEmptyInstructions() {
        ArrayList<String> ingredients = new ArrayList<>();
        ingredients.add("Tomato");
        ArrayList<String> instructions = new ArrayList<>();

        presenter.collectDataAndValidate(ingredients, instructions);

        verify(mockView).showError("Instructions are required");
//        verify(mockView, never()).showIngredientsInstructions(any());
    }

    @Test
    public void testValidateWithEmptyIngredientsAndInstructions() {
        ArrayList<String> ingredients = new ArrayList<>();
        ArrayList<String> instructions = new ArrayList<>();

        presenter.collectDataAndValidate(ingredients, instructions);

        verify(mockView).showError("Ingredient is required");
//        verify(mockView, never()).showIngredientsInstructions(any());
    }

    @Test
    public void testValidateWithValidIngredientsAndInstructions() {
        ArrayList<String> ingredients = new ArrayList<>();
        ingredients.add("Tomato");

        ArrayList<String> instructions = new ArrayList<>();
        instructions.add("Cook for 5 minutes");

        presenter.collectDataAndValidate(ingredients, instructions);

        ArgumentCaptor<Recipe> recipeArgumentCaptor = ArgumentCaptor.forClass(Recipe.class);
        verify(mockView).showIngredientsInstructions(recipeArgumentCaptor.capture());

        Recipe capturedRecipe = recipeArgumentCaptor.getValue();
        assertEquals(ingredients, capturedRecipe.getIngredients());
        assertEquals(instructions, capturedRecipe.getInstructions());
    }
}
