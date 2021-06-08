/**
 * Author: Pedro Gutierrez Jr.
 * Last Updated: May 14, 2021
 * File Name: AdminActivity.java
 * Description: Admin's main page where various admin features are accessible
 */

package com.example.rpgitemshop;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class AdminActivity extends MenuSetUp {

    private Button mAddUserButton;
    private Button mDeleteUserButton;
    private Button mAddItemButton;
    private Button mDeleteItemButton;
    private Button mModifyItemButton;
    private Button mViewUsersButton;
    private Button mViewItemsButton;
    private ImageButton mBackButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        wireUpDisplay();
        setOnClickListeners();
    }

    private void wireUpDisplay() {
        mAddUserButton = (Button) findViewById(R.id.addUserBtn);
        mDeleteUserButton = (Button) findViewById(R.id.deleteUserBtn);
        mAddItemButton = (Button) findViewById(R.id.addItemBtn);
        mDeleteItemButton = (Button) findViewById(R.id.deleteItemBtn);
        mModifyItemButton = (Button) findViewById(R.id.modifyItemBtn);
        mViewUsersButton = (Button) findViewById(R.id.viewUsersBtn);
        mViewItemsButton = (Button) findViewById(R.id.viewItemsBtn);

        mBackButton = (ImageButton) findViewById(R.id.returnBtn);

    }

    private void setOnClickListeners() {
        mAddUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = AddUserActivity.newIntent(AdminActivity.this);
                startActivity(intent);
            }
        });

        mDeleteUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = DeleteUserActivity.newIntent(AdminActivity.this);
                startActivity(intent);
            }
        });

        mAddItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = AddItemActivity.newIntent(AdminActivity.this);
                startActivity(intent);
            }
        });

        mDeleteItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = DeleteItemActivity.newIntent(AdminActivity.this);
                startActivity(intent);
            }
        });

        mModifyItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ModifyItemActivity.newIntent(AdminActivity.this);
                startActivity(intent);
            }
        });

        mViewUsersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ViewExistingUsers.newIntent(AdminActivity.this);
                startActivity(intent);
            }
        });

        mViewItemsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ViewExistingItems.newIntent(AdminActivity.this);
                startActivity(intent);
            }
        });

        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = LandingPage.loginIntent(AdminActivity.this);
                startActivity(intent);
            }
        });
    }

    public static Intent newIntent(Context packagecontext)  {
        Intent intent = new Intent(packagecontext, AdminActivity.class);
        return intent;
    }
}