package com.example.flexyuser;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.flexyuser.ControllerClasses.BusinessOwnerController;
import com.example.flexyuser.ModelClasses.Business;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class ChooseBusiness extends AppCompatActivity {

    private Spinner spinnerListBusiness;
    private ChooseBusinessAdapter adapter;
    private List<Business> listBusinesses;
    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_business);

        spinnerListBusiness = findViewById(R.id.spinnerListBusiness);

        fetchBusinesses();

        BusinessOwnerController businessOwnerController = new BusinessOwnerController();

        spinnerListBusiness.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                Business business = adapter.getItem(position);

                businessOwnerController.storeCurrentBusiness(ChooseBusiness.this, business);

                //Toast.makeText(ChooseBusiness.this, "ID: " + business.getId() + "\nName: " + business.getName(), Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapter) {  }
        });

        Button buttonProceed = findViewById(R.id.buttonProceed);

        buttonProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChooseBusiness.this, NavigationMainActivity.class));
            }
        });


    }

    private void fetchBusinesses()
    {

        db = FirebaseFirestore.getInstance();

        listBusinesses = new ArrayList<Business>();

        CollectionReference businessReference = db.collection("business");
        Query businessDataQuery = businessReference.whereEqualTo("active", true);
        businessDataQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (!task.getResult().isEmpty()){
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Business business = document.toObject(Business.class);
                            listBusinesses.add(business);
                        }

                        adapter = new ChooseBusinessAdapter(ChooseBusiness.this, android.R.layout.simple_spinner_item, listBusinesses);
                        spinnerListBusiness.setAdapter(adapter);

                    }
                }
            }
        });

    }

}