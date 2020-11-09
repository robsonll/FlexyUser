package com.example.flexyuser;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.flexyuser.ModelClasses.Address;

import java.util.ArrayList;
import java.util.List;


public class SelectAddressAdapter extends BaseAdapter
{

    List<Address> adapterData = new ArrayList<Address>();

    public SelectAddressAdapter(List<Address> adapterData) {
        this.adapterData = adapterData;
    }

    @Override
    public int getCount() {
        return adapterData.size();
    }

    @Override
    public Address getItem(int position) {
        return adapterData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if (convertView == null)
        {
            convertView = LayoutInflater
                    .from(parent.getContext()).inflate(R.layout.select_address_adapter,parent,false);
        }

        TextView textViewTitle = convertView.findViewById(R.id.addressTitle);
        TextView textViewAptNumber = convertView.findViewById(R.id.addressNumber);
        TextView textViewFull = convertView.findViewById(R.id.addressFull);

        String tempFullAddress = adapterData.get(position).getCity() + ", " + adapterData.get(position).getProvince() + " - " + adapterData.get(position).getPostalCode();

        textViewTitle.setText(String.valueOf(adapterData.get(position).getAddressTitle()));
        textViewAptNumber.setText(adapterData.get(position).getAptNumber());
        textViewFull.setText(tempFullAddress);


        return convertView;
    }
}
