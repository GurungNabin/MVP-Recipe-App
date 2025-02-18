package com.example.recipebook.recipe.view.fragments;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import androidx.fragment.app.Fragment;

import com.example.recipebook.R;
import com.example.recipebook.recipe.model.Recipe;
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
    ArrayList<String> image;
    ChipGroup chipGroupMealType;

    private LinearLayout imageContainer;
    private ArrayList<Bitmap> capturedImages = new ArrayList<>(); // Store captured images

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
        image = new ArrayList<>();

        edtRecipeName = view.findViewById(R.id.idEdtRecipeName);
        edtRecipeCuisine = view.findViewById(R.id.idEdtRecipeCuisine);
        edtRecipeTags = view.findViewById(R.id.idEdtRecipeTags);
        ivRecipeImage = view.findViewById(R.id.idIVRecipeImage);

        chipGroupMealType = view.findViewById(R.id.chipGroupMealType);

        chipGroupMealType.setOnCheckedStateChangeListener(new ChipGroup.OnCheckedStateChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull ChipGroup group, @NonNull List<Integer> checkedIds) {

                for (Integer checkedId : checkedIds) {
                   Chip selectedChip = view.findViewById(checkedId);

                    Log.d("ChipSelection", selectedChip.getText().toString() + " selected");
                }
            }
        });


        cameraLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                Bitmap photo = (Bitmap) result.getData().getExtras().get("data");

                // Add captured photo to the list
                capturedImages.add(photo);

                // Save image to external storage
                saveImageToExternalStorage(photo);

                // Add image to the LinearLayout to show multiple images
                addImageToGallery(photo);

                // Re-trigger the camera for the next photo
                openImageSelector(); // Re-open the camera for the next photo
            }
        });



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

                            // Create ImageView to display the selected image
                            ImageView imageView = new ImageView(getContext());

                            // Set fixed size for all images
                            int imageSize = getResources().getDimensionPixelSize(R.dimen.image_size); // Defined in dimens.xml

                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(imageSize, imageSize);
                            imageView.setLayoutParams(layoutParams);

                            // Set the image bitmap
                            imageView.setImageBitmap(selectedImageBitmap);

                            // Add the ImageView to the LinearLayout
                            LinearLayout linearLayoutImages = getView().findViewById(R.id.selectedImagesCarousel);
                            linearLayoutImages.addView(imageView);

                            // Save the image to external storage
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

                        // Create ImageView to display the selected image
                        ImageView imageView = new ImageView(getContext());

                        // Set fixed size for all images
                        int imageSize = getResources().getDimensionPixelSize(R.dimen.image_size); // Defined in dimens.xml
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(imageSize, imageSize);
                        imageView.setLayoutParams(layoutParams);

                        // Set the image bitmap
                        imageView.setImageBitmap(selectedImageBitmap);

                        // Add the ImageView to the LinearLayout
                        LinearLayout linearLayoutImages = getView().findViewById(R.id.selectedImagesCarousel);
                        linearLayoutImages.addView(imageView);

                        // Save the image to external storage
                        saveImageToExternalStorage(selectedImageBitmap);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
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

    private void addImageToGallery(Bitmap photo) {
        ImageView imageView = new ImageView(getContext());

        // Set fixed size for all images
        int imageSize = getResources().getDimensionPixelSize(R.dimen.image_size); // Defined in dimens.xml
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(imageSize, imageSize);
        imageView.setLayoutParams(layoutParams);

        // Set the image bitmap
        imageView.setImageBitmap(photo);

        // Add the ImageView to the LinearLayout
        LinearLayout linearLayoutImages = getView().findViewById(R.id.selectedImagesCarousel);
        linearLayoutImages.addView(imageView);
    }


    public Recipe collectDataFromNameFragment() {
        String recipeName = edtRecipeName.getText().toString().trim();  // Make sure this is the correct EditText for the name
        if(recipeName.isEmpty()){
            Toast.makeText(getActivity(),"Recipe name is required",Toast.LENGTH_SHORT).show();
            return null;
        }
        String recipeCuisine = edtRecipeCuisine.getText().toString().trim();  // Same for cuisine
        if (recipeCuisine.isEmpty()) {
            Toast.makeText(getActivity(), "Cuisine is required", Toast.LENGTH_SHORT).show();
            return null;
        }
//        ArrayList<String> recipeTags = new ArrayList<>(Arrays.asList(edtRecipeTags.getText().toString().split(",")));
        String tagsInput = edtRecipeTags.getText().toString().trim();
        ArrayList<String> recipeTags = new ArrayList<>();
        if (!tagsInput.isEmpty()) {
            recipeTags.addAll(Arrays.asList(tagsInput.split(",")));
        }
        ArrayList<String> recipeImage = image;  // Ensure this EditText is properly accessed
        if (recipeImage.isEmpty()) {
            Toast.makeText(getActivity(), "At least one image is required", Toast.LENGTH_SHORT).show();
            return null;
        }

        ArrayList<String> recipeTypes = new ArrayList<>();
        int chipCount = chipGroupMealType.getChildCount();
        for (int i = 0; i < chipCount; i++) {
            Chip chip = (Chip) chipGroupMealType.getChildAt(i);
            if (chip.isChecked()) {
                recipeTypes.add(chip.getText().toString());
            }
        }

        if (recipeTypes.isEmpty()) {
            Toast.makeText(getActivity(), "Please select at least one meal type", Toast.LENGTH_SHORT).show();
            return null;
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

            if (image != null) {
                image.add(file.getAbsolutePath());
            } else {
                Log.e("Image Error", "Image list is not initialized!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onStart() {
        super.onStart();

        // Intercept back button press inside this fragment
        requireActivity().getOnBackPressedDispatcher().addCallback(this, new androidx.activity.OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Handle back button action explicitly
                navigateBackToMain();
            }
        });
    }

    // Explicitly manage the back navigation
    private void navigateBackToMain() {
        // Replace the fragment or navigate to your MainActivity
        // For example, let's say you want to replace the current fragment with the MainFragment

        // Option 1: Replace with MainFragment (or MainActivity)
        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new RecipeNameFragment()) // Replace with your main fragment
                .commit();

        // Option 2: If you want to exit the fragment and go back to MainActivity
        requireActivity().finish(); // This will exit the fragment and go back to the MainActivity
    }



}