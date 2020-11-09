package com.example.flexyuser;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.flexyuser.ControllerClasses.BusinessController;
import com.example.flexyuser.ControllerClasses.OrderControler;
import com.example.flexyuser.ControllerClasses.UserController;
import com.example.flexyuser.ModelClasses.Business;
import com.example.flexyuser.ModelClasses.Order;
import com.example.flexyuser.ModelClasses.OrderItem;
import com.example.flexyuser.ModelClasses.Product;
import com.example.flexyuser.ModelClasses.User;

import java.util.ArrayList;
import java.util.List;

public class BeginOrderAdapter extends RecyclerView.Adapter<BeginOrderAdapter.ViewHolder>
{

    private List<Product> listProducts;
    private Context context;

    private int positionIndex = 0;


    public BeginOrderAdapter(List<Product> listProducts)
    {
        this.listProducts = listProducts;
    }


    public class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView imageProduct1;
        TextView textProduct1;
        TextView priceProduct1;
        CardView cardView1;

        ImageView imageProduct2;
        TextView textProduct2;
        TextView priceProduct2;
        CardView cardView2;

        public ViewHolder(View itemView)
        {
            super(itemView);
            this.imageProduct1 = itemView.findViewById(R.id.imageProduct1);
            this.textProduct1 = itemView.findViewById(R.id.nameProduct1);
            this.priceProduct1 = itemView.findViewById(R.id.textViewPrice1);
            this.cardView1 = itemView.findViewById(R.id.cardViewItem1);

            this.imageProduct2 = itemView.findViewById(R.id.imageProduct2);
            this.textProduct2 = itemView.findViewById(R.id.nameProduct2);
            this.priceProduct2 = itemView.findViewById(R.id.textViewPrice2);
            this.cardView2 = itemView.findViewById(R.id.cardViewItem2);
        }

    }

    @NonNull
    @Override
    public BeginOrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_begin_order,parent,false);

        BeginOrderAdapter.ViewHolder viewHolder = new BeginOrderAdapter.ViewHolder(view);

        context = parent.getContext();

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final BeginOrderAdapter.ViewHolder holder, final int position) {

        if(positionIndex < listProducts.size()) {
            final Product productData1 = listProducts.get(positionIndex);

            holder.textProduct1.setText(productData1.getName());
            Glide.with(context).load(productData1.getImage()).into(holder.imageProduct1);
            holder.priceProduct1.setText("$" + productData1.getPrice());

            holder.cardView1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    createOrder(productData1);
                    context.startActivity(new Intent(context, AddOnsActivity.class));
                }
            });
            positionIndex++;
        }

        if(positionIndex < listProducts.size()) {
            final Product productData2 = listProducts.get(positionIndex);

            //Set burguer name and image or right
            holder.textProduct2.setText(productData2.getName());
            Glide.with(context).load(productData2.getImage()).into(holder.imageProduct2);
            holder.priceProduct2.setText("$" + productData2.getPrice());


            holder.cardView2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    createOrder(productData2);

                    //Move to next
                    Intent moveWithData = new Intent(context, AddOnsActivity.class);

                    context.startActivity(moveWithData);
                }
            });

            positionIndex++;
        }else{
            holder.cardView2.setVisibility(View.GONE);
        }
    }

    public void createOrder(Product product){

        UserController userController = new UserController();
        User user = userController.retrieveUser(context);

        OrderControler orderControler = new OrderControler();
        Order order = orderControler.retrieveCurrentOrder(context);

        BusinessController businessController = new BusinessController();
        Business business = businessController.retrieveCurrentBusiness(context);

        if(order == null) {

            // *********************************************************
            // * Creating new Order and putting in SharedPreferences
            // *********************************************************

            Order newOrder = new Order();

            newOrder.setUserId(user.getId());
            newOrder.setBusinessId(business.getId());

            List<Product> listProducts = new ArrayList<>();
            listProducts.add(product);

            List<OrderItem> listItems = new ArrayList<>();

            Integer index = listItems.size() + 1;

            OrderItem orderItem = new OrderItem(index.toString(), listProducts, product.getPrice());
            listItems.add(orderItem);

            newOrder.setItems(listItems);
            newOrder.setTotalPrice(orderItem.getItemPrice());

            orderControler.storeOrder(context, newOrder);

        }else{

            // ***********************************************************************
            // * Creating new Item in current Order and putting in SharedPreferences
            // ***********************************************************************

            List<Product> listProducts = new ArrayList<>();
            listProducts.add(product);

            Integer index = order.getItems().size() + 1;

            OrderItem orderItem = new OrderItem(index.toString(), listProducts, product.getPrice());

            order.getItems().add(orderItem);

            // orderControler.updateOrderPrice(context, super.);

            order.setTotalPrice(orderItem.getItemPrice());

            orderControler.storeOrder(context, order);

        }

    }

    @Override
    public int getItemCount() {


        return (listProducts.size() + 1)/2;
    }
}
