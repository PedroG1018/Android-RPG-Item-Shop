/**
 * Author: Pedro Gutierrez Jr.
 * Last Updated: May 14, 2021
 * File Name: OrderLog.java
 * Description: Creates a table that stores each user's orders
 *      Columns: mOrderId, mUserId, mOrderPrice, mOrderName, mOrderDescription
 */

package com.example.rpgitemshop;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.rpgitemshop.db.AppDatabase;

@Entity(tableName = AppDatabase.ORDERLOG_TABLE)
public class OrderLog {
    @PrimaryKey(autoGenerate = true)
    private int mOrderId;

    private int mUserId;

    private double mOrderPrice;

    private String mOrderName;
    private String mOrderDescription;

    public OrderLog(int mUserId, double mOrderPrice, String mOrderName, String mOrderDescription) {
        this.mUserId = mUserId;
        this.mOrderPrice = mOrderPrice;
        this.mOrderName = mOrderName;
        this.mOrderDescription = mOrderDescription;
    }

    public int getOrderId() {
        return mOrderId;
    }

    public void setOrderId(int orderId) {
        this.mOrderId = orderId;
    }

    public int getUserId() {
        return mUserId;
    }

    public void setUserId(int userId) {
        this.mUserId = userId;
    }

    public double getOrderPrice() {
        return mOrderPrice;
    }

    public void setOrderPrice(double orderPrice) {
        this.mOrderPrice = orderPrice;
    }

    public String getOrderName() {
        return mOrderName;
    }

    public void setOrderName(String orderName) {
        this.mOrderName = orderName;
    }

    public String getOrderDescription() {
        return mOrderDescription;
    }

    public void setOrderDescription(String orderDescription) {
        this.mOrderDescription = orderDescription;
    }
}
