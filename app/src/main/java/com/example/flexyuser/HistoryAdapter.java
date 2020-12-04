package com.example.flexyuser;

import android.content.Context;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flexyuser.ControllerClasses.OrderController;
import com.example.flexyuser.ModelClasses.Order;

import java.text.SimpleDateFormat;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder>
{

    private Context context;
    private List<Order> orderList;


    public HistoryAdapter(List<Order> orderList)
    {
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //Create a view from layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_history,parent,false);

        HistoryAdapter.ViewHolder viewHolder = new HistoryAdapter.ViewHolder(view);

        context = parent.getContext();

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final Order orderData = orderList.get(position);
        String strOrderSummary = "";
        String strDate = "";
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");

        OrderController orderController = new OrderController();
        strOrderSummary = orderController.getOrderSummary(orderData);

        if(orderData.getDate() != null) {
            strDate = dateFormat.format(orderData.getDate().toDate());
        }else{
            strDate = "Pending";
        }

        holder.textOrderSummary.setText(Html.fromHtml(strOrderSummary));
        holder.textOrderSummary.setMovementMethod(new ScrollingMovementMethod());

        //holder.textOrderPrice.setText("$" + orderData.getTotalPrice());
        holder.textOrderDate.setText(strDate);
        holder.textOrderID.setText("#" + orderData.getUserId().substring(0,6));
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        //Instanciate all view holder items
        TextView textOrderSummary;
        TextView textOrderPrice;
        TextView textOrderID;
        TextView textOrderDate;

        public ViewHolder(View itemView)
        {
            super(itemView);

            this.textOrderSummary = itemView.findViewById(R.id.textViewOrderSummary);
            //this.textOrderPrice = itemView.findViewById(R.id.textViewPrice);
            this.textOrderID = itemView.findViewById(R.id.textViewOrderID);
            this.textOrderDate = itemView.findViewById(R.id.textViewDate);
        }

    }
}
