package com.example.flexyuser;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.bumptech.glide.Glide;
import com.example.flexyuser.ControllerClasses.BusinessController;
import com.example.flexyuser.ControllerClasses.MessageController;
import com.example.flexyuser.ModelClasses.Business;


public class HomeActivity extends Fragment {

    TextView txtViewWelcomeMsg;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.activity_home, container, false);

        BusinessController businessController = new BusinessController();
        Business business = businessController.retrieveCurrentBusiness(getContext());

        String imageUrl = business.getBusinessConfiguration().getBackgroundImage();

        ImageView imageViewBackgroundHome = view.findViewById(R.id.imageViewBackgroundHome);
        Glide.with(getContext()).load(imageUrl).into(imageViewBackgroundHome);

        txtViewWelcomeMsg = view.findViewById(R.id.textViewWelcomeMsg);

        MessageController messageController = new MessageController();
        String msgWelcome = messageController.retrieveMessageType(getContext(), "msgWelcome");

        txtViewWelcomeMsg.setText(msgWelcome);


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
                startActivity(new Intent(getContext(), BeginOrder.class));
            }
        });

        return view;

    }

}