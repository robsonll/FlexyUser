package com.example.flexyuser;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.flexyuser.ControllerClasses.UserController;
import com.example.flexyuser.ModelClasses.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    FirebaseFirestore db;
    private String TAG = "LoginActivity";
    EditText editTextEmail, editTextPassword;

    //private String WEB_CLIENT_ID = "450593198851-lak8at4losegmdh6l6es9johv8ugj8la.apps.googleusercontent.com";
    //private String WEB_CLIENT_SECRET = "Xleq5-tdfF5WWGGTlilmq3iO";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        Button buttonLogin = findViewById(R.id.buttonLogin);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginAction();
            }
        });

    }


    //Button methods
    void loginAction(){

        //Check for empty fields and then login

        if (editTextEmail.getText().toString().equals(""))
        {
            Toast.makeText(this, "Please enter email address.", Toast.LENGTH_SHORT).show();
        }
        else if (!isValidEmail(editTextEmail.getText().toString()))
        {
            Toast.makeText(this, "Please enter a valid email.", Toast.LENGTH_SHORT).show();
        }
        else if(editTextPassword.getText().toString().equals(""))
        {
            Toast.makeText(this, "Please enter password.", Toast.LENGTH_SHORT).show();
        }
        else
        {
            //Get authentication using firebase
            checkInFirebase();
        }
    }

    public static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    private void checkInFirebase() {


        //Firebase authentication
        mAuth.signInWithEmailAndPassword(editTextEmail.getText().toString(), editTextPassword.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            // Sign in success, update UI with the signed-in user's information
                            Log.d("FIREBASE ::", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            try {
                                verifyUser(user);
                            }
                            catch (Exception ex)
                            {
                                Toast.makeText(LoginActivity.this, "Error occured!!", Toast.LENGTH_SHORT).show();
                            }

                        } else {

                            Log.w("FIREBASE ::", "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed. User does not exist. Sign Up first.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }


                    private void verifyUser(FirebaseUser fbUser){

                        final String userEmail = fbUser.getEmail();
                        final UserController userController = new UserController();

                        db = FirebaseFirestore.getInstance();

                        CollectionReference buyersReference = db.collection("users");
                        Query usersDataQuery = buyersReference.whereEqualTo("email", userEmail);
                        usersDataQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {

                                    if (!task.getResult().isEmpty()){
                                        // User exists in Firebase Authenticaton and in the database
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            User user = document.toObject(User.class);

                                            userController.storeUser(getApplicationContext(), user);

                                            startActivity(new Intent(LoginActivity.this, ChooseBusiness.class));
                                        }
                                    }else{
                                        Toast.makeText(LoginActivity.this, "Authentication failed. Contact admin.",
                                                Toast.LENGTH_SHORT).show();
                                    }

                                }
                            }
                        });

                    }

                });

    }

}
