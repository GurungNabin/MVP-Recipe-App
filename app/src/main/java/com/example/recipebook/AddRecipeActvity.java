package com.example.recipebook;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;

public class AddRecipeActvity extends AppCompatActivity {
    private EditText edtRecipeName, edtRecipeIngredients, edtRecipeInstructions, edtRecipePrepTime, edtRecipeCookTime, edtRecipeServing, edtRecipeDifficulty, edtRecipeCuisine, edtCaloriesPerServing, edtRecipeTags, edtMealType;

    // image
     ImageView ivRecipeImage;

    private ActivityResultLauncher<Intent> cameraLauncher;
    private ActivityResultLauncher<Intent> galleryLauncher;


    private static final int SELECT_PICTURE = 1;
    private static final int REQUEST_CAMERA = 2;

    private Button btnAddRecipe;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_recipe);

        edtRecipeName = findViewById(R.id.idEdtRecipeName);
        edtRecipeIngredients = findViewById(R.id.idEdtRecipeIngredients);
        edtRecipeInstructions = findViewById(R.id.idEdtRecipeInstructions);
        edtRecipePrepTime = findViewById(R.id.idEdtRecipePrepTime);
        edtRecipeCookTime = findViewById(R.id.idEdtRecipeCookTime);
        edtRecipeServing = findViewById(R.id.idEdtRecipeServings);
        edtRecipeDifficulty = findViewById(R.id.idEdtRecipeDifficulty);
        edtRecipeCuisine = findViewById(R.id.idEdtRecipeCuisine);
        edtCaloriesPerServing = findViewById(R.id.idEdtRecipeCaloriesPerServing);
        edtRecipeTags = findViewById(R.id.idEdtRecipeTags);
        edtMealType = findViewById(R.id.idEdtRecipeMealType);
        // image recipe
        ivRecipeImage = findViewById(R.id.idIVRecipeImage);

        btnAddRecipe = findViewById(R.id.idBtnAddRecipe);

        btnAddRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String recipeName = edtRecipeName.getText().toString();
                String recipeIngredients = edtRecipeName.getText().toString();
                String recipeInstructions = edtRecipeName.getText().toString();
                String recipePrepTime = edtRecipeName.getText().toString();
                String recipeCookTime = edtRecipeName.getText().toString();
                String recipeServings = edtRecipeName.getText().toString();
                String recipeDifficulty = edtRecipeName.getText().toString();
                String recipeCuisine = edtRecipeName.getText().toString();
                String recipeCaloriesPerServing = edtRecipeName.getText().toString();
                String recipeTags = edtRecipeName.getText().toString();
                String recipeMealType = edtRecipeName.getText().toString();


                // Check if any field is empty
                if (recipeName.isEmpty() || recipeIngredients.isEmpty() || recipeInstructions.isEmpty() ||
                        recipePrepTime.isEmpty() || recipeCookTime.isEmpty() || recipeServings.isEmpty() ||
                        recipeDifficulty.isEmpty() || recipeCuisine.isEmpty() || recipeCaloriesPerServing.isEmpty() ||
                        recipeTags.isEmpty() || recipeMealType.isEmpty()) {
                    Toast.makeText(AddRecipeActvity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                } else {
                    // Proceed to add the recipe
                    // Add your recipe saving code here
                    Toast.makeText(AddRecipeActvity.this, "Recipe added", Toast.LENGTH_SHORT).show();

                }

            }


        });


        // Initialize ActivityResultLaunchers
        cameraLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                // For camera image, get Bitmap from the intent extras
                Bitmap photo = (Bitmap) result.getData().getExtras().get("data");
                ivRecipeImage.setImageBitmap(photo);
            } else {
                Toast.makeText(AddRecipeActvity.this, "Failed to get image", Toast.LENGTH_SHORT).show();
            }
        });

        galleryLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                Uri selectedImageUri = result.getData().getData();
                try {
                    // Convert URI to Bitmap and display in ImageView
                    Bitmap selectedImageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                    ivRecipeImage.setImageBitmap(selectedImageBitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(AddRecipeActvity.this, "Failed to get image", Toast.LENGTH_SHORT).show();
            }
        });



        ivRecipeImage.setOnClickListener(v -> {
            openImageSelector();
        });


    }

    // Method to open image selector (gallery or camera)
    private void openImageSelector() {
        CharSequence options[] = new CharSequence[]{"Take Photo", "Choose from Gallery"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Image Source");
        builder.setItems(options, (dialog, which) -> {
            if (which == 0) {
                // Camera option
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraLauncher.launch(cameraIntent);
            } else {
                // Gallery option
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                galleryLauncher.launch(intent);
            }
        });
        builder.show();
    }





    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check if the image selection was successful
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE && data != null) {
                Uri selectedImageUri = data.getData(); // Get the URI of the selected image

                try {
                    // Convert URI to Bitmap and display in ImageView
                    Bitmap selectedImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                    ivRecipeImage.setImageBitmap(selectedImageBitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == REQUEST_CAMERA && data != null) {
                // For camera image, get Bitmap from the intent extras
                Bitmap photo = (Bitmap) data.getExtras().get("data");

                // Set the captured image to ImageView
                ivRecipeImage.setImageBitmap(photo);
            }
        } else {
            Toast.makeText(this, "Failed to get image", Toast.LENGTH_SHORT).show();
        }
    }
}