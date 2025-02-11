package com.example.recipebook;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.recipebook.database.DBHelper;
import com.example.recipebook.model.Recipe;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import android.Manifest;


public class AddRecipeActivity extends AppCompatActivity {

    private EditText edtRecipeName, edtRecipeIngredients, edtRecipeInstructions, edtRecipePrepTime, edtRecipeCookTime, edtRecipeServing, edtRecipeCuisine, edtCaloriesPerServing, edtRecipeTags, edtMealType;
    private RadioButton rbEasy, rbMedium, rbHard;
    private ImageView ivRecipeImage;
    private Button btnAddRecipe;
    private String image;
    private ActivityResultLauncher<Intent> cameraLauncher;
    private ActivityResultLauncher<Intent> galleryLauncher;
    private DBHelper dbHelper;

    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;


    @SuppressLint("MissingInflatedId")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_recipe);

        dbHelper = new DBHelper(this);

        edtRecipeName = findViewById(R.id.idEdtRecipeName);
        edtRecipeIngredients = findViewById(R.id.idEdtRecipeIngredients);
        edtRecipeInstructions = findViewById(R.id.idEdtRecipeInstructions);
        edtRecipePrepTime = findViewById(R.id.idEdtRecipePrepTime);
        edtRecipeCookTime = findViewById(R.id.idEdtRecipeCookTime);
        edtRecipeServing = findViewById(R.id.idEdtRecipeServings);
//        edtRecipeDifficulty = findViewById(R.id.idEdtRecipeDifficulty);
        edtRecipeCuisine = findViewById(R.id.idEdtRecipeCuisine);
        edtCaloriesPerServing = findViewById(R.id.idEdtRecipeCaloriesPerServing);
        edtRecipeTags = findViewById(R.id.idEdtRecipeTags);
        edtMealType = findViewById(R.id.idEdtRecipeMealType);
        ivRecipeImage = findViewById(R.id.idIVRecipeImage);
        btnAddRecipe = findViewById(R.id.idBtnAddRecipe);

        //radiobutton
        rbEasy = findViewById(R.id.radioButtonEasy);
        rbMedium = findViewById(R.id.radioButtonMedium);
        rbHard = findViewById(R.id.radioButtonHard);




        btnAddRecipe.setOnClickListener(v -> {
            String recipeName = edtRecipeName.getText().toString();
            String recipeIngredients = edtRecipeIngredients.getText().toString();
            String recipeInstructions = edtRecipeInstructions.getText().toString();
            String recipePrepTime = edtRecipePrepTime.getText().toString();
            String recipeCookTime = edtRecipeCookTime.getText().toString();
            String recipeServings = edtRecipeServing.getText().toString();
//            String recipeDifficulty = edtRecipeDifficulty.getText().toString();
            String recipeCuisine = edtRecipeCuisine.getText().toString();
            String recipeCaloriesPerServing = edtCaloriesPerServing.getText().toString();
            String recipeTags = edtRecipeTags.getText().toString();
            String recipeMealType = edtMealType.getText().toString();

            boolean isEasySelected = rbEasy.isChecked();
            boolean isMediumSelected = rbMedium.isChecked();
            boolean isHardSelected = rbHard.isChecked();


            // Check if any field is empty
            if (recipeName.isEmpty() || recipeIngredients.isEmpty() || recipeInstructions.isEmpty() ||
                    recipePrepTime.isEmpty() || recipeCookTime.isEmpty() || recipeServings.isEmpty() ||
                   recipeCuisine.isEmpty() || recipeCaloriesPerServing.isEmpty() ||
                    recipeTags.isEmpty() || recipeMealType.isEmpty() || !(isEasySelected || isMediumSelected || isHardSelected)
            ) {
                Toast.makeText(AddRecipeActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            } else {
                saveRecipe();
                Toast.makeText(AddRecipeActivity.this, "Recipe added", Toast.LENGTH_SHORT).show();
            }
        });

        // Initialize ActivityResultLaunchers for Camera and Gallery
        cameraLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                Bitmap photo = (Bitmap) result.getData().getExtras().get("data");
                ivRecipeImage.setImageBitmap(photo);
                image = convertBitmapToBase64(photo);
            }
        });

        galleryLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                Uri selectedImageUri = result.getData().getData();
                try {
                    Bitmap selectedImageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                    ivRecipeImage.setImageBitmap(selectedImageBitmap);
                    image = convertBitmapToBase64(selectedImageBitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        ivRecipeImage.setOnClickListener(v -> {
            // Check for camera permission before opening the camera
            if (ContextCompat.checkSelfPermission(AddRecipeActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                // Request permission if not granted
                ActivityCompat.requestPermissions(AddRecipeActivity.this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
            } else {
                // Permission granted, proceed with camera
                openImageSelector();
            }});

    }

    private void openImageSelector() {
        CharSequence[] options = new CharSequence[]{"Take Photo", "Choose from Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Image Source");
        builder.setItems(options, (dialog, which) -> {
            if (which == 0) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraLauncher.launch(cameraIntent);
            } else {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                galleryLauncher.launch(intent);
            }
        });
        builder.show();
    }

    public String convertBitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String saveBase64ImageToFile(String base64Image) {
        if (base64Image == null || base64Image.isEmpty()) {
            return null;
        }

        try {
            byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
            String fileName = "image_" + System.currentTimeMillis() + ".jpg";
            FileOutputStream fos = openFileOutput(fileName, MODE_PRIVATE);
            fos.write(decodedString);
            fos.close();
            return getFilesDir().getPath() + "/" + fileName;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, you can now access the camera
                Toast.makeText(this, "Camera permission granted", Toast.LENGTH_SHORT).show();
            } else {
                // Permission denied
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void saveRecipe() {
        String name = edtRecipeName.getText().toString().trim();
        String ingredients = edtRecipeIngredients.getText().toString().trim();
        String instructions = edtRecipeInstructions.getText().toString().trim();
        int prepTime = Integer.parseInt(edtRecipePrepTime.getText().toString().trim());
        int cookTime = Integer.parseInt(edtRecipeCookTime.getText().toString().trim());
        int servings = Integer.parseInt(edtRecipeServing.getText().toString().trim());
//        String difficulty = edtRecipeDifficulty.getText().toString();
        String difficulty = "";
        if (rbEasy.isChecked()) {
            difficulty = "Easy";
        } else if (rbMedium.isChecked()) {
            difficulty = "Medium";
        } else if (rbHard.isChecked()) {
            difficulty = "Hard";
        }
        String cuisine = edtRecipeCuisine.getText().toString().trim();
        int calories = Integer.parseInt(edtCaloriesPerServing.getText().toString().trim());
        String tags = edtRecipeTags.getText().toString().trim();
        String mealType = edtMealType.getText().toString();

        // Save the Base64 image as a file and get the file path
        String imagePath = saveBase64ImageToFile(image);

        Recipe recipe = new Recipe(
                name,
                new ArrayList<>(Arrays.asList(ingredients.split(","))),
                new ArrayList<>(Arrays.asList(instructions.split(","))),
                prepTime,
                cookTime,
                servings,
                difficulty,
                cuisine,
                calories,
                new ArrayList<>(Arrays.asList(tags.split(","))),
                imagePath,  // Store the image file path instead of Base64 string
                new ArrayList<>(Arrays.asList(mealType.split(",")))
        );

        boolean isInserted = dbHelper.insertRecipe(recipe);
        Log.d("DB_INSERT", "Insert status: " + isInserted);

        if (isInserted) {
            Toast.makeText(this, "Recipe added successfully!", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
            finish();
        } else {
            Toast.makeText(this, "Failed to add recipe!", Toast.LENGTH_SHORT).show();
        }
    }
}
