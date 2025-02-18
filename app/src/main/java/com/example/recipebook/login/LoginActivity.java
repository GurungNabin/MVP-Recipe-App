package com.example.recipebook.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.recipebook.MainActivity;
import com.example.recipebook.R;
import com.example.recipebook.login.model.LoginModel;
import com.example.recipebook.login.presenter.LoginPresenter;

public class LoginActivity extends AppCompatActivity implements LoginContract.View {
    private EditText etEmail, etPassword;
    private Button btnSignIn;
    private LoginPresenter loginPresenter;
    private LoginContract.Model loginModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main);

        loginModel = new LoginModel();

        //setting ids with their elements
        etEmail = findViewById(R.id.emailEditText);
        etPassword = findViewById(R.id.passwordEditText);
        btnSignIn = findViewById(R.id.loginButton);
        loginPresenter = new LoginPresenter(this, loginModel);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                loginPresenter.onLoginButtonClick(email, password);
            }
        });
    }

    @Override
    public void showInvalidCredentialsError() {
        Toast.makeText(this, R.string.invalid_credentials_error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoginSuccessMessage() {
        Toast.makeText(this, R.string.signin_success, Toast.LENGTH_SHORT).show();
        //upon successful login, the user will be redirected to HomeActivity via Intent
        Intent intent = new Intent(LoginActivity.this,MainActivity.class );
        startActivity(intent);
        finish();
    }

    @Override
    public void showUsernameEmptyError() {
        Toast.makeText(this, "Please enter the username or email", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void showPasswordEmptyError() {
        Toast.makeText(this, "Please enter the password", Toast.LENGTH_SHORT).show();

    }
}
