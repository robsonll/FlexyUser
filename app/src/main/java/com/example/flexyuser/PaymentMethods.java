package com.example.flexyuser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.flexyuser.ControllerClasses.BusinessController;
import com.example.flexyuser.ControllerClasses.OrderControler;
import com.example.flexyuser.ModelClasses.Business;
import com.example.flexyuser.ModelClasses.Order;
import com.example.flexyuser.ModelClasses.PaymentMethod;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import org.json.JSONException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class PaymentMethods extends AppCompatActivity {

    String paymentMethod = "";
    BigDecimal orderTotal = BigDecimal.ZERO;
    String strOrderProductsDescription = "";
    OrderControler orderControler = new OrderControler();
    Order order;

    private FirebaseFirestore db;
    PayPalConfiguration payPalConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_methods);

        BusinessController businessController = new BusinessController();
        Business business = businessController.retrieveCurrentBusiness(getApplicationContext());

        getSupportActionBar().setTitle(business.getName());
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String imageUrl = business.getBusinessConfiguration().getBackgroundImage();

        ImageView imageViewBackground = findViewById(R.id.imageViewBackgroundPaymentMethods);
        Glide.with(getApplicationContext()).load(imageUrl).into(imageViewBackground);


        order = orderControler.retrieveCurrentOrder(getApplicationContext());

        strOrderProductsDescription = orderControler.getOrderProductsDescriprion(order);
        orderTotal = new BigDecimal(order.getTotalPrice());

        db = FirebaseFirestore.getInstance();
        payPalConfiguration = new PayPalConfiguration()

                // Start with mock environment.  When ready, switch to sandbox (ENVIRONMENT_SANDBOX)
                // or live (ENVIRONMENT_PRODUCTION)
                .environment(PayPalConfiguration.ENVIRONMENT_NO_NETWORK)
                .clientId("AQ9eU8-nCkRasTYTDInP_slqtfmkNGdWUN2J2HNAlta5VOYgl_mmEdGH2pGlYdb5baL6q3J45186HcQ8");


        // **********************************************************
        // * Configuring payment methods
        // **********************************************************

        Button buttonPaypal = findViewById(R.id.buttonPaypal);
        Button buttonCOD = findViewById(R.id.buttonCOD);
        Button buttonCreditCard = findViewById(R.id.buttonCreditCard);
        Button buttonBitcoin = findViewById(R.id.buttonBitcoin);

        buttonPaypal.setVisibility(View.GONE);
        buttonCOD.setVisibility(View.GONE);
        buttonCreditCard.setVisibility(View.GONE);
        buttonBitcoin.setVisibility(View.GONE);

        List<PaymentMethod> listPaymentMethods = business.getBusinessConfiguration().getAcceptedPaymentMethods();

        for(PaymentMethod pm : listPaymentMethods){
            if(pm.getName().equals("Bitcoin"))
                buttonBitcoin.setVisibility(View.VISIBLE);

            if(pm.getName().equals("Credit Card"))
                buttonCreditCard.setVisibility(View.VISIBLE);

            if(pm.getName().equals("Cash"))
                buttonCOD.setVisibility(View.VISIBLE);

            if(pm.getName().equals("PayPal"))
                buttonPaypal.setVisibility(View.VISIBLE);
        }

        buttonPaypal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                payWithPaypal();
            }
        });

        buttonCOD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //TODO: Go to checkout

                paymentMethod = "COD";

                orderPlaced();
            }
        });

        showOrderSummary();

    }

    @Override
    protected void onStart() {
        super.onStart();
    }


    private void showOrderSummary(){

        OrderControler orderControler = new OrderControler();
        Order order = orderControler.retrieveCurrentOrder(getApplicationContext());
        String strOrderSummary = "";

        strOrderSummary += " <b>*****************************<br> ";
        strOrderSummary += "   Order Summary <br>";
        strOrderSummary += " *****************************</b><br> ";
        strOrderSummary += " <br> ";
        strOrderSummary += orderControler.getOrderSummary(order);

        TextView txtOrderSummary = findViewById(R.id.textViewOrderSummary);
        txtOrderSummary.setText(Html.fromHtml(strOrderSummary));

        txtOrderSummary.setMovementMethod(new ScrollingMovementMethod());

    }

    private void payWithPaypal() {

        paymentMethod = "Paypal";

        PayPalPayment payment = new PayPalPayment(orderTotal, "CAD", strOrderProductsDescription,
                PayPalPayment.PAYMENT_INTENT_SALE);

        Intent intent = new Intent(this, PaymentActivity.class);

        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, payPalConfiguration);

        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);

        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
            if (confirm != null) {
                try {
                    Log.i("Payment Stub: ", confirm.toJSONObject().toString(4));

                    //Update order
                    orderPlaced();

                } catch (JSONException e) {
                    Log.e("Payment Stub: ", "An extremely unlikely failure occurred: ", e);
                }
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Log.i("Payment Stub: ", "The user canceled.");
        } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
            Log.i("Payment Stub: ", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
        }
    }


    private void orderPlaced()
    {
        // Getting the updated order info
        order = orderControler.retrieveCurrentOrder(getApplicationContext());

        order.setPaymentMethod(paymentMethod);
        order.setDate(new Timestamp(new Date()));
        order.setStatus("Placed");

        DocumentReference orderRef = db.collection("orders").document(order.getId());

        orderRef
            .set(order)
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d("ORDER", "Order successfully placed!");

                    startActivity(new Intent(PaymentMethods.this, OrderPlaced.class));
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w("ORDER", "Error updating order", e);
                }
            });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(getApplicationContext(),AddOnsActivity.class));
                return false;
        }
        return super.onOptionsItemSelected(item);
    }

}
