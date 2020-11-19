package com.example.flexyuser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.flexyuser.ControllerClasses.BusinessController;
import com.example.flexyuser.ControllerClasses.OrderControler;
import com.example.flexyuser.ModelClasses.Business;
import com.example.flexyuser.ModelClasses.Order;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.internal.$Gson$Preconditions;

import java.util.Date;

public class CreditCardPayment extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_card_payment);

        BusinessController businessController = new BusinessController();
        Business business = businessController.retrieveCurrentBusiness(getApplicationContext());

        getSupportActionBar().setTitle(business.getName());
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String imageUrl = business.getBusinessConfiguration().getBackgroundImage();

        ImageView imageViewBackground = findViewById(R.id.imageViewBackgroundCCPayment);
        Glide.with(getApplicationContext()).load(imageUrl).into(imageViewBackground);

        Button butonProceed = findViewById(R.id.buttonCCPaymentProceed);
        Button butonCancel = findViewById(R.id.buttonCCPaymentCancel);

        butonProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //OrderControler orderControler = new OrderControler();
                //orderControler.placeOrder( getApplicationContext(),"CC");

                orderPlaced();
            }
        });

        butonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),PaymentMethods.class));
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(getApplicationContext(),PaymentMethods.class));
                return false;
        }
        return super.onOptionsItemSelected(item);
    }

    private void orderPlaced()
    {
        // Getting the updated order info
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        OrderControler orderControler = new OrderControler();
        Order order = orderControler.retrieveCurrentOrder(getApplicationContext());

        order.setPaymentMethod("CC");
        order.setDate(new Timestamp(new Date()));
        order.setStatus("Placed");

        DocumentReference orderRef = db.collection("orders").document(order.getId());

        orderRef
                .set(order)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("ORDER", "Order successfully placed!");

                        startActivity(new Intent(CreditCardPayment.this, OrderPlaced.class));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("ORDER", "Error updating order", e);
                    }
                });

    }

}