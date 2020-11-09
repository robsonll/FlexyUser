package com.example.flexyuser.ControllerClasses;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;
import com.example.flexyuser.ModelClasses.User;
import com.google.gson.Gson;

public class UserController extends AppCompatActivity {


    public User retrieveUser(Context context){

        final SharedPreferences pref = context.getSharedPreferences("userProfile", 0); // 0 - for private mode

        Gson gson = new Gson();
        String json = pref.getString("User", null);
        User user = gson.fromJson(json, User.class);

        return user;
    }

    public void storeUser(Context context, User user){

        SharedPreferences pref = context.getSharedPreferences("userProfile", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();

        Gson gson = new Gson();
        String json = gson.toJson(user);
        editor.putString("User", json);
        editor.commit();

    }
}
