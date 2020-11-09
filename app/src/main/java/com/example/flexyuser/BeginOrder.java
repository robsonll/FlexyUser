package com.example.flexyuser;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.flexyuser.ControllerClasses.BusinessController;
import com.example.flexyuser.ModelClasses.Business;
import com.example.flexyuser.ModelClasses.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.kaopiz.kprogresshud.KProgressHUD;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class BeginOrder extends AppCompatActivity {

    RecyclerView recyclerView;
    BeginOrderAdapter beginOrderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_begin);

        ImageView imageViewBackgroundBeginOrder = findViewById(R.id.imageViewBackgroundBeginOrder);

        BusinessController businessController = new BusinessController();
        Business business = businessController.retrieveCurrentBusiness(BeginOrder.this);

        getSupportActionBar().setTitle(business.getName());
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String imageUrl = business.getBusinessConfiguration().getBackgroundImage();

        Glide.with(BeginOrder.this).load(imageUrl).into(imageViewBackgroundBeginOrder);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        //Create modal class object
        final List<Product> listProducts = new ArrayList<Product>();

        final KProgressHUD hud = KProgressHUD.create(BeginOrder.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setDetailsLabel("Fetching Products")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        //Get burgers from firebase firestore
        db.collection("products").whereIn("type", Arrays.asList("main","combo")).whereEqualTo("businessId", business.getId())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("FIREBASE", document.getId() + " => " + document.get("burgerName"));
                                Product product = document.toObject(Product.class);

                                listProducts.add(product);
                            }

                            beginOrderAdapter = new BeginOrderAdapter(listProducts);
                            recyclerView = findViewById(R.id.recyclerViewProductsList);
                            recyclerView.setLayoutManager(new LinearLayoutManager(BeginOrder.this));
                            recyclerView.setItemAnimator(new DefaultItemAnimator());
                            recyclerView.setAdapter(beginOrderAdapter);

                        } else {
                            Log.d("FIREBASE :", "Error getting documents: ", task.getException());
                        }
                        hud.dismiss();
                    }
                });

        //return view;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(getApplicationContext(),NavigationMainActivity.class));
                return false;
        }
        return super.onOptionsItemSelected(item);
    }

}