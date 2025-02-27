package com.example.recipebook.recipe.presenter;

import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import com.example.recipebook.recipe.contract.RecipeNameContract;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class RecipeNamePresenterTest {

    private RecipeNameContract.View mockView;
    private RecipeNamePresenter presenter;

    @Before
    public void setUp() {
        mockView = mock(RecipeNameContract.View.class);
        presenter = new RecipeNamePresenter(mockView);
    }

    @Test
    public void testValidateAndCollectRecipeData_WhenValidInputs_ShouldCallOnRecipeSavedSuccessfully() {

        String name = "Pasta";
        String cuisine = "Italian";
        ArrayList<String> tags = new ArrayList<>();
        tags.add("Vegetarian");
        ArrayList<String> mealTypes = new ArrayList<>();
        mealTypes.add("Lunch");
        ArrayList<String> images = new ArrayList<>();
        images.add("image_url");

        presenter.validateAndCollectRecipeData(name, cuisine, tags, mealTypes, images);

        verify(mockView).onRecipeSavedSuccessfully();
        verify(mockView, never()).showErrorMessage(anyString());
    }

    @Test
    public void testValidateAndCollectRecipeData_WhenNameIsEmpty_ShouldShowErrorMessage() {

        String name = "";
        String cuisine = "Italian";
        ArrayList<String> tags = new ArrayList<>();
        tags.add("Vegetarian");
        ArrayList<String> mealTypes = new ArrayList<>();
        mealTypes.add("Lunch");
        ArrayList<String> images = new ArrayList<>();
        images.add("image_url");

        presenter.validateAndCollectRecipeData(name, cuisine, tags, mealTypes, images);

        verify(mockView).showErrorMessage("Recipe name is required");
    }

    @Test
    public void testValidateAndCollectRecipeData_WhenCuisineIsEmpty_ShouldShowErrorMessage() {

        String name = "Pasta";
        String cuisine = "";
        ArrayList<String> tags = new ArrayList<>();
        tags.add("Vegetarian");
        ArrayList<String> mealTypes = new ArrayList<>();
        mealTypes.add("Lunch");
        ArrayList<String> images = new ArrayList<>();
        images.add("image_url");

        presenter.validateAndCollectRecipeData(name, cuisine, tags, mealTypes, images);

        verify(mockView).showErrorMessage("Cuisine is required");
    }

    @Test
    public void testValidateAndCollectRecipeData_WhenTagsIsEmpty_ShouldShowErrorMessage() {

        String name = "Pasta";
        String cuisine = "Italian";
        ArrayList<String> tags = new ArrayList<>();
        ArrayList<String> mealTypes = new ArrayList<>();
        mealTypes.add("Lunch");
        ArrayList<String> images = new ArrayList<>();
        images.add("image_url");

        presenter.validateAndCollectRecipeData(name, cuisine, tags, mealTypes, images);

        verify(mockView).showErrorMessage("Tags is required");
    }

    @Test
    public void testValidateAndCollectRecipeData_WhenMealTypesIsEmpty_ShouldShowErrorMessage() {// Arrange: prepare data with empty mealTypes
        String name = "Pasta";
        String cuisine = "Italian";
        ArrayList<String> tags = new ArrayList<>();
        tags.add("Vegetarian");
        ArrayList<String> mealTypes = new ArrayList<>();
        ArrayList<String> images = new ArrayList<>();
        images.add("image_url");

        presenter.validateAndCollectRecipeData(name, cuisine, tags, mealTypes, images);

        verify(mockView).showErrorMessage("Please select at least one meal type");
    }

    @Test
    public void testValidateAndCollectRecipeData_WhenImagesIsEmpty_ShouldShowErrorMessage() {

        String name = "Pasta";
        String cuisine = "Italian";
        ArrayList<String> tags = new ArrayList<>();
        tags.add("Vegetarian");
        ArrayList<String> mealTypes = new ArrayList<>();
        mealTypes.add("Lunch");
        ArrayList<String> images = new ArrayList<>();

        presenter.validateAndCollectRecipeData(name, cuisine, tags, mealTypes, images);

        verify(mockView).showErrorMessage("At least one image is required");
    }
}
