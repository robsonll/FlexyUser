package com.example.flexyuser;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.flexyuser.ControllerClasses.OrderControler;
import com.example.flexyuser.ModelClasses.Order;
import com.example.flexyuser.ModelClasses.Product;

import java.util.ArrayList;
import java.util.List;

public class SelectDrinkAdapter extends BaseAdapter
{

    List<Product> adapterData = new ArrayList<>();

    public SelectDrinkAdapter(List<Product> adapterData) {
        this.adapterData = adapterData;
    }

    @Override
    public int getCount() {
        return adapterData.size();
    }

    @Override
    public Product getItem(int position) {
        return adapterData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent)
    {
        if (convertView == null)
        {
            convertView = LayoutInflater
                    .from(parent.getContext()).inflate(R.layout.select_drink_adapter,parent,false);
        }

        TextView textDrinkDescription = convertView.findViewById(R.id.drinkDescription);
        TextView textDrinkPrice = convertView.findViewById(R.id.drinkPrice);

        final TextView txtOrderPrice = parent.getRootView().findViewById(R.id.textViewPrice);

        CheckBox chkDrink = convertView.findViewById(R.id.chkDrink);
        chkDrink.setTag(position);

        textDrinkDescription.setText(adapterData.get(position).getName());
        textDrinkPrice.setText("$" + adapterData.get(position).getPrice().toString());

        chkDrink.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int pos = (int) buttonView.getTag();
                Long orderPrice;

                OrderControler orderControler = new OrderControler();

                Order order = orderControler.retrieveCurrentOrder(parent.getContext());

                if(isChecked)
                    orderControler.addProductToOrderItem(order, adapterData.get(pos));
                else
                    orderControler.removeProductFromOrderItem(order, adapterData.get(pos));

                orderPrice = orderControler.updateOrderPrice(parent.getContext(), order);

                txtOrderPrice.setText("$" + orderPrice);

            }
        });

        return convertView;
    }
}
