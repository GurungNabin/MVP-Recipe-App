//package com.example.recipebook;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.LinearLayout;
//import androidx.fragment.app.Fragment;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//import com.example.recipebook.R;
//import com.example.recipebook.recipe.database.DBHelper;
//import com.example.recipebook.recipe.model.Recipe;
//import com.example.recipebook.recipe.view.RecipeAdapter;
//import com.example.recipebook.recipe.view.RecipePageActivity;
//import com.google.android.material.floatingactionbutton.FloatingActionButton;
//
//import java.util.List;
//
//public class RecipeFragment extends Fragment {
//
//    private RecyclerView recyclerView;
//    private RecipeAdapter recipeAdapter;
//    private List<Recipe> recipes;
//    private DBHelper dbHelper;
//
//    public RecipeFragment() {
//        super(R.layout.fragment_recipe);
//    }
//
//    @Override
//    public void onViewCreated(View view, Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        dbHelper = new DBHelper(getContext());
//
//        recyclerView = view.findViewById(R.id.recyclerView);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//
//        recipeAdapter = new RecipeAdapter(recipes, getContext());
//        recyclerView.setAdapter(recipeAdapter);
//
//        FloatingActionButton fabAddRecipe = view.findViewById(R.id.idFabAddRecipe);
//        fabAddRecipe.setOnClickListener(v -> {
//            Intent intent = new Intent(getActivity(), RecipePageActivity.class);
//            startActivity(intent);
//        });
//
//        displayLocalData();
//    }
//
//    public void displayLocalData() {
//        recipes = dbHelper.getAllRecipe(); // Assuming DBHelper is setup to fetch recipes
//        if (recipes != null && !recipes.isEmpty()) {
////            recipeAdapter.up(recipes);
//        }
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        displayLocalData();
//    }
//}
