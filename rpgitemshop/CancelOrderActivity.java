/**
 * Author: Pedro Gutierrez Jr.
 * Last Updated: May 14, 2021
 * File Name: CancelOrderActivity.java
 * Description: Allows users to cancel any orders in their order history
 */

package com.example.rpgitemshop;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rpgitemshop.db.ItemLogDAO;
import com.example.rpgitemshop.db.OrderLogDAO;

import java.util.ArrayList;
import java.util.List;

public class CancelOrderActivity extends AppCompatActivity {
    private TextView mItemInfo;

    private Spinner mItemSpinner;

    private Button mConfirmButton;

    private ItemLogDAO mItemLogDAO;
    private OrderLogDAO mOrderLogDAO;

    private List<OrderLog> mOrders;
    private List<String> mOrderNames = new ArrayList<>();

    private OrderLog mOrder;
    private ItemLog mItem;

    private static int mUserId;

    private String mSelectedItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel_order);

        getDatabase();
        wireUpDisplay();
        populateSpinner();
        setOnClickListeners();
    }

    private void getDatabase() {
        mItemLogDAO = Util.getItemDatabase(this);
        mOrderLogDAO = Util.getOrderDatabase(this);
    }

    private void wireUpDisplay() {
        mItemInfo = (TextView) findViewById(R.id.cancelOrderItemTextView);

        mItemSpinner = (Spinner) findViewById(R.id.cancelOrderSpinner);

        mConfirmButton = (Button) findViewById(R.id.cancelOrderConfirmBtn);
    }

    private void populateSpinner() {
        mOrders = mOrderLogDAO.getOrdersByUserId(mUserId);

        for (OrderLog order: mOrders) {
            mOrderNames.add(order.getOrderName());
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mOrderNames);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mItemSpinner.setAdapter(arrayAdapter);
    }

    private void setOnClickListeners() {
        mConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSelectedItem();
                cancelOrder();
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

    private void getSelectedItem() {
        mSelectedItem = mItemSpinner.getSelectedItem().toString();
    }

    private void refreshDisplay() {
        mOrder = mOrderLogDAO.getIndividualOrderByOrderName(mSelectedItem);

        String selectedOrderInfo = mOrder.getOrderName() + "\n" +
                "You Paid: " + (int)(mOrder.getOrderPrice()) +  " Gil\n" +
                "Description: " + mOrder.getOrderDescription();

        mItemInfo.setText(selectedOrderInfo);
    }

    private void cancelOrder() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this, R.style.DialogTheme);

        alertBuilder.setMessage("Cancel This Order?");

        alertBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mItem = new ItemLog(mOrder.getOrderPrice(), mOrder.getOrderName(), mOrder.getOrderDescription(), 'U');
                        mItemLogDAO.insert(mItem);
                        mOrderLogDAO.delete(mOrder);

                        Toast toast = Toast.makeText(CancelOrderActivity.this, "Your Order Has Been Cancelled", Toast.LENGTH_LONG);
                        toast.show();

                        Intent intent = LandingPage.newIntent(CancelOrderActivity.this, mUserId);
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
        Intent intent = new Intent(packageContext, CancelOrderActivity.class);
        intent.putExtra(Util.USER_ID_KEY, userId);
        mUserId = userId;
        return intent;
    }
}