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
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipebook.api.RecipeApiService;
import com.example.recipebook.api.RetrofitClient;
import com.example.recipebook.database.DBHelper;
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

public class MainActivity extends AppCompatActivity  {

    private RecyclerView recyclerView;
    private RecipeAdapter recipeAdapter;
    private DBHelper dbHelper;

    private List<Recipe> recipes;

    private ActivityResultLauncher<Intent> addRecipeLauncher;



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
              if(result.getResultCode() == RESULT_OK){
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

        fetchDataFromAPI();

    }









    private void fetchDataFromAPI(){
        RecipeApiService recipeApi = RetrofitClient.getApiService();
        Call<MyRecipe> call = recipeApi.getRecipes();

        call.enqueue(new Callback<MyRecipe>() {
            @Override
            public void onResponse(Call<MyRecipe> call, Response<MyRecipe> response) {
                if (response.isSuccessful() && response.body() != null) {
                    MyRecipe myRecipe = response.body();

                    recipes  = myRecipe.getRecipes();


                    recipeAdapter = new RecipeAdapter(recipes, MainActivity.this);
                    recyclerView.setAdapter(recipeAdapter);



                    Log.d("DB","Data stored locally");
                    displayLocalData();
                } else {
                    Log.e("API_ERROR", "Error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<MyRecipe> call, Throwable t) {
                Log.e("API_FAILURE", "Error: " + t.getMessage());
                displayLocalData();
            }
        });
    }






    @Override
    protected void onResume() {
        super.onResume();
        displayLocalData();
    }

    public void displayLocalData() {
        List<Recipe> localRecipes = dbHelper.getAllRecipe(); // Fetch local data

        List<Recipe> combinedRecipes = new ArrayList<>(recipes); // Start with API data

        // Avoid duplicates by checking existing recipe names
        Set<String> displayedNames = new HashSet<>();
        for (Recipe recipe : recipes) {
            displayedNames.add(recipe.getName());
        }

        for (Recipe recipe : localRecipes) {
            if (!displayedNames.contains(recipe.getName())) {
                combinedRecipes.add(recipe); // Add only if not already in the list
            }
        }

        // Update RecyclerView
        if (recipeAdapter != null) {
            recipeAdapter.setRecipeList(combinedRecipes);
            recipeAdapter.notifyDataSetChanged();
        } else {
            recipeAdapter = new RecipeAdapter(combinedRecipes, MainActivity.this);
            recyclerView.setAdapter(recipeAdapter);
        }
    }











}
