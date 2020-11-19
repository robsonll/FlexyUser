package com.example.flexyuser;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.flexyuser.ControllerClasses.BusinessController;
import com.example.flexyuser.ControllerClasses.OrderControler;
import com.example.flexyuser.ModelClasses.Business;
import com.example.flexyuser.ModelClasses.Order;


public class OrderPlaced extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_placed);

        Button buttonCall = findViewById(R.id.buttonCall);
        Button buttonGoHome = findViewById(R.id.buttonGoHome);
        TextView textViewOrderID = findViewById(R.id.textViewOrderID);

        BusinessController businessController = new BusinessController();
        Business business = businessController.retrieveCurrentBusiness(this);

        String imageUrl = business.getBusinessConfiguration().getBackgroundImage();

        ImageView imageViewBackground = findViewById(R.id.imageViewBackgroundOrderPlaced);
        Glide.with(this).load(imageUrl).into(imageViewBackground);

        Bundle bundle = getIntent().getExtras();

        OrderControler orderControler = new OrderControler();
        Order order = orderControler.retrieveCurrentOrder(getApplicationContext());


        if (bundle != null)
        {
            textViewOrderID.setText(order.getId());
        }

        buttonCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(OrderPlaced.this,
                        Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(OrderPlaced.this,
                            new String[]{Manifest.permission.CALL_PHONE},
                            1);

                } else {
                    // Permission has already been granted
                    startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:7783252701")));
                }

            }
        });

        buttonGoHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(OrderPlaced.this, NavigationMainActivity.class));
            }
        });



    }

    @Override
    public void onBackPressed() {

        //Do nothing on back press
        Toast.makeText(this, "No further back allowed.", Toast.LENGTH_SHORT).show();
    }
}
