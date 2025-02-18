package com.example.recipebook.food;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipebook.MainActivity;
import com.example.recipebook.R;
import com.example.recipebook.recipe.api.RecipeApiService;
import com.example.recipebook.recipe.api.RetrofitClient;
import com.example.recipebook.recipe.database.DBHelper;
import com.example.recipebook.recipe.model.ApiRecipe;
import com.example.recipebook.recipe.model.MyApiRecipe;
import com.example.recipebook.recipe.model.Recipe;
import com.example.recipebook.recipe.view.RecipeAdapter;
import com.example.recipebook.recipe.view.RecipePageActivity;
import com.example.recipebook.recipe.repository.RecipeRepository;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FoodFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecipeAdapter recipeAdapter;
    private RecipeRepository recipeRepository;
    private List<Recipe> recipes;
    private boolean isLoading = false;
    private DBHelper dbHelper;
    private ActivityResultLauncher<Intent> addRecipeLauncher;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.food_main, container, false);

        dbHelper = new DBHelper(requireContext());
        recipes = new ArrayList<>();
        recipeRepository = new RecipeRepository(requireContext());

        recyclerView = view.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        recipeAdapter = new RecipeAdapter(recipes, dbHelper, (MainActivity) requireActivity());
        recyclerView.setAdapter(recipeAdapter);

        // Floating Action Button to add recipes
        FloatingActionButton fabAddRecipe = view.findViewById(R.id.idFabAddRecipe);
        fabAddRecipe.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), RecipePageActivity.class);
            addRecipeLauncher.launch(intent);
        });

        addRecipeLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == getActivity().RESULT_OK) {
                        recipes.clear();
                        loadNextPage();  // Reload data when returning from another activity
                    }
                }
        );

        // Load recipes
        loadNextPage();
        fetchDataFromAPI();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (!isLoading && layoutManager.findLastCompletelyVisibleItemPosition() == recipes.size() - 1) {
                    loadNextPage();
                }
            }
        });

        return view;
    }

    private void loadNextPage() {
        isLoading = true;
        List<Recipe> newRecipes = recipeRepository.getNextPage();

        if (!newRecipes.isEmpty()) {
            recipes.addAll(newRecipes);
            recipeAdapter.notifyDataSetChanged();
        }

        isLoading = false;
    }

    private void fetchDataFromAPI() {
        RecipeApiService recipeApi = RetrofitClient.getApiService();
        Call<MyApiRecipe> call = recipeApi.getRecipes();

        call.enqueue(new Callback<MyApiRecipe>() {
            @Override
            public void onResponse(Call<MyApiRecipe> call, Response<MyApiRecipe> response) {
                if (response.isSuccessful() && response.body() != null) {
                    MyApiRecipe myApiRecipe = response.body();
                    List<ApiRecipe> apiRecipes = myApiRecipe.getApiRecipes();

                    for (ApiRecipe apiRecipe : apiRecipes) {
                        Recipe recipe = getRecipe(apiRecipe);
                        recipes.add(recipe);
                    }
                    recipeAdapter.notifyDataSetChanged();
                } else {
                    Log.e("API_ERROR", "Error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<MyApiRecipe> call, Throwable t) {
                Log.e("API_FAILURE", "Error: " + t.getMessage());
            }
        });
    }

    private @NonNull Recipe getRecipe(ApiRecipe apiRecipe) {
        Recipe recipe = new Recipe();
        recipe.setName(apiRecipe.getName());
        recipe.setIngredients(apiRecipe.getIngredients());
        recipe.setInstructions(apiRecipe.getInstructions());
        recipe.setCookTimeMinutes(apiRecipe.getCookTimeMinutes());
        recipe.setPrepTimeMinutes(apiRecipe.getPrepTimeMinutes());
        recipe.setServings(apiRecipe.getServings());
        recipe.setDifficulty(apiRecipe.getDifficulty());
        recipe.setCuisine(apiRecipe.getCuisine());
        recipe.setCaloriesPerServing(apiRecipe.getCaloriesPerServing());
        recipe.setTags(apiRecipe.getTags());
        ArrayList<String> images = new ArrayList<>();
        images.add(apiRecipe.getImage());
        recipe.setImage(images);
        recipe.setMealType(apiRecipe.getMealType());
        return recipe;
    }
}
