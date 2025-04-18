package com.example.recipebook.login.model;

import com.example.recipebook.login.LoginContract;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class LoginModel implements LoginContract.Model {
//     private ArrayList<User> users = new ArrayList<>();

//    @Override
//    public boolean login(String emailOrUsername, String password) {
//        return checkUser(emailOrUsername, password);
//    }
//
//     private boolean checkUser(String emailOrUsername, String password){
//        users.add(new User("admin@gmail.com","admin", "admin123"));
//        users.add(new User("test@gmail.com","test", "test123"));
//
//        for (User user: users){
//            if ((user.getUsername().equals(emailOrUsername) || user.getEmail().equals(emailOrUsername) && user.getPassword().equals(password)))
//                return true;
//        }
//        return false;
//    }

    @Override
    public void login(String username, String password, LoginCallback callback) {
        new Thread(()->{
            try {
                URL url = new URL("https://dummyjson.com/auth/login");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "application/json");

                JSONObject payload = new JSONObject();
                payload.put("username", username);
                payload.put("password", password);

                OutputStream os = conn.getOutputStream();
                os.write(payload.toString().getBytes());
                os.flush();
                os.close();
                int responseCode = conn.getResponseCode();
                if (responseCode == 200){
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();

                    JSONObject responseJson = new JSONObject(response.toString());
                    String token = responseJson.getString("accessToken");

                    // You can save the token using SharedPreferences if needed
                    callback.onSuccess(token);
                }else{
                    callback.onError("Invalid credentials");
                }
            }catch (Exception e){
                callback.onError("Login failed" + e.getMessage());
            }
        }).start();
    }
}
