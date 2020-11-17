package com.example.flexyuser;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.flexyuser.ControllerClasses.BusinessController;
import com.example.flexyuser.ControllerClasses.OrderControler;
import com.example.flexyuser.ControllerClasses.UserController;
import com.example.flexyuser.ModelClasses.Address;
import com.example.flexyuser.ModelClasses.Business;
import com.example.flexyuser.ModelClasses.Order;
import com.example.flexyuser.ModelClasses.Product;
import com.example.flexyuser.ModelClasses.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;

public class AddOnsActivity extends AppCompatActivity {

    private String burgerPrice = "";
    private List<Address> addressList;
    private List<Product> drinkList;
    private List<Product> extraList;
    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ons);


        Button buttonPay = findViewById(R.id.buttonPayment);
        Button buttonAddOtherItem = findViewById(R.id.buttonAddOtherItem);
        final TextView textViewBurgerName = findViewById(R.id.textViewName);
        final TextView textViewBurgerPrice = findViewById(R.id.textViewPrice);
        ImageView imageViewBurger = findViewById(R.id.imageViewBurger);

        db = FirebaseFirestore.getInstance();

        BusinessController businessController = new BusinessController();
        Business business = businessController.retrieveCurrentBusiness(this);

        getSupportActionBar().setTitle(business.getName());
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String imageUrl = business.getBusinessConfiguration().getBackgroundImage();

        ImageView imageViewBackground = findViewById(R.id.imageViewBackgroundAddOns);
        Glide.with(getApplicationContext()).load(imageUrl).into(imageViewBackground);

        if (!business.getBusinessConfiguration().getShowDrinks()){
            View viewDrinks = findViewById(R.id.viewDrinks);
            TextView textViewTitleDrinks = findViewById(R.id.textViewTitleDrinks);
            TextView textViewDivDrinks = findViewById(R.id.textViewDivDrinks);
            GridView grid_drinks = findViewById(R.id.grid_drinks);

            viewDrinks.setVisibility(View.GONE);
            textViewTitleDrinks.setVisibility(View.GONE);
            textViewDivDrinks.setVisibility(View.GONE);
            grid_drinks.setVisibility(View.GONE);
        }

        if (!business.getBusinessConfiguration().getShowAddOns()){
            View viewExtras = findViewById(R.id.viewExtras);
            TextView textViewTitleExtras = findViewById(R.id.textViewTitleExtras);
            TextView textViewDivExtras = findViewById(R.id.textViewDivExtras);
            GridView grid_extras = findViewById(R.id.grid_extras);

            viewExtras.setVisibility(View.GONE);
            textViewTitleExtras.setVisibility(View.GONE);
            textViewDivExtras.setVisibility(View.GONE);
            grid_extras.setVisibility(View.GONE);
        }


        final OrderControler orderControler = new OrderControler();
        final Order order = orderControler.retrieveCurrentOrder(getApplicationContext());

        Product mainProduct = orderControler.getMainProduct(order);

        burgerPrice = mainProduct.getPrice().toString();
        textViewBurgerName.setText(mainProduct.getName());
        textViewBurgerPrice.setText("$" + burgerPrice);
        Glide.with(this).load(mainProduct.getImage()).into(imageViewBurger);


        buttonPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Spinner spinnerAddress = findViewById(R.id.spinnerAddress);
                Integer itemPosition = spinnerAddress.getSelectedItemPosition();

                Order finalOrder = orderControler.retrieveCurrentOrder(getApplicationContext());

                finalOrder.setUserAddress(String.valueOf(itemPosition));
                orderControler.storeOrder(getApplicationContext(), finalOrder);

                saveOrder();

                Intent intent = new Intent(AddOnsActivity.this, PaymentMethods.class);
                startActivity(intent);

            }
        });

        buttonAddOtherItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        fetchAddresses();
        fetchDrinks();
        fetchExtras();
        updateOrderPrice();
    }


    private void updateOrderPrice(){

        Long orderPrice;

        OrderControler orderControler = new OrderControler();
        Order order = orderControler.retrieveCurrentOrder(getApplicationContext());

        orderPrice = orderControler.updateOrderPrice(getApplicationContext(), order);

        TextView txtOrderPrice = findViewById(R.id.textViewPrice);

        txtOrderPrice.setText("$" + orderPrice);

    }

    private void fetchAddresses() {

        UserController userController = new UserController();

        User user = userController.retrieveUser(getApplicationContext());

        addressList = user.getAddressList();


        if (addressList.isEmpty())
        {
            startActivity(new Intent(AddOnsActivity.this, AddAddress.class));
        }
        else {
            Spinner spinnerAddress = findViewById(R.id.spinnerAddress);

            SelectAddressAdapter selectAddressAdapter = new SelectAddressAdapter(addressList);
            spinnerAddress.setAdapter(selectAddressAdapter);

        }

    }


    private void fetchDrinks()
    {

        drinkList = new ArrayList<>();

        //Get drinks from firebase
        db.collection("products")
                .whereEqualTo("type","drink")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                drinkList.add(document.toObject(Product.class));
                            }

                            if (drinkList.isEmpty())
                            {
                                //textViewTitle.setText("You haven't added any address yet!");
                                Log.d("FIREBASE", "No drinks available at this time");
                                // Toast.makeText(getContext(), "No drinks available at this time", Toast.LENGTH_LONG).show();
                            }
                            else
                            {

                                SelectDrinkAdapter selectDrinkAdapter = new SelectDrinkAdapter(drinkList);
                                GridView gridDrinks = findViewById(R.id.grid_drinks);

                                gridDrinks.setNumColumns(2);
                                gridDrinks.setAdapter(selectDrinkAdapter);

                            }
                        } else {
                            Log.d("FIREBASE", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }


    private void fetchExtras()
    {

        extraList = new ArrayList<>();

        //Get extras from firebase
        db.collection("products")
                .whereEqualTo("type","extra")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                extraList.add(document.toObject(Product.class));
                            }

                            if (extraList.isEmpty())
                            {
                                //textViewTitle.setText("You haven't added any address yet!");
                                Log.d("FIREBASE", "No extras available at this time");
                                // Toast.makeText(getContext(), "No drinks available at this time", Toast.LENGTH_LONG).show();
                            }
                            else
                            {

                                SelectExtraAdapter selectExtraAdapter = new SelectExtraAdapter(extraList);
                                GridView grid = findViewById(R.id.grid_extras);

                                grid.setNumColumns(2);
                                grid.setAdapter(selectExtraAdapter);

                            }
                        } else {
                            Log.d("FIREBASE", "Error getting documents: ", task.getException());
                        }

                    }
                });
    }

    private void saveOrder(){

        final OrderControler orderControler = new OrderControler();
        final Order order = orderControler.retrieveCurrentOrder(getApplicationContext());

        db.collection("orders")
                .add(order)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {

                        documentReference.update("id",documentReference.getId());
                        order.setId(documentReference.getId());
                        order.setStatus("Pending");
                        orderControler.storeOrder(getApplicationContext(), order);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("ORDER", "Error adding order", e);
                    }
                });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(getApplicationContext(),BeginOrder.class));
                return false;
        }
        return super.onOptionsItemSelected(item);
    }


}
