/**
 * Author: Pedro Gutierrez Jr.
 * Last Updated: May 14, 2021
 * File Name: AddItemActivity.java
 * Description: Admin can add items to ITEMLOG_TABLE
 */

package com.example.rpgitemshop;

import androidx.appcompat.app.AlertDialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rpgitemshop.db.ItemLogDAO;

import java.util.List;

public class AddItemActivity extends MenuSetUp {

    private TextView mAddItem;

    private EditText mItemNameField;
    private EditText mItemPriceField;
    private EditText mItemQuantityField;
    private EditText mItemDescriptionField;

    private ImageButton mBackButton;
    private Button mConfirmButton;

    private ItemLogDAO mItemLogDAO;

    private ItemLog mItem;

    private List<ItemLog> mItems;

    private String mItemName;
    private String mItemDescription;

    private double mItemPrice;
    private int mItemQuantity;

    private boolean mItemExists = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        mItemLogDAO = Util.getItemDatabase(this);

        wireUpDisplay();
        setOnClickListeners();
    }

    private void wireUpDisplay() {
        mAddItem = (TextView) findViewById(R.id.addItemTextView);

        mItemNameField = (EditText) findViewById(R.id.addItemNameEditText);
        mItemPriceField = (EditText) findViewById(R.id.addItemPriceEditText);
        mItemQuantityField = (EditText) findViewById(R.id.addItemQuantityEditText);
        mItemDescriptionField = (EditText) findViewById(R.id.addItemDescriptionEditText);

        mBackButton = (ImageButton) findViewById(R.id.addItemBackBtn);
        mConfirmButton = (Button) findViewById(R.id.addItemConfirmBtn);
    }

    private void setOnClickListeners() {
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = AdminActivity.newIntent(AddItemActivity.this);
                startActivity(intent);
            }
        });

        mConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getValuesFromDisplay();

                mItems = mItemLogDAO.getItemLogs();

                for(ItemLog item : mItems) {
                    if(mItemName.toLowerCase().equals(item.getItemName().toLowerCase())) {
                        mItemExists = true;
                        break;
                    }
                }

                if(checkFields() && !mItemExists) {
                    addItemsToDatabase();
                } else if(mItemExists) {
                    Toast toast = Toast.makeText(AddItemActivity.this, "This Item is Already In the Store", Toast.LENGTH_SHORT);
                    toast.show();
                }

                mItemExists = false;
            }
        });
    }

    private boolean checkFields() {
        int nameField = mItemNameField.getText().toString().length();
        int priceField = mItemPriceField.getText().toString().length();
        int quantityField = mItemQuantityField.getText().toString().length();
        int descriptionField = mItemDescriptionField.getText().toString().length();

        if(nameField <= 0 || priceField <= 0 || quantityField <= 0 || descriptionField <= 0) {
            Toast toast = Toast.makeText(AddItemActivity.this, "Please Fill Out All Fields", Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }
        return true;
    }

    private void getValuesFromDisplay() {
        mItemName = mItemNameField.getText().toString();

        try {
            mItemPrice = Double.parseDouble(mItemPriceField.getText().toString());
        } catch (NumberFormatException e) {
            Log.d("ITEMLOG", "Couldn't convert price");
        }

        try {
            mItemQuantity = Integer.parseInt(mItemQuantityField.getText().toString());
        } catch (NumberFormatException e) {
            Log.d("ITEMLOG", "Couldn't convert quantity");
        }

        mItemDescription = mItemDescriptionField.getText().toString();
    }

    private void addItemsToDatabase() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this, R.style.DialogTheme);

        if(mItemQuantity <= 1) {
            alertBuilder.setMessage("Add This Item?");
        } else {
            alertBuilder.setMessage("Add These Items?");
        }

        alertBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast toast = Toast.makeText(AddItemActivity.this, "Item(s) Successfully Added", Toast.LENGTH_SHORT);
                        toast.show();

                        mItem = new ItemLog(mItemPrice, mItemName, mItemDescription, 'U');

                        for(int i = 0; i < mItemQuantity; i++) {
                            mItemLogDAO.insert(mItem);
                        }

                        Intent intent = AdminActivity.newIntent(AddItemActivity.this);
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



    public static Intent newIntent(Context packagecontext)  {
        Intent intent = new Intent(packagecontext, AddItemActivity.class);
        return intent;
    }
}