package com.example.recipebook.login.presenter;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.recipebook.login.LoginContract;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class LoginPresenterTest {

    @Mock
    private LoginContract.View mockView;

    @Mock
    private LoginContract.Model mockModel;

    private LoginPresenter loginPresenter;

    @Before
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        loginPresenter = new LoginPresenter(mockView, mockModel);
    }

    @Test
    public void testLoginSuccess(){
        String username = "testUser";
        String password = "testPassword";

        when(mockModel.login(username,password)).thenReturn(true);

        loginPresenter.onLoginButtonClick(username,password);

        verify(mockView).showLoginSuccessMessage();
    }

    @Test
    public void testLoginError(){
        String username = "testUser";
        String password = "wrongPassword";

        when(mockModel.login(username,password)).thenReturn(false);

        loginPresenter.onLoginButtonClick(username,password);

        verify(mockView).showInvalidCredentialsError();
    }

    @Test
    public void testEmptyUsername(){
        String username = "";
        String password = "testPassword";

        loginPresenter.onLoginButtonClick(username,password);

        verify(mockView).showUsernameEmptyError();
    }

    @Test
    public void testEmptyPassword(){
        String username = "testUser";
        String password = "";

        loginPresenter.onLoginButtonClick(username,password);

        verify(mockView).showPasswordEmptyError();
    }

    @Test
    public void testNullUsername(){
        String username = null;
        String password = "testPassword";

        loginPresenter.onLoginButtonClick(username, password);

        verify(mockView).showUsernameEmptyError();
    }

    @Test
    public void testNullPassword(){
        String username = "testUser";
        String password = null;

        loginPresenter.onLoginButtonClick(username, password);

        verify(mockView).showPasswordEmptyError();
    }

      @Test
    public void testLoginFailure(){
        String username = "testUser";
        String password = "wrongPassword";

        when(mockModel.login(username, password)).thenReturn(false);

        loginPresenter.onLoginButtonClick(username, password);

        verify(mockView).showInvalidCredentialsError();
    }

     @Test
    public void testLoginSuccessWhenModelReturnsTrue(){
        String username = "validUser";
        String password = "validPassword";

        when(mockModel.login(username, password)).thenReturn(true);

        loginPresenter.onLoginButtonClick(username, password);

        verify(mockView).showLoginSuccessMessage();
    }
}