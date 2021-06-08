/**
 * Author: Pedro Gutierrez Jr.
 * Last Updated: May 14, 2021
 * File Name: AddUserActivity.java
 * Description: Admin can add new users to USERLOG_TABLE
 */

package com.example.rpgitemshop;

import androidx.appcompat.app.AlertDialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rpgitemshop.db.CharacterLogDAO;
import com.example.rpgitemshop.db.UserLogDAO;

import java.util.List;

public class AddUserActivity extends MenuSetUp {

    private String mUsername;
    private String mPassword;

    private TextView mAddUser;

    private EditText mUsernameField;
    private EditText mPasswordField;

    private ImageButton mBackButton;
    private Button mConfirmButton;

    private UserLogDAO mUserLogDAO;
    private CharacterLogDAO mCharacterLogDAO;

    private UserLog mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        getDatabase();

        wireUpDisplay();
        setOnClickListeners();
    }

    private void getDatabase() {
        mUserLogDAO = Util.getUserDatabase(this);
        mCharacterLogDAO = Util.getCharacterDatabase(this);
    }

    private void wireUpDisplay() {
        mAddUser = (TextView) findViewById(R.id.addUserTextView);

        mUsernameField = (EditText) findViewById(R.id.addUsernameEditText);
        mPasswordField = (EditText) findViewById(R.id.addPasswordEditText);

        mBackButton = (ImageButton) findViewById(R.id.backBtn);
        mConfirmButton = (Button) findViewById(R.id.confirmBtn);
    }

    private void setOnClickListeners() {
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = AdminActivity.newIntent(AddUserActivity.this);
                startActivity(intent);
            }
        });

        mConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getValuesFromDisplay();

                if(!checkForUserInDatabase()) {
                    addUserToDatabase();
                }
            }
        });
    }

    private void getValuesFromDisplay() {
        mUsername = mUsernameField.getText().toString();
        mPassword = mPasswordField.getText().toString();
    }

    private boolean checkForUserInDatabase() {
        mUser = mUserLogDAO.getUserByUsername(mUsername);

        if(mUser != null) {
            Toast toast = Toast.makeText(AddUserActivity.this, "User Already Exists", Toast.LENGTH_SHORT);
            toast.show();
            return true;
        }
        return false;
    }

    private void addUserToDatabase() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this, R.style.DialogTheme);

        alertBuilder.setMessage("Add This User?");

        alertBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        List<CharacterLog> characters = mCharacterLogDAO.getCharacterLogs();
                        UserLog newUser = new UserLog(mUsername, mPassword, false, 1.0);

                        for(CharacterLog character : characters) {
                            if (mUsername.toLowerCase().equals(character.getCharacterName().toLowerCase())) {
                                if(character.getIsVillain()) {
                                    newUser.setPriceChange(1.10);
                                } else {
                                    newUser.setPriceChange(0.90);
                                }
                                break;
                            }
                        }

                        mUserLogDAO.insert(newUser);

                        Toast toast = Toast.makeText(AddUserActivity.this, "User Successfully Added", Toast.LENGTH_SHORT);
                        toast.show();

                        Intent intent = AdminActivity.newIntent(AddUserActivity.this);
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
        Intent intent = new Intent(packagecontext, AddUserActivity.class);
        return intent;
    }
}