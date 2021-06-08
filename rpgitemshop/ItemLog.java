/**
 * Author: Pedro Gutierrez Jr.
 * Last Updated: May 14, 2021
 * File Name: ItemLog.java
 * Description: Creates a table that holds items, intended to be filled with common items found in RPG's (i.e. Final Fantasy)
 *      Columns: mItemId, mUserId, mItemPrice, mItemName, mItemDescription, mStatus
 */

package com.example.rpgitemshop;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.rpgitemshop.db.AppDatabase;

@Entity(tableName = AppDatabase.ITEMLOG_TABLE)
public class ItemLog {

    @PrimaryKey(autoGenerate = true)
    private int mItemId;

    private int mUserId;

    private double mItemPrice;

    private String mItemName;
    private String mItemDescription;

    private char mStatus;

    public ItemLog(double itemPrice, String itemName, String itemDescription, char status) {
        this.mItemPrice = itemPrice;
        this.mItemName = itemName;
        this.mItemDescription = itemDescription;
        this.mStatus = status;

        //this.mItems.add(this);
    }

    public int getItemId() {
        return mItemId;
    }

    public void setItemId(int itemId) {
        this.mItemId = itemId;
    }

    public int getUserId() {
        return mUserId;
    }

    public void setUserId(int userId) {
        this.mUserId = userId;
    }

    public double getItemPrice() {
        return mItemPrice;
    }

    public void setItemPrice(double itemPrice) {
        this.mItemPrice = itemPrice;
    }

    public String getItemName() {
        return mItemName;
    }

    public void setItemName(String itemName) {
        this.mItemName = itemName;
    }

    public String getItemDescription() {
        return mItemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.mItemDescription = itemDescription;
    }

    public char getStatus() {
        return mStatus;
    }

    public void setStatus(char status) {
        this.mStatus = status;
    }
}
