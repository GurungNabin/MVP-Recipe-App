package com.example.recipebook.recipe;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.recipebook.R;
import com.example.recipebook.recipe.presenter.RecipeDetailsPresenter;
import com.example.recipebook.recipe.view.RecipeDetailsView;
import com.example.recipebook.recipe.adapter.ImageAdapter;
import com.example.recipebook.recipe.model.Recipe;

import java.util.ArrayList;

public class RecipeDetails extends AppCompatActivity implements RecipeDetailsView {
    private RecipeDetailsPresenter presenter;
    private ImageAdapter imageAdapter;
    private ViewPager viewPager;

    TextView recipeName, prepCookTime, cookingTime, servingPeople, difficultyCuisineMeal, cuisineType, caloriesRating;
    LinearLayout ingredientsList,instructionList,tagsList, mealType;
    ImageView recipeImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_detail_screen);

        initializeView();

        presenter = new RecipeDetailsPresenter(this);

        Recipe recipe = getIntent().getParcelableExtra("recipe");
        presenter.loadRecipeDetails(recipe);
    }

    private void initializeView() {
        recipeName = findViewById(R.id.recipeName);
        ingredientsList = findViewById(R.id.ingredientsList);
        instructionList = findViewById(R.id.instructionsList);
        prepCookTime = findViewById(R.id.prepCookTime);
        cookingTime = findViewById(R.id.cookingTime);
        servingPeople = findViewById(R.id.servings);
        difficultyCuisineMeal = findViewById(R.id.difficulty);
        cuisineType = findViewById(R.id.cuisine);
        caloriesRating = findViewById(R.id.caloriesRating);
        tagsList = findViewById(R.id.tags);
        recipeImage = findViewById(R.id.recipeImage);
        mealType = findViewById(R.id.types);
        viewPager = findViewById(R.id.viewPagerMain);

    }

    @Override
    public void showRecipeDetails(Recipe recipe) {
        recipeName.setText(recipe.getName());
        difficultyCuisineMeal.setText(recipe.getDifficulty());
        cuisineType.setText(recipe.getCuisine());
        prepCookTime.setText("Time to prepare " + recipe.getPrepTimeMinutes() + " min");
        cookingTime.setText("Time to cook "+ recipe.getCookTimeMinutes() + " min");
        servingPeople.setText(recipe.getServings() + " people");
        caloriesRating.setText(recipe.getCaloriesPerServing() + " kcal");

        addTextViews(ingredientsList, recipe.getIngredients());
        addTextViews(instructionList, recipe.getInstructions());
        addTextViews(tagsList, recipe.getTags());
        addTextViews(mealType, recipe.getMealType());

        if (recipe.getImage() != null){
            setImageAdapter(recipe.getImage());
        }
    }

    private void setImageAdapter(ArrayList<String> recipeImage) {
        imageAdapter = new ImageAdapter(this, recipeImage);
        viewPager.setAdapter(imageAdapter);
    }

    private void addTextViews(LinearLayout layout, ArrayList<String> items){
        if(items != null){
            layout.removeAllViews();
            for (String item : items){
                TextView textView = new TextView(this);
                textView.setText(item);
                textView.setPadding(8,8,8,8);
                layout.addView(textView);

            }
        }
    }
}
