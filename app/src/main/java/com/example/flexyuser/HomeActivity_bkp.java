package com.example.flexyuser;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.flexyuser.ControllerClasses.BusinessController;
import com.example.flexyuser.ControllerClasses.MessageController;
import com.example.flexyuser.ModelClasses.Business;
import com.google.firebase.firestore.FirebaseFirestore;


public class HomeActivity_bkp extends Fragment {

    RecyclerView recyclerView;
    HomeAdapter homeAdapter;
    TextView txtViewWelcomeMsg;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.activity_home, container, false);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        BusinessController businessController = new BusinessController();
        Business business = businessController.retrieveCurrentBusiness(getContext());

        String imageUrl = business.getBusinessConfiguration().getBackgroundImage();

        Glide.with(this).load(imageUrl).into(new CustomTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                view.setBackground(resource);
            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {
            }

        });

        txtViewWelcomeMsg = view.findViewById(R.id.textViewWelcomeMsg);

        MessageController messageController = new MessageController();
        String msgWelcome = messageController.retrieveMessageType(getContext(), "msgWelcome");

        txtViewWelcomeMsg.setText(msgWelcome);


/*
        Button btnAboutUs = view.findViewById(R.id.buttonAboutUs);

        btnAboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), AboutUsActivity.class));
            }
        });

        Button btnStartOrder = view.findViewById(R.id.buttonStartOrder);

        btnStartOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.fragment_container_home, new BeginOrder());
            }
        });
*/


        return view;

    }

}