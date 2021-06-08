/**
 * Author: Pedro Gutierrez Jr.
 * Last Updated: May 14, 2021
 * File Name: ViewExistingUsers.java
 * Description: Admin can view all users in USERLOG_TABLE, including their cart items and order history
 */

package com.example.rpgitemshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.rpgitemshop.db.ItemLogDAO;
import com.example.rpgitemshop.db.OrderLogDAO;
import com.example.rpgitemshop.db.UserLogDAO;

import java.util.List;

public class ViewExistingUsers extends AppCompatActivity {

    private TextView mUserInfo;

    private Button mBackButton;

    private UserLogDAO mUserLogDAO;
    private ItemLogDAO mItemLogDAO;
    private OrderLogDAO mOrderLogDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_existing_users);

        getDatabase();
        wireUpDisplay();
        displayUserInfo();

        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = AdminActivity.newIntent(ViewExistingUsers.this);
                startActivity(intent);
            }
        });
    }

    private void getDatabase() {
        mItemLogDAO = Util.getItemDatabase(this);
        mUserLogDAO = Util.getUserDatabase(this);
        mOrderLogDAO = Util.getOrderDatabase(this);
    }

    private void wireUpDisplay() {
        mUserInfo = (TextView) findViewById(R.id.viewUserInfoTextView);
        mUserInfo.setMovementMethod(new ScrollingMovementMethod());

        mBackButton = (Button) findViewById(R.id.viewUserBackBtn);
    }

    private void displayUserInfo() {
        StringBuilder userInfo = new StringBuilder();

        List<UserLog> users = mUserLogDAO.getUserLogs();

        for(UserLog user : users) {
            userInfo.append("User: " + user.getUsername() + "\n" +
                        "Items in Cart: " + "\n");

            List<ItemLog> cartItems = mItemLogDAO.getItemLogsByUserId(user.getUserId());
            List<OrderLog> userOrders = mOrderLogDAO.getOrdersByUserId(user.getUserId());

            if(cartItems.size() > 0) {
                for (ItemLog cartItem : cartItems) {
                    userInfo.append(cartItem.getItemName() + "\n" +
                            "Actual Price: " + (int)(cartItem.getItemPrice()) + " Gil\n" +
                            "User's Price: " + (int)(cartItem.getItemPrice() * user.getPriceChange()) + " Gil\n" +
                            "Description: " + cartItem.getItemDescription() + "\n\n");
                }
            } else {
                userInfo.append(" 0" + "\n");
            }

            userInfo.append("Orders:" + "\n");

            if(userOrders.size() > 0) {
                for (OrderLog userOrder : userOrders) {
                    userInfo.append(userOrder.getOrderName() + "\n" +
                            "User Paid: " + (int)(userOrder.getOrderPrice()) + " Gil\n" +
                            "Description: " + userOrder.getOrderDescription() + "\n" +
                            "-------------------------------------" + "\n");
                }
            } else {
                userInfo.append(" 0" + "\n" +
                        "-------------------------------------" + "\n");
            }
        }

        mUserInfo.setText(userInfo);
    }

    public static Intent newIntent(Context packagecontext)  {
        Intent intent = new Intent(packagecontext, ViewExistingUsers.class);
        return intent;
    }
}