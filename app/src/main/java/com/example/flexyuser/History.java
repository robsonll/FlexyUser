package com.example.flexyuser;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flexyuser.ControllerClasses.UserController;
import com.example.flexyuser.ModelClasses.Order;
import com.example.flexyuser.ModelClasses.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.ArrayList;
import java.util.List;

public class History extends Fragment {


    RecyclerView orderHistoryRecyclerView;
    HistoryAdapter historyAdapter;
    ConstraintLayout completeHistory;
    Button arrowBtn;
    CardView cardView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.activity_history, container, false);
        View viewHistory = inflater.inflate(R.layout.adapter_history, container, false);

        //Create modal class object
        final List<Order> orderList = new ArrayList<>();

        // [START get_firestore_instance]
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        //Get id
        final SharedPreferences pref = getActivity().getSharedPreferences("userProfile", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();

        final TextView titleTextView = view.findViewById(R.id.textViewTitle);

        completeHistory = viewHistory.findViewById(R.id.completeHistory);
        arrowBtn = viewHistory.findViewById(R.id.arrowBtn);
        //cardView = viewHistory.findViewById(R.id.cardView);

        arrowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"teste", Toast.LENGTH_LONG).show();
            }
        });


/*
        arrowBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {

                Toast.makeText(getContext(),"teste", Toast.LENGTH_LONG).show();

                if(completeHistory.getVisibility() == View.GONE){
                    TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                    completeHistory.setVisibility(View.VISIBLE);
                    arrowBtn.setBackgroundResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
                }else{
                    TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                    completeHistory.setVisibility(View.GONE);
                    arrowBtn.setBackgroundResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
                }
            }
        });
*/

        //Start HUD
        final KProgressHUD hud = KProgressHUD.create(getContext())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setDetailsLabel("Fetching Orders")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        UserController userController = new UserController();
        User user = userController.retrieveUser(getContext());

        //Get history from firebase
        db.collection("orders")
                .whereEqualTo("userId",user.getId())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        hud.dismiss();

                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("History", document.getId() + " => " + document.get("burgerName"));

                                //Add each burger to list
                                //Order eachOrder = new Order(document.getId(),document.get("burgerName").toString(),document.get("burgerPrice").toString(),document.get("burgerAddOns").toString(),document.get("paymentMethod").toString(),document.get("currentDate").toString(),"");
                                Order eachOrder = document.toObject(Order.class);

                                orderList.add(eachOrder);
                            }


                            if (orderList.isEmpty())
                            {
                                titleTextView.setText("You haven't placed any orders yet!");
                            }
                            else
                            {
                                historyAdapter = new HistoryAdapter(orderList);

                                orderHistoryRecyclerView = view.findViewById(R.id.recyclerViewHistory);

                                orderHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

                                orderHistoryRecyclerView.setItemAnimator(new DefaultItemAnimator());

                                orderHistoryRecyclerView.setAdapter(historyAdapter);
                            }
                        } else {
                            Log.d("FIREBASE", "Error getting documents: ", task.getException());
                        }
                    }
                });

        return view;
    }
}
