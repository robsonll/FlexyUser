package com.example.flexyuser.ControllerClasses;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

import com.example.flexyuser.ModelClasses.Business;
import com.example.flexyuser.ModelClasses.Message;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MessageController extends AppCompatActivity {

    private List<Message> listMessages;

    public String retrieveMessageType(Context context, String type){

        final SharedPreferences pref = context.getSharedPreferences("userProfile", 0); // 0 - for private mode

        Gson gson = new Gson();
        String json = pref.getString("currentBusiness", null);
        Business currentBusiness = gson.fromJson(json, Business.class);

        List<Message> listMessages = currentBusiness.getBusinessConfiguration().getMessageList();

        for(Message message : listMessages){
            if(message.getType().equals(type)){
                return message.getContent();
            }
        }

        return "";
    }

}
