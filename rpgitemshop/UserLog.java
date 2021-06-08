/**
 * Author: Pedro Gutierrez Jr.
 * Last Updated: May 14, 2021
 * File Name: UserLog.java
 * Description: Creates a table that stores user accounts
 *      Columns: mUserId, mPriceChange, mUsername, mPassword, mIsAdmin
 */

package com.example.rpgitemshop;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.rpgitemshop.db.AppDatabase;

@Entity(tableName = AppDatabase.USERLOG_TABLE)
public class UserLog {

    @PrimaryKey(autoGenerate = true)
    private int mUserId;

    private double mPriceChange;

    private String mUsername;
    private String mPassword;

    private boolean mIsAdmin;

    public UserLog(String username, String password, boolean isAdmin, double priceChange) {
        this.mUsername = username;
        this.mPassword = password;
        this.mIsAdmin = isAdmin;
        this.mPriceChange = priceChange;
    }

    public boolean getIsAdmin() {
        return mIsAdmin;
    }

    public void setIsAdmin(boolean isAdmin) {
        this.mIsAdmin = isAdmin;
    }

    public void setUserId(int userId) {
        this.mUserId = userId;
    }

    public void setUsername(String username) {
        this.mUsername = username;
    }

    public void setPassword(String password) {
        this.mPassword = password;
    }

    public int getUserId() {
        return mUserId;
    }

    public String getUsername() {
        return mUsername;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPriceChange(double priceChange) {
        this.mPriceChange = priceChange;
    }

    public double getPriceChange() {
        return mPriceChange;
    }
}
