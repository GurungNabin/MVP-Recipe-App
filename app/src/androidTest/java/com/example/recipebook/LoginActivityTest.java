package com.example.recipebook;

import android.view.View;
import android.widget.EditText;
import android.widget.Button;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import com.example.recipebook.login.LoginActivity;

@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {

    @Rule
    public IntentsTestRule<LoginActivity> intentsTestRule = new IntentsTestRule<>(LoginActivity.class);

    @Before
    public void setUp() {
        // Initialize any necessary setup before each test
    }

    @Test
    public void testLoginSuccess() {
        // Input valid credentials
        Espresso.onView(withId(R.id.emailEditText))
                .perform(ViewActions.typeText("admin@gmail.com"), ViewActions.closeSoftKeyboard());

        Espresso.onView(withId(R.id.passwordEditText))
                .perform(ViewActions.typeText("admin123"), ViewActions.closeSoftKeyboard());

        // Click on the SignIn button
        Espresso.onView(withId(R.id.loginButton)).perform(ViewActions.click());

        // Check if MainActivity is launched after successful login
        Intents.intended(hasComponent(MainActivity.class.getName()));
    }

    @Test
    public void testInvalidLogin() {
        // Input invalid credentials
        Espresso.onView(withId(R.id.emailEditText))
                .perform(ViewActions.typeText("invalid@gmail.com"), ViewActions.closeSoftKeyboard());

        Espresso.onView(withId(R.id.passwordEditText))
                .perform(ViewActions.typeText("wrongpassword"), ViewActions.closeSoftKeyboard());

        // Click on the SignIn button
        Espresso.onView(withId(R.id.loginButton)).perform(ViewActions.click());

        // Check if the invalid credentials error message is shown
        Espresso.onView(withText(R.string.invalid_credentials_error)).check(matches(isDisplayed()));
    }

    @Test
    public void testEmptyUsername() {
        // Leave username empty and input password
        Espresso.onView(withId(R.id.passwordEditText))
                .perform(ViewActions.typeText("admin123"), ViewActions.closeSoftKeyboard());

        // Click on the SignIn button
        Espresso.onView(withId(R.id.loginButton)).perform(ViewActions.click());

        // Check if the empty username error message is shown
        Espresso.onView(withText("Please enter the username or email")).check(matches(isDisplayed()));
    }

    @Test
    public void testEmptyPassword() {
        // Input username and leave password empty
        Espresso.onView(withId(R.id.emailEditText))
                .perform(ViewActions.typeText("admin@gmail.com"), ViewActions.closeSoftKeyboard());

        // Click on the SignIn button
        Espresso.onView(withId(R.id.loginButton)).perform(ViewActions.click());

        // Check if the empty password error message is shown
        Espresso.onView(withText("Please enter the password")).check(matches(isDisplayed()));
    }
}
