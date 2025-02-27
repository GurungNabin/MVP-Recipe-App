package com.example.recipebook.recipe.presenter;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import com.example.recipebook.recipe.model.Recipe;
import com.example.recipebook.recipe.view.RecipeDetailsView;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;

public class RecipeDetailsPresenterTest {

    @Mock
    private RecipeDetailsView mockView;
    private RecipeDetailsPresenter presenter;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        presenter = new RecipeDetailsPresenter(mockView);
    }

    @Test
    public void testLoadRecipeDetails_WhenRecipeIsNotNull_ShouldShowRecipeDetails() {

        Recipe recipe = new Recipe();
        recipe.setName("Pasta");
        recipe.setIngredients(new ArrayList<>(Arrays.asList("Noodles", "Sauce")));
        recipe.setInstructions(new ArrayList<>(Arrays.asList("Boil noodles", "Add sauce")));

        presenter.loadRecipeDetails(recipe);

        verify(mockView).showRecipeDetails(recipe);
    }

    @Test
    public void testLoadRecipeDetails_WhenRecipeIsNull_ShouldNotShowRecipeDetails() {

        presenter.loadRecipeDetails(null);


        verify(mockView, never()).showRecipeDetails(null);
    }
}
