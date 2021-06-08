/**
 * Author: Pedro Gutierrez Jr.
 * Last Updated: May 14, 2021
 * File Name: CreateAccountActivity.java
 * Description: Allows users to create a new account
 */

package com.example.rpgitemshop;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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

public class CreateAccountActivity extends AppCompatActivity {

    private String mUsername;
    private String mPassword;

    private TextView mCreateAccountPrompt;

    private EditText mUsernameField;
    private EditText mPasswordField;

    private ImageButton mBackButton;
    private Button mConfirmButton;

    private UserLogDAO mUserLogDAO;
    private CharacterLogDAO mCharacterLogDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        getDatabase();

        wireUpDisplay();
        setOnClickListeners();
    }

    private void getDatabase() {
        mUserLogDAO = Util.getUserDatabase(this);
        mCharacterLogDAO = Util.getCharacterDatabase(this);
    }

    private void wireUpDisplay() {
        mCreateAccountPrompt = (TextView) findViewById(R.id.createAccountTextView);

        mUsernameField = (EditText) findViewById(R.id.createUsernameEditText);
        mPasswordField = (EditText) findViewById(R.id.createPasswordEditText);

        mBackButton = (ImageButton) findViewById(R.id.createAccountBackBtn);
        mConfirmButton = (Button) findViewById(R.id.createAccountConfirmBtn);
    }

    private void setOnClickListeners() {
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = MainActivity.newIntent(CreateAccountActivity.this);
                startActivity(intent);
            }
        });

        mConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getValuesFromDisplay();

                if(!checkForUserInDatabase()) {
                    if(mUsername.length() > 0 && mPassword.length() > 0) {
                        addUserToDatabase();
                    } else {
                        Toast toast = Toast.makeText(CreateAccountActivity.this, "Please Fill Out All Fields", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
            }
        });
    }

    private void getValuesFromDisplay() {
        mUsername = mUsernameField.getText().toString();
        mPassword = mPasswordField.getText().toString();
    }

    private boolean checkForUserInDatabase() {
        UserLog user = mUserLogDAO.getUserByUsername(mUsername);

        if(user != null) {
            Toast toast = Toast.makeText(CreateAccountActivity.this, "Username Taken", Toast.LENGTH_SHORT);
            toast.show();
            return true;
        }
        return false;
    }

    private void addUserToDatabase() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this, R.style.DialogTheme);

        alertBuilder.setMessage("Create an Account?");

        alertBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        UserLog newUser = new UserLog(mUsername, mPassword, false, 1.0);
                        List<CharacterLog> characters = mCharacterLogDAO.getCharacterLogs();

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

                        Toast toast = Toast.makeText(CreateAccountActivity.this, "Account Created Successfully", Toast.LENGTH_SHORT);
                        toast.show();

                        Intent intent = LoginActivity.newIntent(CreateAccountActivity.this);
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
        Intent intent = new Intent(packagecontext, CreateAccountActivity.class);
        return intent;
    }
}