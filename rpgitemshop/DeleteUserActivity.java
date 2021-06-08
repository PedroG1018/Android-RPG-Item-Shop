/**
 * Author: Pedro Gutierrez Jr.
 * Last Updated: May 14, 2021
 * File Name: DeleteUserActivity.java
 * Description: Admin can delete a specific user from USERLOG_TABLE
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
import com.example.rpgitemshop.db.OrderLogDAO;
import com.example.rpgitemshop.db.UserLogDAO;

import java.util.ArrayList;
import java.util.List;

public class DeleteUserActivity extends MenuSetUp {
    private TextView mUserInfo;

    private Spinner mDeleteUserSpinner;

    private ImageButton mBackButton;
    private Button mConfirmButton;

    private UserLog mUser;
    private List<OrderLog> mUserOrders;
    private List<ItemLog> mCartItems;

    private UserLogDAO mUserLogDAO;
    private ItemLogDAO mItemLogDAO;
    private OrderLogDAO mOrderLogDAO;

    private String mSelectedItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_user);

        getDatabase();
        wireUpDisplay();

        populateSpinner();

        setOnClickListeners();
    }

    private void getDatabase() {
        mUserLogDAO = Util.getUserDatabase(this);
        mItemLogDAO = Util.getItemDatabase(this);
        mOrderLogDAO = Util.getOrderDatabase(this);
    }

    private void wireUpDisplay() {
        mUserInfo = (TextView) findViewById(R.id.deleteUserInfoTextView);

        mDeleteUserSpinner = (Spinner) findViewById(R.id.deleteUserSpinner);

        mBackButton = (ImageButton) findViewById(R.id.deleteUserBackBtn);
        mConfirmButton = (Button) findViewById(R.id.confirmDeleteUserBtn);
    }

    private void populateSpinner() {
        List<UserLog> users = mUserLogDAO.getUserLogs();

        List<String> usernames = new ArrayList<>();

        for(UserLog user : users) {
            if(!user.getIsAdmin()) {
                usernames.add(user.getUsername());
            }
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, usernames);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mDeleteUserSpinner.setAdapter(arrayAdapter);
    }

    private void setOnClickListeners() {
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = AdminActivity.newIntent(DeleteUserActivity.this);
                startActivity(intent);
            }
        });

        mConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSelectedItem();

                deleteUserFromDatabase();
            }
        });

        mDeleteUserSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        mSelectedItem = mDeleteUserSpinner.getSelectedItem().toString();
    }

    private void refreshDisplay() {
        mUser = mUserLogDAO.getUserByUsername(mSelectedItem);
        mCartItems = mItemLogDAO.getItemLogsByUserId(mUser.getUserId());
        mUserOrders = mOrderLogDAO.getOrdersByUserId(mUser.getUserId());

        StringBuilder selectedItemInfo = new StringBuilder("User: " + mUser.getUsername() + "\n\n" +
                "Items in Cart:" + "\n");

        if(mCartItems.size() > 0) {
            for (ItemLog cartItem : mCartItems) {
                selectedItemInfo.append(cartItem.getItemName()).append("\n").append("Actual Price: ").append((int)(cartItem.getItemPrice())).append(" Gil\n").append("User's Price: ").append((int)(cartItem.getItemPrice() * mUser.getPriceChange())).append(" Gil\n").append("Description: ").append(cartItem.getItemDescription()).append("\n\n");
            }
        } else {
            selectedItemInfo.append(" 0" + "\n\n");
        }

        selectedItemInfo.append("Orders:" + "\n");

        if(mUserOrders.size() > 0) {
            for (OrderLog userOrder : mUserOrders) {
                selectedItemInfo.append(userOrder.getOrderName()).append("\n").append("User Paid: ").append((int)(userOrder.getOrderPrice() * mUser.getPriceChange())).append(" Gil\n").append("Description: ").append(userOrder.getOrderDescription()).append("\n").append("-------------------------------------").append("\n");
            }
        } else {
            selectedItemInfo.append(" 0");
        }

        mUserInfo.setText(selectedItemInfo.toString());
    }

    private void deleteUserFromDatabase() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this, R.style.DialogTheme);

        alertBuilder.setMessage("Delete user " + mSelectedItem + "?");

        alertBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(mCartItems.size() > 0) {
                            for(ItemLog cartItem : mCartItems) {
                                cartItem.setUserId(0);
                                cartItem.setStatus('U');
                                mItemLogDAO.update(cartItem);
                            }
                        }

                        if(mUserOrders.size() > 0) {
                            for(OrderLog userOrder : mUserOrders) {
/*                                mItem = new ItemLog(userOrder.getOrderPrice(), userOrder.getOrderName(), userOrder.getOrderDescription(), 'U');
                                mItemLogDAO.insert(mItem);*/
                                mOrderLogDAO.delete(userOrder);
                            }
                        }

                        mUserLogDAO.delete(mUser);

                        Toast toast = Toast.makeText(DeleteUserActivity.this, mSelectedItem + " has been deleted.", Toast.LENGTH_SHORT);
                        toast.show();

                        Intent intent = AdminActivity.newIntent(DeleteUserActivity.this);
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
        Intent intent = new Intent(packagecontext, DeleteUserActivity.class);
        return intent;
    }
}