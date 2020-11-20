package com.example.flexyuser.ControllerClasses;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;

import com.example.flexyuser.ModelClasses.Order;
import com.example.flexyuser.ModelClasses.OrderItem;
import com.example.flexyuser.ModelClasses.Product;
import com.google.gson.Gson;

import java.util.Iterator;
import java.util.List;


public class OrderController extends AppCompatActivity {

    public Product getMainProduct(Order order){

        // Returns the main product of the last item inserted in the order

        Product mainProduct = null;

        OrderItem lastItem = order.getItems().get(order.getItems().size() - 1);

        List<Product> listProducts = lastItem.getProducts();

        for(Product product : listProducts){
            if((product.getType().equals("main")) || (product.getType().equals("combo"))){
                mainProduct = product;
            }
        }

        return mainProduct;

    }

    public Order retrieveCurrentOrder(Context context){

        SharedPreferences pref = context.getSharedPreferences("userProfile", 0); // 0 - for private mode

        Gson gson = new Gson();
        String json = pref.getString("Order", null);
        Order order = gson.fromJson(json, Order.class);

        return order;

    }

    public void storeOrder(Context context, Order order){

        SharedPreferences pref = context.getSharedPreferences("userProfile", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        Gson gson = new Gson();

        String json = gson.toJson(order);
        editor.putString("Order", json);
        editor.commit();


    }

    public void addProductToOrderItem(Order order, Product product){

        OrderItem orderItem = order.getItems().get(order.getItems().size() - 1);
        List<Product> listProduct = orderItem.getProducts();

        listProduct.add(product);

    }

    public void removeProductFromOrderItem(Order order, Product product){

        OrderItem orderItem = order.getItems().get(order.getItems().size() - 1);

        Iterator<Product> itr = orderItem.getProducts().iterator();

        while (itr.hasNext()) {
            Product productItem = itr.next();

            if (product.getId().equals(productItem.getId())) {
                itr.remove();
            }
        }

    }

    public Long updateOrderPrice(Context context, Order order){

        Long itemTotal = Long.valueOf(0);
        Long orderTotal = Long.valueOf(0);


        // Updating totals inside order

        for(OrderItem orderItem : order.getItems()){
            for(Product itemProduct : orderItem.getProducts()){
                itemTotal += itemProduct.getPrice();
            }
            orderItem.setItemPrice(itemTotal);
            orderTotal += itemTotal;
            itemTotal= Long.valueOf(0);
        }

        order.setTotalPrice(orderTotal);

        // Saving updated order into SharedPreferences

        SharedPreferences pref = context.getSharedPreferences("userProfile", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();

        Gson gson = new Gson();

        String json = gson.toJson(order);
        editor.putString("Order", json);
        editor.commit();

        // Toast.makeText(context, "OrderPrice: " + order.getTotalPrice(), Toast.LENGTH_LONG).show();
        Log.i("ORDER","OrderPrice: " + order.getTotalPrice());

        return order.getTotalPrice();

    }

    public void cleanOrder(Context context){

        Order newOrder = null;

        SharedPreferences pref = context.getSharedPreferences("userProfile", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();

        Gson gson = new Gson();

        String json = gson.toJson(newOrder);
        editor.putString("Order", json);
        editor.commit();

    }

    public String getOrderSummary(Order order){

        String strOrderSummary = "";

        for(OrderItem orderItem : order.getItems()){
            strOrderSummary += " <b>Item</b> " + orderItem.getId() + "<br>" ;

            for(Product product : orderItem.getProducts()){
                strOrderSummary += "     " + product.getDescription() + " - $" + product.getPrice() + "<br>";
            }
            strOrderSummary += "<br>";
            strOrderSummary += " <b>Item Total</b> : $" + orderItem.getItemPrice().toString() + "<br>";
            strOrderSummary += "<br>";
        }

        strOrderSummary += "<br>";
        strOrderSummary += " <b>Order Total</b> : $" + order.getTotalPrice().toString();

        return strOrderSummary;

    }

    public String getOrderProductsDescriprion(Order order){

        String strOrderProdutcsDescription = "";

        for(OrderItem orderItem : order.getItems()){
            for(Product product : orderItem.getProducts()){
                strOrderProdutcsDescription += product.getDescription() + ", ";
            }
        }

        return strOrderProdutcsDescription;

    }

}
