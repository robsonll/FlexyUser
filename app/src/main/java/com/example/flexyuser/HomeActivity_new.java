package com.example.flexyuser;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.flexyuser.ControllerClasses.BusinessController;
import com.example.flexyuser.ControllerClasses.MessageController;
import com.example.flexyuser.ModelClasses.Business;
import com.google.firebase.firestore.FirebaseFirestore;


public class HomeActivity_new extends AppCompatActivity {

    RecyclerView recyclerView;
    HomeAdapter homeAdapter;
    TextView txtViewWelcomeMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ImageView imgViewHome = findViewById(R.id.imageViewBackground3);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        BusinessController businessController = new BusinessController();
        Business business = businessController.retrieveCurrentBusiness(this);

        String imageUrl = business.getBusinessConfiguration().getBackgroundImage();

        Glide.with(this).load(imageUrl).into(new CustomTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                // imgViewHome.setBackground(resource);
            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {

            }

        });

        txtViewWelcomeMsg = findViewById(R.id.textViewWelcomeMsg);

        MessageController messageController = new MessageController();
        String msgWelcome = messageController.retrieveMessageType(this, "msgWelcome");

        txtViewWelcomeMsg.setText(msgWelcome);


/*
        Button btnAboutUs = findViewById(R.id.buttonAboutUs);

        btnAboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity_new.this, AboutUsActivity.class));
            }
        });

        Button btnStartOrder = findViewById(R.id.buttonStartOrder);

        btnStartOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getFragmentManager().beginTransaction().replace(R.id.fragment_container_home, new BeginOrder());
                //startActivity(new Intent(HomeActivity.this, BeginOrder.class));
            }
        });
*/


    }

}