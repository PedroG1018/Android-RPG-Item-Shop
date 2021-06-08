/**
 * Author: Pedro Gutierrez Jr.
 * Last Updated: May 14, 2021
 * File Name: LandingPage.java
 * Description: Landing page for logged in users
 */

package com.example.rpgitemshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rpgitemshop.db.OrderLogDAO;
import com.example.rpgitemshop.db.UserLogDAO;

import java.util.List;

public class LandingPage extends AppCompatActivity {

    private TextView mWelcomeMessage;

    private Button mSearchButton;
    private Button mCartButton;
    private Button mOrderHistoryButton;
    private Button mCancelOrderButton;
    private Button mAdminButton;

    private UserLogDAO mUserLogDAO;
    private OrderLogDAO mOrderLogDAO;

    private List<OrderLog> mOrderLogs;

    private int mUserId = -1;

    private SharedPreferences mPreferences = null;

    private UserLog mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);

        getDatabse();

        checkForUsers();
        addUserToPreference(mUserId);
        loginUser(mUserId);

        wireUpDisplay();
        setOnClickListeners();
    }

    private void getDatabse() {
        mUserLogDAO = Util.getUserDatabase(this);
        mOrderLogDAO = Util.getOrderDatabase(this);
    }

    private void checkForUsers() {
        mUserId = getIntent().getIntExtra(Util.USER_ID_KEY, -1);

        if(mUserId != -1) {
            return;
        }

        if(mPreferences == null) {
            getPrefs();
        }

        mUserId = mPreferences.getInt(Util.USER_ID_KEY, -1);

        if(mUserId != -1) {
            return;
        }

        Intent intent = LoginActivity.newIntent(this);
        startActivity(intent);
    }

    private void getPrefs() {
        mPreferences = this.getSharedPreferences(Util.PREFERENCES_KEY, Context.MODE_PRIVATE);
    }

    private void addUserToPreference(int userId) {
        if(mPreferences == null) {
            getPrefs();
        }
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putInt(Util.USER_ID_KEY, userId);
        editor.apply();
    }

    private void loginUser(int userId) {
        mUser = mUserLogDAO.getUserLogsById(userId);
        invalidateOptionsMenu();
    }

    private void checkIfAdmin() {
        if(!mUser.getIsAdmin()) {
            mAdminButton.setVisibility(View.INVISIBLE);
        }
    }

    private void wireUpDisplay() {
        mWelcomeMessage = (TextView) findViewById(R.id.welcomeTextView);

        mWelcomeMessage.append(",  " + mUser.getUsername());

        if(mUser.getPriceChange() == 0.90) {
            mWelcomeMessage.append("\n10% Discount on All Items For Your Service");
        } else if(mUser.getPriceChange() == 1.10){
            mWelcomeMessage.append("\n10% Tax on All Items For Your Disservice");
        }

        mSearchButton = (Button) findViewById(R.id.searchBtn);
        mCartButton = (Button) findViewById(R.id.viewCartBtn);
        mOrderHistoryButton = (Button) findViewById(R.id.historyBtn);
        mCancelOrderButton = (Button) findViewById(R.id.cancelOrderBtn);
        mAdminButton = (Button) findViewById(R.id.adminBtn);

        checkIfAdmin();
    }

    private void setOnClickListeners() {
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = SearchActivity.newIntent(LandingPage.this, mUserId);
                startActivity(intent);
            }
        });

        mCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = CartActivity.newIntent(LandingPage.this, mUserId);
                startActivity(intent);
            }
        });

        mOrderHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ViewOrders.newIntent(LandingPage.this, mUserId);
                startActivity(intent);
            }
        });

        mCancelOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOrderLogs = mOrderLogDAO.getOrdersByUserId(mUserId);

                if(mOrderLogs.size() <= 0) {
                    Toast toast = Toast.makeText(LandingPage.this, "You Have Not Made Any Orders", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    Intent intent = CancelOrderActivity.newIntent(LandingPage.this, mUserId);
                    startActivity(intent);
                }
            }
        });

        mAdminButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = AdminActivity.newIntent(LandingPage.this);
                startActivity(intent);
            }
        });
    }

    private void logoutUser() {
        clearUserFromIntent();
        clearUserFromPref();
        checkForUsers();

        Toast t = Toast.makeText(getApplicationContext(), "Successfully Logged Out", Toast.LENGTH_SHORT);
        t.show();
    }

    private void clearUserFromIntent() {
        getIntent().putExtra(Util.USER_ID_KEY, -1);
    }

    private void clearUserFromPref() {
        addUserToPreference(-1);
    }

    public static Intent newIntent(Context packageContext, int userId) {
        Intent intent = new Intent(packageContext, LandingPage.class);
        intent.putExtra(Util.USER_ID_KEY, userId);
        return intent;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.account:
                return true;
            case R.id.accountInfo:
                Intent intent = UserAccount.newIntent(LandingPage.this, mUserId);
                startActivity(intent);
                return true;
            case R.id.logout:
                SharedPreferences sharedPref = getSharedPreferences("login", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean("isLogin", false);
                editor.apply();

                logoutUser();

                intent = MainActivity.newIntent(LandingPage.this);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static Intent loginIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, LandingPage.class);
        return intent;
    }
}