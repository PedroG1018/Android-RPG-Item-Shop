/**
 * Author: Pedro Gutierrez Jr.
 * Last Updated: May 14, 2021
 * File Name: ViewExistingItems.java
 * Description: Admin can view all existing items in ITEMLOG_TABLE
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

import java.util.List;

public class ViewExistingItems extends AppCompatActivity {
    private TextView mItemInfo;

    private Button mBackButton;

    private ItemLogDAO mItemLogDAO;

    private List<String> mItemNames;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_existing_items);

        mItemLogDAO = Util.getItemDatabase(this);

        wireUpDisplay();
        displayItems();

        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = AdminActivity.newIntent(ViewExistingItems.this);
                startActivity(intent);
            }
        });
    }

    private void wireUpDisplay() {
        mItemInfo = (TextView) findViewById(R.id.viewItemInfoTextView);
        mItemInfo.setMovementMethod(new ScrollingMovementMethod());

        mBackButton = (Button) findViewById(R.id.viewItemsBackBtn);
    }

    private void displayItems() {
        mItemNames = mItemLogDAO.getDistinctItems();

        StringBuilder itemInfo = new StringBuilder();

        if(mItemNames.size() > 0) {
            for(String itemName : mItemNames) {
                ItemLog item = mItemLogDAO.getItemByItemName(itemName);

                itemInfo.append("Item: " + item.getItemName() + "\n" +
                                "Price: " + (int)(item.getItemPrice()) + " Gil \n" +
                                "Description: " + item.getItemDescription() + "\n" +
                                "In Stock: " + mItemLogDAO.getItemStock(item.getItemName()) + "\n" +
                                "----------------------------------" + "\n");
            }

            mItemInfo.setText(itemInfo);
        } else {
            String noItemsMessage = "Looks Like We're Out of Inventory";
            mItemInfo.setText(noItemsMessage);
        }
    }

    public static Intent newIntent(Context packagecontext)  {
        Intent intent = new Intent(packagecontext, ViewExistingItems.class);
        return intent;
    }
}