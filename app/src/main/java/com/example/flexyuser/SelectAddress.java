package com.example.flexyuser;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.flexyuser.ControllerClasses.UserController;
import com.example.flexyuser.ModelClasses.Address;
import com.example.flexyuser.ModelClasses.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;


public class SelectAddress extends Fragment {

    private FirebaseFirestore db;
    private TextView textViewTitle;
    private List<Address> addressList;
    private SelectAddressAdapter selectAddressAdapter;
    private ListView listViewAddress;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.activity_select_address, container, false);


        ImageView imageViewAddAddress = view.findViewById(R.id.imageViewAdd);

        imageViewAddAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getContext(), AddAddress.class));
            }
        });

        db = FirebaseFirestore.getInstance();

        textViewTitle = view.findViewById(R.id.textViewTitle);
        listViewAddress = view.findViewById(R.id.listViewAddresses);
        listViewAddress.setAdapter(selectAddressAdapter);

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();

        //Get all addresses
        fetchAddresses();
    }


    private void fetchAddresses()
    {

        UserController userController = new UserController();
        User user = userController.retrieveUser(getContext());

        addressList = user.getAddressList();

        //Get addresses from firebase
        db.collection("addresses")
                .whereEqualTo("userID",user.getId())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                //Add each pizza to list
                                Address address = new Address(document.get("addTitle").toString(),document.get("addNumber").toString(),document.get("addPostal").toString(),document.get("addCity").toString(),document.get("addProvince").toString());

                                addressList.add(address);
                            }


                            if (addressList.isEmpty())
                            {
                                //textViewTitle.setText("You haven't added any address yet!");
                                Toast.makeText(getContext(), "You have not added any addresses yet.", Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                selectAddressAdapter = new SelectAddressAdapter(addressList);
                                listViewAddress.setAdapter(selectAddressAdapter);
                            }
                        } else {
                            Log.d("FIREBASE", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}
