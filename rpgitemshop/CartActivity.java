/**
 * Author: Pedro Gutierrez Jr.
 * Last Updated: May 14, 2021
 * File Name: CartActivity.java
 * Description: Users can view items in their cart and check out
 */

package com.example.rpgitemshop;

import androidx.appcompat.app.AlertDialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rpgitemshop.db.ItemLogDAO;
import com.example.rpgitemshop.db.OrderLogDAO;
import com.example.rpgitemshop.db.UserLogDAO;

import java.util.List;

public class CartActivity extends MenuSetUp {
    private TextView mUserCart;

    private ImageButton mBackButton;
    private Button mRemoveItemsButton;
    private Button mCheckoutButton;

    private UserLogDAO mUserLogDAO;
    private OrderLogDAO mOrderLogDAO;
    private ItemLogDAO mItemLogDAO;

    private List<ItemLog> mItems;

    private UserLog mUser;
    private OrderLog mOrder;

    private static int mUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        getDatabase();
        wireUpDisplay();
        displayCart();
        setOnClickListeners();
    }

    private void getDatabase() {
        mUserLogDAO = Util.getUserDatabase(this);
        mItemLogDAO = Util.getItemDatabase(this);
        mOrderLogDAO = Util.getOrderDatabase(this);
    }

    private void wireUpDisplay() {
        mUserCart = (TextView) findViewById(R.id.cartItemsTextView);
        mUserCart.setMovementMethod(new ScrollingMovementMethod());

        mBackButton = (ImageButton) findViewById(R.id.cartBackBtn);
        mRemoveItemsButton = (Button) findViewById(R.id.removeItemsBtn);
        mCheckoutButton = (Button) findViewById(R.id.checkoutBtn);
    }

    private void displayCart() {
        mUser = mUserLogDAO.getUserLogsById(mUserId);
        mItems = mItemLogDAO.getItemLogsByUserId(mUserId);

        if(mItems.size() > 0) {
            for (ItemLog item : mItems) {
                mUserCart.append("Item: " + item.getItemName() + "\n" +
                        "Actual Price: " + (int)(item.getItemPrice()) + " Gil \n" +
                        "Your Price: " + (int)(item.getItemPrice() * mUser.getPriceChange()) + " Gil\n" +
                        "Description: " + item.getItemDescription() + "\n" +
                        "In Stock: " + mItemLogDAO.getItemStock(item.getItemName()) + "\n" +
                        "----------------------------------" + "\n");
            }
        } else {
            String emptyCart = "Your Cart is Empty";
            mRemoveItemsButton.setEnabled(false);
            mCheckoutButton.setEnabled(false);
            mUserCart.setText(emptyCart);
        }
    }

    private void setOnClickListeners() {
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = LandingPage.newIntent(CartActivity.this, mUserId);
                startActivity(intent);
            }
        });

        mRemoveItemsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = RemoveCartItemsWindow.newIntent(CartActivity.this, mUserId);
                startActivity(intent);
            }
        });

        mCheckoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkOut();
            }
        });
    }

    private void checkOut() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this, R.style.DialogTheme);

        alertBuilder.setMessage("Check Out These Items?");

        alertBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for(ItemLog item : mItems) {
                            mOrder = new OrderLog(item.getUserId(), item.getItemPrice() * mUser.getPriceChange(), item.getItemName(), item.getItemDescription());
                            mOrderLogDAO.insert(mOrder);
                            mItemLogDAO.delete(item);
                        }

                        Toast toast = Toast.makeText(CartActivity.this, "Items Successfully Checked Out", Toast.LENGTH_LONG);
                        toast.show();

                        Intent intent = LandingPage.newIntent(CartActivity.this, mUserId);
                        startActivity(intent);
                    }
                });

        alertBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Nothing
                    }
                });

        alertBuilder.create().show();
    }

    public static Intent newIntent(Context packageContext, int userId) {
        Intent intent = new Intent(packageContext, CartActivity.class);
        intent.putExtra(Util.USER_ID_KEY, userId);
        mUserId = userId;
        return intent;
    }
}
