package com.example.flexyuser;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.flexyuser.ControllerClasses.OrderController;
import com.example.flexyuser.ControllerClasses.UserController;
import com.example.flexyuser.ModelClasses.Order;
import com.example.flexyuser.ModelClasses.OrderItem;
import com.example.flexyuser.ModelClasses.Product;
import com.example.flexyuser.ModelClasses.User;

import java.util.ArrayList;
import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder>
{

    private List<Product> burgerList;
    private Context context;

    private int positionIndex = 0;


    public HomeAdapter(List<Product> burgerList)
    {
        this.burgerList = burgerList;
    }


    public class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView imageBurger1;
        TextView textBurger1;
        TextView priceBurger1;
        CardView cardView1;

        ImageView imageBurger2;
        TextView textBurger2;
        TextView priceBurger2;
        CardView cardView2;

        public ViewHolder(View itemView)
        {
            super(itemView);
            this.imageBurger1 = itemView.findViewById(R.id.imageBurger1);
            this.textBurger1 = itemView.findViewById(R.id.nameBurger1);
            this.priceBurger1 = itemView.findViewById(R.id.textViewPrice1);
            this.cardView1 = itemView.findViewById(R.id.cardViewItem1);

            this.imageBurger2 = itemView.findViewById(R.id.imageBurger2);
            this.textBurger2 = itemView.findViewById(R.id.nameBurger2);
            this.priceBurger2 = itemView.findViewById(R.id.textViewPrice2);
            this.cardView2 = itemView.findViewById(R.id.cardViewItem2);
        }

    }

    @NonNull
    @Override
    public HomeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_home,parent,false);

        HomeAdapter.ViewHolder viewHolder = new HomeAdapter.ViewHolder(view);

        context = parent.getContext();

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final HomeAdapter.ViewHolder holder, final int position) {

        final Product burgerData1 = burgerList.get(positionIndex);
        final Product burgerData2 = burgerList.get(positionIndex + 1);


        //Set burger name  and image on left
        holder.textBurger1.setText(burgerData1.getDescription());
        Glide.with(context).load(burgerData1.getImage()).into(holder.imageBurger1);
        holder.priceBurger1.setText("$" + burgerData1.getPrice());

        holder.cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                createOrder(burgerData1);

                //Move to next
                Intent moveWithData = new Intent( context, AddOnsActivity.class);

                context.startActivity(moveWithData);
            }
        });

        //Set burger name and image or right
        holder.textBurger2.setText(burgerData2.getDescription());
        Glide.with(context).load(burgerData2.getImage()).into(holder.imageBurger2);
        holder.priceBurger2.setText("$" + burgerData2.getPrice());


        holder.cardView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                createOrder(burgerData2);

                //Move to next
                Intent moveWithData = new Intent( context, AddOnsActivity.class);

                context.startActivity(moveWithData);
            }
        });

        positionIndex += 2;
    }

    public void createOrder(Product product){

        UserController userController = new UserController();
        User user = userController.retrieveUser(context);

        OrderController orderController = new OrderController();
        Order order = orderController.retrieveCurrentOrder(context);

        if(order == null) {

            // *********************************************************
            // * Creating new Order and putting in SharedPreferences
            // *********************************************************

            Order newOrder = new Order();

            newOrder.setUserId(user.getId());

            List<Product> listProducts = new ArrayList<>();
            listProducts.add(product);

            List<OrderItem> listItems = new ArrayList<>();

            Integer index = listItems.size() + 1;

            OrderItem orderItem = new OrderItem(index.toString(), listProducts, product.getPrice());
            listItems.add(orderItem);

            newOrder.setItems(listItems);
            newOrder.setTotalPrice(orderItem.getItemPrice());

            orderController.storeOrder(context, newOrder);

        }else{

            // ***********************************************************************
            // * Creating new Item in current Order and putting in SharedPreferences
            // ***********************************************************************

            List<Product> listProducts = new ArrayList<>();
            listProducts.add(product);

            Integer index = order.getItems().size() + 1;

            OrderItem orderItem = new OrderItem(index.toString(), listProducts, product.getPrice());

            order.getItems().add(orderItem);

            // orderController.updateOrderPrice(context, super.);

            order.setTotalPrice(orderItem.getItemPrice());

            orderController.storeOrder(context, order);

        }

    }

    @Override
    public int getItemCount() {
        return burgerList.size()/2;
    }
}
