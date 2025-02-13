package com.example.recipebook.recipe;

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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import androidx.fragment.app.Fragment;

import com.example.recipebook.R;
import com.example.recipebook.model.Recipe;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RecipeNameFragment extends Fragment {

     EditText edtRecipeName, edtRecipeCuisine, edtRecipeTags, edtRecipeTypes;
     ImageView ivRecipeImage;
     String image;
     ChipGroup chipGroupMealType;

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

        chipGroupMealType = view.findViewById(R.id.chipGroupMealType);

        chipGroupMealType.setOnCheckedStateChangeListener(new ChipGroup.OnCheckedStateChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull ChipGroup group, @NonNull List<Integer> checkedIds) {
                // Clear the previous selected chips (optional, depending on your use case)
                // You could store the selected chips in a list or handle them as needed.

                for (Integer checkedId : checkedIds) {
                    // Find the chip corresponding to the checked ID
                    Chip selectedChip = view.findViewById(checkedId);

                    // Log the selected chip text (or handle the selection as needed)
                    Log.d("ChipSelection", selectedChip.getText().toString() + " selected");
                }
            }
        });

        // Initialize ActivityResultLaunchers for Camera and Gallery
        cameraLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                Bitmap photo = (Bitmap) result.getData().getExtras().get("data");
                ivRecipeImage.setImageBitmap(photo);
                saveImageToExternalStorage(photo);
            }
        });

//        galleryLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
//            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
//                Uri selectedImageUri = result.getData().getData();
//                try {
//                    Bitmap selectedImageBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImageUri);
//                    ivRecipeImage.setImageBitmap(selectedImageBitmap);
//                   saveImageToExternalStorage(selectedImageBitmap);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        });

        galleryLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                ArrayList<Uri> imageUris = new ArrayList<>();
                // Check if multiple images are selected
                if (result.getData().getClipData() != null) {
                    int count = result.getData().getClipData().getItemCount();
                    for (int i = 0; i < count; i++) {
                        Uri imageUri = result.getData().getClipData().getItemAt(i).getUri();
                        imageUris.add(imageUri);
                        try {
                            Bitmap selectedImageBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                            ivRecipeImage.setImageBitmap(selectedImageBitmap);  // You can display the last selected image for now
                            saveImageToExternalStorage(selectedImageBitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    // Single image selected
                    Uri selectedImageUri = result.getData().getData();
                    imageUris.add(selectedImageUri);
                    try {
                        Bitmap selectedImageBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImageUri);
                        ivRecipeImage.setImageBitmap(selectedImageBitmap);
                        saveImageToExternalStorage(selectedImageBitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                // Now you can use imageUris which holds the list of selected images
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

    public Recipe collectDataFromNameFragment() {
        String recipeName = edtRecipeName.getText().toString().trim();  // Make sure this is the correct EditText for the name
        String recipeCuisine = edtRecipeCuisine.getText().toString().trim();  // Same for cuisine
        ArrayList<String> recipeTags = new ArrayList<>(Arrays.asList(edtRecipeTags.getText().toString().split(",")));
//        ArrayList<String> recipeTypes = new ArrayList<>(Arrays.asList(edtRecipeTypes.getText().toString().split(",")));
        String recipeImage = image;  // Ensure this EditText is properly accessed

        ArrayList<String> recipeTypes = new ArrayList<>();
        int chipCount = chipGroupMealType.getChildCount();
        for (int i = 0; i < chipCount; i++) {
            Chip chip = (Chip) chipGroupMealType.getChildAt(i);
            if (chip.isChecked()) {
                recipeTypes.add(chip.getText().toString());
            }
        }


        return new Recipe(
                recipeName, recipeCuisine, recipeTags, recipeImage, recipeTypes
        );

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
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
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
