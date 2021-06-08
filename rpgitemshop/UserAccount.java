/**
 * Author: Pedro Gutierrez Jr.
 * Last Updated: May 14, 2021
 * File Name: UserAccount.java
 * Description: Allows users to change their account username and/or password
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

public class UserAccount extends MenuSetUp {

    private TextView mAccount;

    private EditText mUsernameField;
    private EditText mPasswordField;

    private ImageButton mBackButton;
    private Button mConfirmButton;

    private UserLogDAO mUserLogDAO;
    private CharacterLogDAO mCharacterLogDAO;

    private String mUsername;
    private String mPassword;

    private static int mUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account);

        getDatabase();
        wireUpDisplay();
        setFields();
        getFields();
        setOnClickListeners();
    }

    private void getDatabase() {
        mCharacterLogDAO = Util.getCharacterDatabase(this);
        mUserLogDAO = Util.getUserDatabase(this);
    }

    private void wireUpDisplay() {
        mAccount = (TextView) findViewById(R.id.changeAccountTextView);

        mUsernameField = (EditText) findViewById(R.id.changeUsernameEditText);
        mPasswordField = (EditText) findViewById(R.id.changePasswordEditText);

        mBackButton = (ImageButton) findViewById(R.id.accountBackBtn);
        mConfirmButton = (Button) findViewById(R.id.accountConfirmBtn);
    }

    private void setFields() {
        mUsernameField.setText(mUserLogDAO.getUserLogsById(mUserId).getUsername());
        mPasswordField.setText(mUserLogDAO.getUserLogsById(mUserId).getPassword());
    }

    private void getFields() {
        mUsername = mUsernameField.getText().toString();
        mPassword = mPasswordField.getText().toString();
    }

    private void setOnClickListeners() {
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = LandingPage.newIntent(UserAccount.this, mUserId);
                startActivity(intent);
            }
        });

        mConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFields();

                if(mUsername.length() <= 0 || mPassword.length() <= 0) {
                    Toast toast = Toast.makeText(UserAccount.this, "You Must Enter a Username & Password", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    if (mUsername.equals(mUserLogDAO.getUserLogsById(mUserId).getUsername()) && mPassword.equals(mUserLogDAO.getUserLogsById(mUserId).getPassword())) {
                        Toast toast = Toast.makeText(UserAccount.this, "Please Enter Either a New Username or Password", Toast.LENGTH_SHORT);
                        toast.show();
                    } else if(usernameExists()) {
                        Toast toast = Toast.makeText(UserAccount.this, "Username Already Exists - Enter a Different Username", Toast.LENGTH_SHORT);
                        toast.show();
                    } else {
                        changeUserAccount();
                    }
                }
            }
        });

    }

    private boolean usernameExists() {
        List<UserLog> mUsers = mUserLogDAO.getUserLogs();

        for(UserLog user : mUsers) {
            if(user.getUsername().equals(mUsername)) {
                return true;
            }
        }

        return false;
    }

    private void changeUserAccount() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this, R.style.DialogTheme);

        alertBuilder.setMessage("Apply Changes? (This May Affect the Prices You See)");

        alertBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        UserLog updatedUser = mUserLogDAO.getUserLogsById(mUserId);
                        List<CharacterLog> characters = mCharacterLogDAO.getCharacterLogs();

                        updatedUser.setUsername(mUsername);
                        updatedUser.setPassword(mPassword);
                        updatedUser.setPriceChange(1.0);

                        for(CharacterLog character : characters) {
                            if(updatedUser.getUsername().toLowerCase().equals(character.getCharacterName().toLowerCase())) {
                                if(character.getIsVillain()) {
                                    updatedUser.setPriceChange(1.10);
                                } else if(!character.getIsVillain()){
                                    updatedUser.setPriceChange(0.90);
                                }
                                break;
                            }
                        }

                        mUserLogDAO.update(updatedUser);

                        Toast toast = Toast.makeText(UserAccount.this, "Your Changes Have Been Applied", Toast.LENGTH_LONG);
                        toast.show();

                        Intent intent = LandingPage.newIntent(UserAccount.this, mUserId);
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
        Intent intent = new Intent(packageContext, UserAccount.class);
        intent.putExtra(Util.USER_ID_KEY, userId);
        mUserId = userId;
        return intent;
    }
}