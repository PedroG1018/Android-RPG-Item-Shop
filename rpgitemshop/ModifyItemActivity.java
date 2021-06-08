/**
 * Author: Pedro Gutierrez Jr.
 * Last Updated: May 14, 2021
 * File Name: ModifyItemActivity.java
 * Description: Admin can modify the price and description of items in the store, changes apply to duplicate items
 */

package com.example.rpgitemshop;

import androidx.appcompat.app.AlertDialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.rpgitemshop.db.ItemLogDAO;

import java.util.List;

public class ModifyItemActivity extends MenuSetUp {

    private Spinner mModifyItemSpinner;

    private EditText mItemPriceField;
    private EditText mItemDescriptionField;

    private ImageButton mBackButton;
    private Button mConfirmButton;

    private ItemLogDAO mItemLogDAO;
    private ItemLog mItem;

    private String mSelectedItem;
    private String mItemDescription;

    private double mItemPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_item);

        mItemLogDAO = Util.getItemDatabase(this);

        wireUpDisplay();
        populateSpinner();
        getValuesFromDisplay();
        fillFields();
        setOnClickListeners();
    }

    private void wireUpDisplay() {
        mModifyItemSpinner = (Spinner) findViewById(R.id.modifyItemSpinner);

        mItemPriceField = (EditText) findViewById(R.id.modifyItemPriceEditText);
        mItemDescriptionField = (EditText) findViewById(R.id.modifyItemDescriptionEditText);

        mBackButton = (ImageButton) findViewById(R.id.modifyItemBackBtn);
        mConfirmButton = (Button) findViewById(R.id.modifyItemConfirmBtn);
    }

    private void populateSpinner() {
        List<String> itemNames = mItemLogDAO.getDistinctItems();

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, itemNames);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mModifyItemSpinner.setAdapter(arrayAdapter);
    }

    private void setOnClickListeners() {
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = AdminActivity.newIntent(ModifyItemActivity.this);
                startActivity(intent);
            }
        });

        mConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSelectedItem();
                getValuesFromDisplay();

                if(checkFields()) {
                    updateItemInDatabase();
                }
            }
        });
    }

    private void fillFields() {
        mModifyItemSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mSelectedItem = mModifyItemSpinner.getSelectedItem().toString();

                String itemPrice = mItemLogDAO.getItemByItemName(mSelectedItem).getItemPrice() + "";
                String itemDescription = mItemLogDAO.getItemByItemName(mSelectedItem).getItemDescription() + "";


                mItemPriceField.setHint(itemPrice);
                mItemDescriptionField.setHint(itemDescription);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getSelectedItem() {
        mSelectedItem = mModifyItemSpinner.getSelectedItem().toString();
    }

    private void getValuesFromDisplay() {
        try {
            mItemPrice = Double.parseDouble(mItemPriceField.getText().toString());
        } catch (NumberFormatException e) {
            Log.d("ITEMLOG", "Couldn't convert price");
        }

        mItemDescription = mItemDescriptionField.getText().toString();
    }

    private boolean checkFields() {
        int priceField = mItemPriceField.getText().toString().length();
        int descriptionField = mItemDescriptionField.getText().toString().length();

        if(priceField <= 0 || descriptionField <= 0) {
            Toast toast = Toast.makeText(ModifyItemActivity.this, "Please Fill Out All Fields", Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }
        return true;
    }

    private void updateItemInDatabase() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this, R.style.DialogTheme);

        alertBuilder.setMessage("Modify this item?");

        alertBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        List<ItemLog> items = mItemLogDAO.getMultipleItemsByName(mSelectedItem);

                        for(ItemLog item : items) {
                            item.setItemPrice(mItemPrice);
                            item.setItemDescription(mItemDescription);
                            mItemLogDAO.update(item);
                        }

                        Toast toast = Toast.makeText(ModifyItemActivity.this, mSelectedItem + " has been modified.", Toast.LENGTH_SHORT);
                        toast.show();

                        Intent intent = AdminActivity.newIntent(ModifyItemActivity.this);
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
        Intent intent = new Intent(packagecontext, ModifyItemActivity.class);
        return intent;
    }
}