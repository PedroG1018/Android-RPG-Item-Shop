/**
 * Author: Pedro Gutierrez Jr.
 * Last Updated: May 14, 2021
 * File Name: RemoveCartItemsWindow.java
 * Description: Allows users to remove items from their cart
 */

package com.example.rpgitemshop;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rpgitemshop.db.ItemLogDAO;
import com.example.rpgitemshop.db.UserLogDAO;

import java.util.ArrayList;
import java.util.List;

public class RemoveCartItemsWindow extends AppCompatActivity {
    private TextView mCartItemTextView;

    private Spinner mItemSpinner;

    private Button mConfirmButton;

    private UserLogDAO mUserLogDAO;
    private ItemLogDAO mItemLogDAO;
    private List<String> mItemNames = new ArrayList<>();

    private ItemLog mItem;

    private static int mUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_cart_items_window);

        getDatabase();
        wireUpDisplay();
        populateSpinner();
        setOnClickListeners();
    }

    private void getDatabase() {
        mItemLogDAO = Util.getItemDatabase(this);
        mUserLogDAO = Util.getUserDatabase(this);
    }

    private void populateSpinner() {
        List<ItemLog> items = mItemLogDAO.getItemLogsByUserId(mUserId);

        for(ItemLog item : items) {
            mItemNames.add(item.getItemName());
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mItemNames);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mItemSpinner.setAdapter(arrayAdapter);

        items.clear();
    }

    private void wireUpDisplay() {
        mCartItemTextView = (TextView) findViewById(R.id.cartItemTextView);

        mItemSpinner = (Spinner) findViewById(R.id.removeCartItemsSpinner);

        mConfirmButton = (Button) findViewById(R.id.confirmRemoveItemsBtn);
    }

    private void getSelectedItem() {
        String selectedItem = mItemSpinner.getSelectedItem().toString();
        int itemId = mItemLogDAO.getItemByItemName(selectedItem).getItemId();
        mItem = mItemLogDAO.getItemByItemId(itemId);
    }

    private void setOnClickListeners() {
        mConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSelectedItem();

                removeFromCart();
            }
        });

        mItemSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getSelectedItem();
                refreshDisplay();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Do nothing
            }
        });
    }

    private void refreshDisplay() {
        UserLog user = mUserLogDAO.getUserLogsById(mUserId);

        String selectedItemInfo = mItem.getItemName() + "\n" +
                "Actual Price: " + (int)(mItem.getItemPrice()) +  " Gil\n" +
                "Your Price: " + (int)(mItem.getItemPrice() * user.getPriceChange()) + " Gil\n" +
                "Description: " + mItem.getItemDescription();

        mCartItemTextView.setText(selectedItemInfo);
    }

    private void removeFromCart() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this, R.style.DialogTheme);

        alertBuilder.setMessage("Remove this item from your cart?");

        alertBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mItem.setUserId(-1);
                        mItem.setStatus('U');
                        mItemLogDAO.update(mItem);

                        Toast toast = Toast.makeText(RemoveCartItemsWindow.this, "Item Successfully Removed from Cart", Toast.LENGTH_LONG);
                        toast.show();

                        Intent intent = CartActivity.newIntent(RemoveCartItemsWindow.this, mUserId);
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
        Intent intent = new Intent(packageContext, RemoveCartItemsWindow.class);
        intent.putExtra(Util.USER_ID_KEY, userId);
        mUserId = userId;
        return intent;
    }
}