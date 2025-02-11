package com.example.recipebook;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import androidx.fragment.app.Fragment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class RecipeNameFragment extends Fragment {

    private EditText edtRecipeName, edtRecipeCuisine, edtRecipeTags, edtRecipeTypes;
    private ImageView ivRecipeImage;
    private String image;

    private ActivityResultLauncher<Intent> cameraLauncher;
    private ActivityResultLauncher<Intent> galleryLauncher;

    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;


    public RecipeNameFragment() {
        // Required empty public constructor
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_recipe_name, container, false);

        edtRecipeName = view.findViewById(R.id.idEdtRecipeName);
        edtRecipeCuisine = view.findViewById(R.id.idEdtRecipeCuisine);
        edtRecipeTags = view.findViewById(R.id.idEdtRecipeTags);
        edtRecipeTypes = view.findViewById(R.id.idEdtRecipeMealType);
        ivRecipeImage = view.findViewById(R.id.idIVRecipeImage);

        Button nextButton = view.findViewById(R.id.idBtnNext);
        nextButton.setOnClickListener(v -> {
            // Get the data from the EditText fields
            String recipeName = edtRecipeName.getText().toString();
            String recipeCuisine = edtRecipeCuisine.getText().toString();
            String recipeTags = edtRecipeTags.getText().toString();
            String recipeType = edtRecipeTypes.getText().toString();


            // Log the data to ensure they are correctly retrieved
            Log.d("DEBUG", "recipeName: " + recipeName);
            Log.d("DEBUG", "recipeCuisine: " + recipeCuisine);
            Log.d("DEBUG", "recipeTags: " + recipeTags);
            Log.d("DEBUG", "recipeType: " + recipeType);
            Log.d("DEBUG", "recipeImage: " + ivRecipeImage);


            // Create a Bundle to pass the data
            Bundle bundle = new Bundle();
            bundle.putString("recipeName", recipeName);
            bundle.putString("recipeCuisine", recipeCuisine);
            bundle.putString("recipeTags", recipeTags);
            bundle.putString("recipeTypes", recipeType);
            bundle.putString("recipeImage", image);

            // Pass data to the ViewPageActivity
//            ((ViewPageActivity) getActivity()).receiveDataFromFragment(bundle);
            Log.d("DEBUG", "Bundle: " + bundle.toString());



            // Navigate to the next fragment (e.g., RecipeOtherFragment)
            RecipeOtherFragment nextFragment = new RecipeOtherFragment();
            nextFragment.setArguments(bundle); // Pass the data to the next fragment if needed
            ((ViewPageActivity) getActivity()).loadFragment(nextFragment);
        });

        // Initialize ActivityResultLaunchers for Camera and Gallery
        cameraLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                Bitmap photo = (Bitmap) result.getData().getExtras().get("data");
                ivRecipeImage.setImageBitmap(photo);
                saveImageToExternalStorage(photo);
            }
        });

        galleryLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                Uri selectedImageUri = result.getData().getData();
                try {
                    Bitmap selectedImageBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImageUri);
                    ivRecipeImage.setImageBitmap(selectedImageBitmap);
                   saveImageToExternalStorage(selectedImageBitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        ivRecipeImage.setOnClickListener(v -> {
            // Check for camera permission before opening the camera
            if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                // Request permission if not granted
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
            } else {
                // Permission granted, proceed with camera
                openImageSelector();
            }});




        return view;
    }


    private void openImageSelector() {
        CharSequence[] options = new CharSequence[]{"Take Photo", "Choose from Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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


    private void saveImageToExternalStorage(Bitmap bitmap) {
        // Define the file path for saving the image
        File directory = new File(getContext().getExternalFilesDir(null), "recipe_images");
        if (!directory.exists()) {
            directory.mkdir(); // Create directory if it doesn't exist
        }

        // Create a new file with a unique name
        String fileName = "recipe_image_" + System.currentTimeMillis() + ".png";
        File file = new File(directory, fileName);

        try (FileOutputStream fos = new FileOutputStream(file)) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();

            // Save the file path (or URI) to the variable
            image = file.getAbsolutePath();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
