package com.example.recipebook.recipe.presenter;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import com.example.recipebook.recipe.contract.RecipeOtherContract;
import com.example.recipebook.recipe.model.Recipe;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class RecipeOtherPresenterTest {

    @Mock
    private RecipeOtherContract.View mockView;

    private RecipeOtherPresenter presenter;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        presenter = new RecipeOtherPresenter(mockView);
    }

    @Test
    public void testValidRecipeInput() {
        String prepTime = "30";
        String cookTime = "45";
        String servings = "4";
        String calories = "500";
        String difficulty = "Medium";

        presenter.validateAndSaveRecipe(prepTime, cookTime, servings, calories, difficulty);

        ArgumentCaptor<Recipe> recipeCaptor = ArgumentCaptor.forClass(Recipe.class);
        verify(mockView).onRecipeSaved(recipeCaptor.capture());

        Recipe capturedRecipe = recipeCaptor.getValue();
        assertEquals(30, capturedRecipe.getPrepTimeMinutes());
        assertEquals(45, capturedRecipe.getCookTimeMinutes());
        assertEquals(500, capturedRecipe.getCaloriesPerServing());
        assertEquals(4, capturedRecipe.getServings());
        assertEquals("Medium", capturedRecipe.getDifficulty());

        verify(mockView, never()).showValidationError(anyString());
    }

    @Test
    public void testNullInput() {
        presenter.validateAndSaveRecipe(null, "45", "4", "500", "Medium");
        verify(mockView).showValidationError("Prep Time is required");
        verify(mockView, never()).onRecipeSaved(any(Recipe.class));
    }

    @Test
    public void testEmptyInput() {
        presenter.validateAndSaveRecipe("30", "45", "", "500", "Medium");
        verify(mockView).showValidationError("Servings is required");
        verify(mockView, never()).onRecipeSaved(any(Recipe.class));
    }

    @Test
    public void testInvalidNumberFormat() {
        presenter.validateAndSaveRecipe("30", "45", "4", "abc", "Medium");
        verify(mockView).showValidationError("Invalid Calories value");
        verify(mockView, never()).onRecipeSaved(any(Recipe.class));
    }

    @Test
    public void testNegativeValues() {
        presenter.validateAndSaveRecipe("-30", "45", "4", "500", "Medium");
        verify(mockView, never()).showValidationError(anyString());
        verify(mockView, never()).onRecipeSaved(any(Recipe.class));
    }

    @Test
    public void testZeroServings() {
        presenter.validateAndSaveRecipe("30", "45", "0", "500", "Medium");
        verify(mockView, never()).showValidationError(anyString());
        verify(mockView, never()).onRecipeSaved(any(Recipe.class));
    }

    @Test
    public void testMultipleInvalidInputs() {
        presenter.validateAndSaveRecipe("", "abc", "-1", "500", "Medium");
        verify(mockView).showValidationError("Prep Time is required");
        verify(mockView, never()).onRecipeSaved(any(Recipe.class));
    }
}