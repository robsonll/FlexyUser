package com.example.flexyuser;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.flexyuser.ControllerClasses.OrderController;
import com.example.flexyuser.ModelClasses.Order;
import com.example.flexyuser.ModelClasses.Message;
import com.example.flexyuser.ControllerClasses.MessageController;


public class OrderPlaced extends AppCompatActivity {

    TextView txtViewCheckoutMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_placed);

        Button buttonCall = findViewById(R.id.buttonCall);
        Button buttonGoHome = findViewById(R.id.buttonGoHome);
        TextView textViewOrderID = findViewById(R.id.textViewOrderID);

        OrderController orderController = new OrderController();
        Order order = orderController.retrieveCurrentOrder(getApplicationContext());

        Bundle bundle = getIntent().getExtras();

        if (order.getId() != null)
        {
            textViewOrderID.setText(order.getId());
        }

//        if (bundle != null)
//        {
//            textViewOrderID.setText(order.getId());
//        }

        txtViewCheckoutMsg = findViewById(R.id.txtViewCheckoutMsg);
        MessageController messageController = new MessageController();
        String msgCheckout = messageController.retrieveMessageType(OrderPlaced.this, "msgCheckout");
        txtViewCheckoutMsg.setText( msgCheckout );


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
                    startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:7783252700")));
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
