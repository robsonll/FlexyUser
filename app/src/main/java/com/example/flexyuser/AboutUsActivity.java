package com.example.flexyuser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.flexyuser.ControllerClasses.BusinessController;
import com.example.flexyuser.ControllerClasses.MessageController;
import com.example.flexyuser.ModelClasses.Business;
import com.example.flexyuser.ModelClasses.Message;

public class AboutUsActivity extends AppCompatActivity {

    TextView txtViewAboutMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        BusinessController businessController = new BusinessController();
        Business business = businessController.retrieveCurrentBusiness(AboutUsActivity.this);

        getSupportActionBar().setTitle(business.getName());
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtViewAboutMsg = findViewById(R.id.textViewAboutMsg);

        MessageController messageController = new MessageController();
        String msgAboutUs = messageController.retrieveMessageType(AboutUsActivity.this, "msgAboutUs");

        txtViewAboutMsg.setText(msgAboutUs);

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