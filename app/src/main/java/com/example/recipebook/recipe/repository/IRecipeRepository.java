package com.example.recipebook.recipe.repository;

import com.example.recipebook.recipe.model.ApiRecipe;
import com.example.recipebook.recipe.model.Recipe;

import java.util.List;

public interface IRecipeRepository {
    List<Recipe> getNextPage();

}
