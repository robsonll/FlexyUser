package com.example.flexyuser;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.flexyuser.ControllerClasses.BusinessController;
import com.example.flexyuser.ControllerClasses.UserController;
import com.example.flexyuser.ModelClasses.Business;
import com.example.flexyuser.ModelClasses.User;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;


public class NavigationMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    GoogleSignInClient googleSignInClient;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Set deafult theme before on create

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_main);


        //contextOfApplication = getApplicationContext();

        //Tell app to remove default action bar and use tool bar
        Toolbar toolbar = findViewById(R.id.toolBar);
        //setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        UserController userController = new UserController();
        User user = userController.retrieveUser(getApplicationContext());

        BusinessController businessController = new BusinessController();
        Business business = businessController.retrieveCurrentBusiness(NavigationMainActivity.this);

        getSupportActionBar().setTitle(business.getName());
        getSupportActionBar().setHomeButtonEnabled(true);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Menu menu = navigationView.getMenu();
        menu.clear();

        navigationView.inflateMenu(R.menu.drawer_menu);

        //Get header view to load data
        View headerView = navigationView.getHeaderView(0);
        TextView profileName = headerView.findViewById(R.id.textViewName);


        profileName.setText(user.getName());

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        if (savedInstanceState == null)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeActivity()).commit();
            //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new BlankFragment()).commit();
            //startActivity(new Intent(NavigationMainActivity.this, HomeActivity.class));

            //navigationView.setCheckedItem(R.id.nav_home);
        }

        // Configure sign-in to request the user's ID, email address, and basic profile.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();

        // Build a GoogleSignInClient with the options specified by gso.
        googleSignInClient = GoogleSignIn.getClient(this, gso);


    }

/*
    public static Context getContextOfApplication()
    {
        return contextOfApplication;
    }
*/

    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else
        {
            //super.onBackPressed();
            Toast.makeText(this, "No further back allowed.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId())
        {
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeActivity()).commit();
                break;

            case R.id.nav_history:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new History()).commit();
                break;

            case R.id.nav_share:
                //Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show();
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Check out this amazing application to order Burgers!"); // Simple text and URL to share
                sendIntent.setType("text/plain");
                this.startActivity(sendIntent);
                break;

            case R.id.nav_address:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SelectAddress()).commit();
                break;

            case R.id.nav_logout:
                signOut();
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }



    //Handle logout
    public void signOut() {

        UserController userController = new UserController();
        User user = userController.retrieveUser(getApplicationContext());

        int loginType = user.getLoginType();

        switch (loginType) {
            case 0: {
                FirebaseAuth.getInstance().signOut();

                final SharedPreferences pref = getApplicationContext().getSharedPreferences("userProfile", 0); // 0 - for private mode
                SharedPreferences.Editor editor = pref.edit();
                editor.clear();
                editor.apply();

                Toast.makeText(this, "Successfully signed out.", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, LandingActivity.class));
            }
            break;
            case 1: {
                googleSignInClient.signOut()
                        .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                // ...

                                final SharedPreferences pref = getApplicationContext().getSharedPreferences("userProfile", 0); // 0 - for private mode
                                SharedPreferences.Editor editor = pref.edit();
                                editor.clear();
                                editor.apply();

                                Toast.makeText(NavigationMainActivity.this, "Successfully signed out.", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(NavigationMainActivity.this, LandingActivity.class));
                            }
                        });

            }
            break;
            default:
                startActivity(new Intent(this, LoginActivity.class));
                break;
        }

    }
}
