package com.example.flexyuser;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.flexyuser.ControllerClasses.UserController;
import com.example.flexyuser.ModelClasses.User;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


public class LandingActivity extends AppCompatActivity {

    //Google sign in
    int RC_SIGN_IN = 0;
    SignInButton signInButton;
    GoogleSignInClient mGoogleSignInClient;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        Button buttonLogin = findViewById(R.id.buttonLogin);
        Button buttonSignUp = findViewById(R.id.buttonSignUp);


        //Initialize
        callGoogleSignIn();

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(LandingActivity.this, LoginActivity.class));
            }
        });

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LandingActivity.this, SignUpActivity.class));
            }
        });

    }

    //Google Methods
    void callGoogleSignIn()
    {

        // Set the dimensions of the sign-in button.
        SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);

        // Configure sign-in to request the user's ID, email address, and basic profile.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }


    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "No further back allowed.", Toast.LENGTH_SHORT).show();
    }


    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.

            try {
                verifyUser(account);
            }
            catch (Exception ex)
            {
                Toast.makeText(this, "Error occured!!", Toast.LENGTH_SHORT).show();
            }

        } catch (ApiException e) {
            Log.w("GoogleSignInError", "signInResult:failed code=" + e.getLocalizedMessage());
            Toast.makeText(this, "GoogleSignInError" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void verifyUser(GoogleSignInAccount googleUser){

        final String userName = googleUser.getDisplayName();
        final String userEmail = googleUser.getEmail();

        final UserController userController = new UserController();

        db = FirebaseFirestore.getInstance();

        CollectionReference buyersReference = db.collection("users");
        Query usersDataQuery = buyersReference.whereEqualTo("email", googleUser.getEmail());
        usersDataQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    if (!task.getResult().isEmpty()){
                        // User exists in the database
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            User user = document.toObject(User.class);

                            userController.storeUser(getApplicationContext(), user);

                            startActivity(new Intent(LandingActivity.this, ChooseBusiness.class));
                        }

                    }else{
                        // User does not exist in the database. Thus, save user in users document

                        final User newUser = new User(null, userName, userEmail, null, 1);

                        db.collection("users")
                                .add(newUser)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {

                                        documentReference.update("id",documentReference.getId());
                                        newUser.setId(documentReference.getId());

                                        Log.d("LOGIN", "DocumentSnapshot written with ID: " + documentReference.getId());

                                        userController.storeUser(getApplicationContext(), newUser);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w("LOGIN", "Error adding document", e);
                                    }
                                });

                        userController.storeUser(getApplicationContext(), newUser);

                        startActivity(new Intent(LandingActivity.this, ChooseBusiness.class));

                    }

                }
            }
        });

    }


}
