package com.example.flexyuser.ControllerClasses;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.flexyuser.ChooseBusiness;
import com.example.flexyuser.LoginActivity;
import com.example.flexyuser.ModelClasses.Business;
import com.example.flexyuser.ModelClasses.BusinessOwner;
import com.example.flexyuser.ModelClasses.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class BusinessController extends AppCompatActivity {

    private FirebaseFirestore db;
    private List<Business> listBusinesses;

    public Business retrieveCurrentBusiness(Context context){

        final SharedPreferences pref = context.getSharedPreferences("userProfile", 0); // 0 - for private mode

        Gson gson = new Gson();
        String json = pref.getString("currentBusiness", null);
        Business currentBusiness = gson.fromJson(json, Business.class);

        return currentBusiness;
    }

    public void storeCurrentBusiness(Context context, Business currentBusiness){

        SharedPreferences pref = context.getSharedPreferences("userProfile", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();

        Gson gson = new Gson();
        String json = gson.toJson(currentBusiness);
        editor.putString("currentBusiness", json);
        editor.commit();

    }

    public List<Business> retrieveListBusinesses(Context context){

        final SharedPreferences pref = context.getSharedPreferences("userProfile", 0); // 0 - for private mode

        Gson gson = new Gson();
        String json = pref.getString("listBusinesses", null);

        Type listType = new TypeToken<ArrayList<Business>>(){}.getType();
        List<Business> listBusinesses = gson.fromJson(json, listType);

        return listBusinesses;
    }

    public void storeListBusinesses(Context context, List<Business> listBusinesses){

        SharedPreferences pref = context.getSharedPreferences("userProfile", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();

        Gson gson = new Gson();
        String json = gson.toJson(listBusinesses);
        editor.putString("listBusinesses", json);
        editor.commit();

    }

}
