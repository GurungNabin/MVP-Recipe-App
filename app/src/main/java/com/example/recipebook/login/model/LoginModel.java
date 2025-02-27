package com.example.recipebook.login.model;

import com.example.recipebook.login.LoginContract;

import java.util.ArrayList;

public class LoginModel implements LoginContract.Model {
     private ArrayList<User> users = new ArrayList<>();

    @Override
    public boolean login(String emailOrUsername, String password) {
        return checkUser(emailOrUsername, password);
    }

     private boolean checkUser(String emailOrUsername, String password){
        users.add(new User("admin@gmail.com","admin", "admin123"));
        users.add(new User("test@gmail.com","test", "test123"));

        for (User user: users){
            if ((user.getUsername().equals(emailOrUsername) || user.getEmail().equals(emailOrUsername) && user.getPassword().equals(password)))
                return true;
        }
        return false;
    }
}
