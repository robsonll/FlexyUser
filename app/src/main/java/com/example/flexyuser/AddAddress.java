package com.example.flexyuser;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.flexyuser.ControllerClasses.UserController;
import com.example.flexyuser.ModelClasses.Address;
import com.example.flexyuser.ModelClasses.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kaopiz.kprogresshud.KProgressHUD;

public class AddAddress extends AppCompatActivity {

    private EditText editTextTitle;
    private EditText editTextNumber;
    private EditText editTextCity;
    private EditText editTextProvince;
    private EditText editTextPostal;
    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);

         editTextTitle = findViewById(R.id.editTextTitle);
         editTextNumber = findViewById(R.id.editTextNo);
         editTextCity = findViewById(R.id.editTextCity);
         editTextProvince = findViewById(R.id.editTextProvince);
         editTextPostal = findViewById(R.id.editTextPostal);
        Button buttonAddAddress= findViewById(R.id.buttonAddAddress);

        db = FirebaseFirestore.getInstance();


        buttonAddAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Validations
                if (editTextTitle.getText().toString().trim().equals("")) {
                    Toast.makeText(AddAddress.this, "Please enter title.", Toast.LENGTH_SHORT).show();

                } else if (editTextNumber.getText().toString().trim().equals("")) {
                    Toast.makeText(AddAddress.this, "Please enter apt number.", Toast.LENGTH_SHORT).show();

                } else if (editTextCity.getText().toString().trim().equals("")) {
                    Toast.makeText(AddAddress.this, "Please enter city.", Toast.LENGTH_SHORT).show();

                } else if (editTextProvince.getText().toString().trim().equals("")) {
                    Toast.makeText(AddAddress.this, "Please enter province.", Toast.LENGTH_SHORT).show();

                } else if (editTextPostal.getText().toString().trim().equals("")) {
                    Toast.makeText(AddAddress.this, "Please enter postal code.", Toast.LENGTH_SHORT).show();

                } else {

                    sendAddress();
                }
            }
        });
    }

    private void sendAddress() {

        final UserController userController = new UserController();

        final User user = userController.retrieveUser(getApplicationContext());

        Address newAdress = new Address(editTextTitle.getText().toString().trim(),
                editTextNumber.getText().toString().trim(),
                editTextPostal.getText().toString().trim(),
                editTextCity.getText().toString().trim(),
                editTextProvince.getText().toString().trim());

        user.getAddressList().add(newAdress);

        //Start HUD
        final KProgressHUD hud = KProgressHUD.create(AddAddress.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setDetailsLabel("Uploading Address")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        db.collection("users").document(user.getId()).set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        hud.dismiss();
                        userController.storeUser(getApplicationContext(), user);

                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(getPackageName(), "Error adding address document", e);
                    }
                });

        //startActivity(new Intent(AddAddress.this, AddOnsActivity.class));
    }

}
