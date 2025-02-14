//package com.example.recipebook;//package com.example.recipebook;
//
//import android.os.Bundle;
//import android.util.Log;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//import com.example.recipebook.recipe.RecipeAdapter;
//import com.example.recipebook.api.RecipeApiService;
//import com.example.recipebook.api.RetrofitClient;
//import com.example.recipebook.model.MyRecipe;
//import com.example.recipebook.model.Recipe;
//import com.google.android.material.bottomnavigation.BottomNavigationView;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//import retrofit2.Retrofit;
//import retrofit2.converter.gson.GsonConverterFactory;
//import java.util.List;
//
//public class MainActivity extends AppCompatActivity {
//
//    private RecyclerView recyclerView;
//    private RecipeAdapter recipeAdapter;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//          recyclerView = findViewById(R.id.recyclerView);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//
//        RecipeApiService recipeApi = RetrofitClient.getApiService();
//        Call<MyRecipe> call = recipeApi.getRecipes();
//
//        call.enqueue(new Callback<MyRecipe>() {
//            @Override
//            public void onResponse(Call<MyRecipe> call, Response<MyRecipe> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    MyRecipe myRecipe = response.body();
//                    List<Recipe> recipes = myRecipe.getRecipes();
//                    recipeAdapter = new RecipeAdapter(recipes, MainActivity.this);
//                    recyclerView.setAdapter(recipeAdapter);
//                } else {
//                    Log.e("API_ERROR", "Error: " + response.message());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<MyRecipe> call, Throwable t) {
//                Log.e("API_FAILURE", "Error: " + t.getMessage());
//            }
//        });
//    }
//
//}


// Using api and SQLiteDatabase

package com.example.recipebook;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipebook.api.RecipeApiService;
import com.example.recipebook.api.RetrofitClient;
import com.example.recipebook.database.DBHelper;
import com.example.recipebook.model.ApiRecipe;
import com.example.recipebook.model.MyApiRecipe;
import com.example.recipebook.model.MyRecipe;
import com.example.recipebook.model.Recipe;
import com.example.recipebook.recipe.RecipeAdapter;
import com.example.recipebook.recipe.ViewPageActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecipeAdapter recipeAdapter;
    private DBHelper dbHelper;

    private List<Recipe> recipes;
    private List<ApiRecipe> apiRecipes;

    private ActivityResultLauncher<Intent> addRecipeLauncher;

    private ArrayList<String> imageUrls = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recipes = new ArrayList<>();
        dbHelper = new DBHelper(this);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(recipeAdapter);
        addRecipeLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        recipes.clear();
                        displayLocalData();
                    }
                }
        );


        FloatingActionButton fabAddRecipe = findViewById(R.id.idFabAddRecipe);
        fabAddRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ViewPageActivity.class);
//                startActivity(intent);
                addRecipeLauncher.launch(intent);


            }
        });
        displayLocalData();
        fetchDataFromAPI();

    }


    private void fetchDataFromAPI() {
        RecipeApiService recipeApi = RetrofitClient.getApiService();
        Call<MyApiRecipe> call = recipeApi.getRecipes();

        call.enqueue(new Callback<MyApiRecipe>() {
            // In onResponse(), ensure you're accessing the correct model

            @Override
            public void onResponse(Call<MyApiRecipe> call, Response<MyApiRecipe> response) {
                if (response.isSuccessful() && response.body() != null) {
                    MyApiRecipe myApiRecipe = response.body();

                    // Fetch the list of ApiRecipe from response
                    apiRecipes = myApiRecipe.getApiRecipes(); // This is a list of ApiRecipe
                    for (ApiRecipe apiRecipe : apiRecipes) {
                        Recipe recipe = getRecipe(apiRecipe);
                        recipes.add(recipe);
                    }
                    recipeAdapter = new RecipeAdapter(recipes, MainActivity.this);
                    recyclerView.setAdapter(recipeAdapter);
                } else {
                    Log.e("API_ERROR", "Error: " + response.message());
                }
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

            @Override
            public void onFailure(Call<MyApiRecipe> call, Throwable t) {
                Log.e("API_FAILURE", "Error: " + t.getMessage());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void displayLocalData() {
        if (dbHelper.getAllRecipe() != null && !dbHelper.getAllRecipe().isEmpty()) {
            recipes.addAll(dbHelper.getAllRecipe());
        }
        recipeAdapter = new RecipeAdapter(recipes, MainActivity.this);
        recyclerView.setAdapter(recipeAdapter);
    }
}
