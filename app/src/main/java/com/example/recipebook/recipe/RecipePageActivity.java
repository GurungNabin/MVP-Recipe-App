package com.example.recipebook.recipe;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.recipebook.MainActivity;
import com.example.recipebook.R;
import com.example.recipebook.recipe.contract.RecipePageContract;
import com.example.recipebook.recipe.presenter.RecipePagePresenter;
import com.example.recipebook.recipe.view.RecipeNameFragment;
import com.example.recipebook.recipe.database.DBHelper;

public class RecipePageActivity extends AppCompatActivity implements RecipePageContract.View {
    private RecipePagePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe);
        presenter = new RecipePagePresenter(this, new DBHelper(this));

        if (savedInstanceState == null) {
            loadFragment(new RecipeNameFragment());
        }

        Button nextButton = findViewById(R.id.idBtnNext);
        nextButton.setOnClickListener(v -> {
            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            if (currentFragment != null) {
                presenter.handleNext(currentFragment);
            }
        });
    }

    @Override
    public void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
    }


    @Override
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void navigateToMainScreen() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("navigateTo", "foodFragment");
        startActivity(intent);
        finish();
    }
}
