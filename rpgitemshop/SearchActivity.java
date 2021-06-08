/**
 * Author: Pedro Gutierrez Jr.
 * Last Updated: May 14, 2021
 * File Name: SearchActivity.java
 * Description: Allows users to search for available items in the store and add them to their cart
 */

package com.example.rpgitemshop;

import androidx.appcompat.app.AlertDialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rpgitemshop.db.ItemLogDAO;
import com.example.rpgitemshop.db.UserLogDAO;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends MenuSetUp {
    private TextView mItemInfo;

    private Spinner mItemSpinner;

    private Button mSearchButton;
    private ImageButton mBackButton;
    private Button mAddToCartButton;

    private UserLogDAO mUserLogDAO;
    private ItemLogDAO mItemLogDAO;

    private List<ItemLog> mItems = new ArrayList<>();

    private String mSelectedItem;

    private static int mUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        getDatabase();
        wireUpDisplay();
        populateSpinner();
        setOnClickListeners();
    }

    private void getDatabase() {
        mItemLogDAO = Util.getItemDatabase(this);
        mUserLogDAO = Util.getUserDatabase(this);
    }

    private void wireUpDisplay() {
        mItemInfo = (TextView) findViewById(R.id.searchItemInfoTextView);
        mItemInfo.setMovementMethod(new ScrollingMovementMethod());

        mItemSpinner = (Spinner) findViewById(R.id.cartItemSpinner);

        mSearchButton = (Button) findViewById(R.id.searchBtn2);
        mBackButton = (ImageButton) findViewById(R.id.searchBackBtn);
        mAddToCartButton = (Button) findViewById(R.id.addToCartBtn);
    }

    private void populateSpinner() {
        List<String> itemNames = mItemLogDAO.getDistinctItems();

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, itemNames);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mItemSpinner.setAdapter(arrayAdapter);
    }

    private void setOnClickListeners() {
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSelectedItem();
                refreshDisplay();
            }
        });

        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = LandingPage.newIntent(SearchActivity.this, mUserId);
                startActivity(intent);
            }
        });

        mAddToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mItems.isEmpty()) {
                    addToCart();
                } else {
                    Toast toast = Toast.makeText(SearchActivity.this, "Please Select An Item", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }

    private void getSelectedItem() {
        mSelectedItem = mItemSpinner.getSelectedItem().toString();
    }

    private void refreshDisplay() {
        int mItemId = mItemLogDAO.getItemByItemName(mSelectedItem).getItemId();

        ItemLog item = mItemLogDAO.getItemByItemId(mItemId);

        boolean check = false;
        boolean c = item.getStatus() == 'C';

        if(mItems.isEmpty()) {
            mItems.add(item);
            if(c) {
                mItems.remove(item);
                check = true;
            }
        } else {
            for(ItemLog itemLog : mItems) {
                if(itemLog.getItemId() == item.getItemId() || c) {
                    check = true;
                    //check
                    break;
                }
            }
        }

        if(check) {
            Toast toast = Toast.makeText(SearchActivity.this, "Item Already Selected or Item Already In a Cart", Toast.LENGTH_LONG);
            toast.show();
        } else {
            mItems.add(item);
            mItemInfo.append("Item: " + item.getItemName() + "\n" +
                    "Your Price: " + (int)(item.getItemPrice() * mUserLogDAO.getUserLogsById(mUserId).getPriceChange()) + " Gil\n" +
                    "Description: " + item.getItemDescription() + "\n" +
                    "In Stock: " + mItemLogDAO.getItemStock(item.getItemName()) + "\n" +
                    "----------------------------------" + "\n");
        }
    }

    private void addToCart() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this, R.style.DialogTheme);

        alertBuilder.setMessage("Add these items to your cart?");

        alertBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for(ItemLog item : mItems) {
                            item.setUserId(mUserId);
                            item.setStatus('C');
                            mItemLogDAO.update(item);
                        }

                        Toast toast = Toast.makeText(SearchActivity.this, "Items Successfully Added to Cart", Toast.LENGTH_LONG);
                        toast.show();

                        Intent intent = SearchActivity.newIntent(SearchActivity.this, mUserId);
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
        Intent intent = new Intent(packageContext, SearchActivity.class);
        intent.putExtra(Util.USER_ID_KEY, userId);
        mUserId = userId;
        return intent;
    }
}