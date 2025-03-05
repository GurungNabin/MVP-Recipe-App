
package com.example.recipebook.recipe;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.viewpager.widget.ViewPager;

import com.example.recipebook.R;
import com.example.recipebook.recipe.contract.RecipeDetailContract;
import com.example.recipebook.recipe.presenter.RecipeDetailsPresenter;
import com.example.recipebook.recipe.adapter.ImageAdapter;
import com.example.recipebook.recipe.model.Recipe;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.util.ArrayList;

public class RecipeDetails extends AppCompatActivity implements RecipeDetailContract.View {
    private RecipeDetailsPresenter presenter;
    private ImageAdapter imageAdapter;
    private ViewPager viewPager;

    TextView recipeName, prepCookTime, cookingTime, servingPeople, difficultyCuisineMeal, cuisineType, caloriesRating;
    LinearLayout ingredientsList, instructionList, tagsList, mealType;
    ImageView recipeImage;

    private FloatingActionButton downloadButton;
    private boolean isDownloaded = false;
    private File pdfFile;


    private static final int PERMISSION_REQUEST_CODE = 100;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_detail_screen);

        initializeView();

        presenter = new RecipeDetailsPresenter(this);

        Recipe recipe = getIntent().getParcelableExtra("recipe");

        if (recipe != null) {
            presenter.loadRecipeDetails(recipe);
        } else {
            Toast.makeText(this, "No recipe available", Toast.LENGTH_SHORT).show();
        }

        downloadButton = findViewById(R.id.idFabDownload);

        downloadButton.setOnClickListener(v -> checkPermissionsAndDownload(recipe));
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

    public void showRecipeDetails(Recipe recipe) {
        recipeName.setText(recipe.getName());
        difficultyCuisineMeal.setText(recipe.getDifficulty());
        cuisineType.setText(recipe.getCuisine());
        prepCookTime.setText("Time to prepare " + recipe.getPrepTimeMinutes() + " min");
        cookingTime.setText("Time to cook " + recipe.getCookTimeMinutes() + " min");
        servingPeople.setText(recipe.getServings() + " people");
        caloriesRating.setText(recipe.getCaloriesPerServing() + " kcal");

        addTextViews(ingredientsList, recipe.getIngredients());
        addTextViews(instructionList, recipe.getInstructions());
        addTextViews(tagsList, recipe.getTags());
        addTextViews(mealType, recipe.getMealType());

        if (recipe.getImage() != null) {
            setImageAdapter(recipe.getImage());
        }
    }

    @Override
    public void onDownloadSuccess(Uri pdfUri) {
        Toast.makeText(this, "Download Complete!", Toast.LENGTH_SHORT).show();
        isDownloaded = true;
        pdfFile = new File(pdfUri.getPath());
        downloadButton.setImageResource(R.drawable.share_foreground);
        showShareOption(pdfFile);
    }

    @Override
    public void onDownloadFailure(String error) {
        Toast.makeText(this, "Download Failed: " + error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgressBar(boolean show) {

    }

    @Override
    public void updateProgressBar(int progress) {

    }

    private void checkPermissionsAndDownload(Recipe recipe) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // On Android 10 and above, permissions are not needed for writing to the Downloads folder.
            presenter.downloadRecipeAsPdf(recipe);
        } else {
            // On lower Android versions, request WRITE_EXTERNAL_STORAGE permission
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, initiate the download
                presenter.downloadRecipeAsPdf(recipe);
            } else {
                // Request permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        PERMISSION_REQUEST_CODE);
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Recipe recipe = getIntent().getParcelableExtra("recipe");
                if (recipe != null) {
                    presenter.downloadRecipeAsPdf(recipe);
                }
            } else {
                Toast.makeText(this, "Permission denied, cannot download PDF" , Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showShareOption(File file) {
        if (file != null && file.exists()) {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("application/pdf");
            Uri uri = FileProvider.getUriForFile(this, getPackageName() + ".provider", file);

            shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            startActivity(Intent.createChooser(shareIntent, "Share PDF"));
        } else {
            Toast.makeText(this, "PDF file is not available", Toast.LENGTH_SHORT).show();
        }
    }

    private void setImageAdapter(ArrayList<String> recipeImage) {
        imageAdapter = new ImageAdapter(this, recipeImage);
        viewPager.setAdapter(imageAdapter);
    }

    private void addTextViews(LinearLayout layout, ArrayList<String> items) {
        if (items != null) {
            layout.removeAllViews();
            for (String item : items) {
                TextView textView = new TextView(this);
                textView.setText(item);
                textView.setPadding(8, 8, 8, 8);
                layout.addView(textView);
            }
        }
    }
}
