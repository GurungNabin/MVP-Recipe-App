package com.example.recipebook.food.view;

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
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.recipebook.R;
import com.example.recipebook.food.contract.RecipeNameContract;
import com.example.recipebook.food.presenter.RecipeNamePresenter;
import com.example.recipebook.recipe.model.Recipe;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RecipeNameFragment extends Fragment implements RecipeNameContract.View {

    private RecipeNameContract.Presenter presenter;

    EditText edtRecipeName, edtRecipeCuisine, edtRecipeTags, edtRecipeTypes;
    ImageView ivRecipeImage;
    ArrayList<String> image;
    ChipGroup chipGroupMealType;


    private LinearLayout imageContainer;
    private ArrayList<Bitmap> capturedImages = new ArrayList<>(); // Store captured images

    private ActivityResultLauncher<Intent> cameraLauncher;
    private ActivityResultLauncher<Intent> galleryLauncher;

    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;



    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_name, container, false);

        presenter = new RecipeNamePresenter(this);

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

    private void openImageSelector() {
        CharSequence[] options = new CharSequence[]{"Take Photo","Choose from Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Select Image Source");
        builder.setItems(options, (dialog, which) -> {
            if (which == 0) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraLauncher.launch(cameraIntent);
            }else {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
                galleryLauncher.launch(intent);
            }
        });
        builder.show();
    }

    private void addImageToGallery(Bitmap photo) {
        ImageView imageView = new ImageView(getContext());
        int imageSize = 150;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(imageSize,imageSize);
        imageView.setLayoutParams(layoutParams);

        imageView.setImageBitmap(photo);

        LinearLayout linearLayoutImages = getView().findViewById(R.id.selectedImagesCarousel);
        linearLayoutImages.addView(imageView);
    }

    private void saveImageToExternalStorage(Bitmap bitmap) {
        File directory = new File(getContext().getExternalFilesDir(null), "recipe_images");
        if(!directory.exists()){
            directory.mkdir();
        }

        String fileName = "recipe_image_" + System.currentTimeMillis() + ".png";
        File file = new File(directory, fileName);

        try (FileOutputStream fos = new FileOutputStream(file)){
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            if (image != null){
                image.add(file.getAbsolutePath());
            }else {
                Log.e("Image Error","Image list is not initialized");
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public Recipe collectDataFromNameFragment(){
        String name = edtRecipeName.getText().toString().trim();
        String cuisine = edtRecipeCuisine.getText().toString().trim();
        ArrayList<String> recipeTags = new ArrayList<>(Arrays.asList(edtRecipeTags.getText().toString().split(",")));
        ArrayList<String> mealTypes = new ArrayList<>();

        int chipCount = chipGroupMealType.getChildCount();
        for (int i = 0; i < chipCount; i++) {
            Chip chip = (Chip) chipGroupMealType.getChildAt(i);
            if(chip.isChecked()){
                mealTypes.add(chip.getText().toString());
            }
        }

        presenter.validateAndCollectRecipeData(name, cuisine, recipeTags, image,mealTypes);

        return new Recipe(name, cuisine, recipeTags,image,mealTypes  );
    }

    @Override
    public void showErrorMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onRecipeSavedSuccessfully() {
        Toast.makeText(getActivity(), "Recipe saved successfully", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showRecipeImage(Bitmap image) {
        ivRecipeImage.setImageBitmap(image);
    }

    @Override
    public void updateChipSelection(ArrayList<String> selectedChips) {
        for (int i = 0; i < chipGroupMealType.getChildCount(); i++) {
            Chip chip = (Chip) chipGroupMealType.getChildAt(i);
            if(selectedChips.contains(chip.getText().toString())){
                chip.setChecked(true);
            }
        }
    }


    @Override
    public void onStart() {
        super.onStart();

        requireActivity().getOnBackPressedDispatcher().addCallback(this, new androidx.activity.OnBackPressedCallback(true){
            @Override
            public void handleOnBackPressed() {
                navigateBackToMain();
            }
        });
    }

    private void navigateBackToMain() {
        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new RecipeNameFragment()).commit();

    }
}
