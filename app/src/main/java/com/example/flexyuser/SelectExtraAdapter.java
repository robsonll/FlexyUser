package com.example.flexyuser;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.flexyuser.ControllerClasses.OrderController;
import com.example.flexyuser.ModelClasses.Order;
import com.example.flexyuser.ModelClasses.Product;

import java.util.ArrayList;
import java.util.List;

public class SelectExtraAdapter extends BaseAdapter
{

    List<Product> adapterData = new ArrayList<Product>();

    public SelectExtraAdapter(List<Product> adapterData) {
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
                    .from(parent.getContext()).inflate(R.layout.select_extra_adapter,parent,false);
        }

        TextView textExtraDescription = convertView.findViewById(R.id.extraDescription);
        TextView textExtraPrice = convertView.findViewById(R.id.extraPrice);

        textExtraDescription.setText(adapterData.get(position).getName());
        textExtraPrice.setText("$" + adapterData.get(position).getPrice().toString());

        final TextView txtOrderPrice = parent.getRootView().findViewById(R.id.textViewPrice);

        CheckBox chkExtra = convertView.findViewById(R.id.chkExtra);
        chkExtra.setTag(position);

        chkExtra.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                int pos = (int) buttonView.getTag();
                Long orderPrice;

                OrderController orderController = new OrderController();

                Order order = orderController.retrieveCurrentOrder(parent.getContext());

                if(isChecked)
                    orderController.addProductToOrderItem(order, adapterData.get(pos));
                else
                    orderController.removeProductFromOrderItem(order, adapterData.get(pos));

                orderPrice = orderController.updateOrderPrice(parent.getContext(), order);

                txtOrderPrice.setText("$" + orderPrice);

            }
        });

        return convertView;
    }
}
