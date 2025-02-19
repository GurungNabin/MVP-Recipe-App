package com.example.recipebook.recipe.repository;

import android.content.Context;
import android.util.Log;

import com.example.recipebook.recipe.database.DBHelper;
import com.example.recipebook.recipe.model.Recipe;

import java.util.List;

public class RecipeRepository implements IRecipeRepository {
    private DBHelper dbHelper;
    private static final int PAGE_SIZE = 2;
    private int currentPage = 0;

    public RecipeRepository(Context context) {
        this.dbHelper = new DBHelper(context);
    }

    public List<Recipe> getNextPage() {
        int offset = currentPage * PAGE_SIZE;
        List<Recipe> recipes = dbHelper.getAllRecipe(PAGE_SIZE, offset);
        Log.d("DEBUG_APP", "getNextPage: "+ recipes.size());
        if (!recipes.isEmpty()) {
            currentPage++;
        }

        return recipes;
    }
}
