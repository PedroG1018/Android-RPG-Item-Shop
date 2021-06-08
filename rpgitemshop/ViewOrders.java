/**
 * Author: Pedro Gutierrez Jr.
 * Last Updated: May 14, 2021
 * File Name: ViewOrders.java
 * Description: Allows users to view their order history
 */

package com.example.rpgitemshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import com.example.rpgitemshop.db.OrderLogDAO;

import java.util.List;

public class ViewOrders extends AppCompatActivity {

    private TextView mUserOrders;

    private OrderLogDAO mOrderLogDAO;

    private static int mUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_orders);

        mOrderLogDAO = Util.getOrderDatabase(this);
        wireUpDisplay();
        showItems();
    }

    private void wireUpDisplay() {
        mUserOrders = (TextView) findViewById(R.id.userOrdersTextView);
        mUserOrders.setMovementMethod(new ScrollingMovementMethod());
    }

    private void showItems() {
        List<OrderLog> orders = mOrderLogDAO.getOrdersByUserId(mUserId);

        for(OrderLog order : orders) {
            mUserOrders.append(order.getOrderName() + "\n" +
                                "You Paid: " + (int)(order.getOrderPrice()) + " Gil\n" +
                                "Description: " + order.getOrderDescription() + "\n" +
                                "----------------------------------" + "\n");
        }
    }

    public static Intent newIntent(Context packageContext, int userId) {
        Intent intent = new Intent(packageContext, ViewOrders.class);
        intent.putExtra(Util.USER_ID_KEY, userId);
        mUserId = userId;
        return intent;
    }
}