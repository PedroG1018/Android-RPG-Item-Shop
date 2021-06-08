/**
 * Author: Pedro Gutierrez Jr.
 * Last Updated: May 14, 2021
 * File Name: DeleteItemActivity.java
 * Description: Admin can delete a specific item from ITEMLOG_TABLE
 */

package com.example.rpgitemshop;

import androidx.appcompat.app.AlertDialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rpgitemshop.db.ItemLogDAO;

import java.util.List;

public class DeleteItemActivity extends MenuSetUp {
    private TextView mItemInfo;

    private Spinner mDeleteItemSpinner;

    private ImageButton mBackButton;
    private Button mConfirmButton;

    private ItemLog mItem;

    private ItemLogDAO mItemLogDAO;

    private String mSelectedItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_item);

        mItemLogDAO = Util.getItemDatabase(this);

        wireUpDisplay();
        populateSpinner();
        setOnClickListeners();
    }

    private void wireUpDisplay() {
        mItemInfo = (TextView) findViewById(R.id.deleteItemInfoTextView);

        mDeleteItemSpinner = (Spinner) findViewById(R.id.deleteItemSpinner);

        mBackButton = (ImageButton) findViewById(R.id.deleteItemBackBtn);
        mConfirmButton = (Button) findViewById(R.id.deleteItemConfirmBtn);
    }

    private void populateSpinner() {
        List<String> itemNames = mItemLogDAO.getDistinctItems();

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, itemNames);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mDeleteItemSpinner.setAdapter(arrayAdapter);
    }

    private void setOnClickListeners() {
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = AdminActivity.newIntent(DeleteItemActivity.this);
                startActivity(intent);
            }
        });

        mConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSelectedItem();
                deleteItemFromDatabase();
            }
        });

        mDeleteItemSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

    private void getSelectedItem() {
        mSelectedItem = mDeleteItemSpinner.getSelectedItem().toString();
    }

    private void refreshDisplay() {
        mItem = mItemLogDAO.getItemByItemName(mSelectedItem);

        String selectedItemInfo = mItem.getItemName() + "\n" +
                "Price: " + (int)(mItem.getItemPrice()) + " Gil\n" +
                "Description: " + mItem.getItemDescription() + "\n" +
                "In Stock: " + mItemLogDAO.getItemStock(mSelectedItem);

        mItemInfo.setText(selectedItemInfo);
    }

    private void deleteItemFromDatabase() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this, R.style.DialogTheme);

        alertBuilder.setMessage("Delete this Item?");

        alertBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mItemLogDAO.delete(mItem);

                        Toast toast = Toast.makeText(DeleteItemActivity.this, mSelectedItem + " has been deleted.", Toast.LENGTH_SHORT);
                        toast.show();

                        Intent intent = AdminActivity.newIntent(DeleteItemActivity.this);
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
        Intent intent = new Intent(packagecontext, DeleteItemActivity.class);
        return intent;
    }
}