/**
 * Author: Pedro Gutierrez Jr.
 * Last Updated: May 14, 2021
 * File Name: LoginActivity.java
 * Description: Allows user to log into their account
 */

package com.example.rpgitemshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.rpgitemshop.db.UserLogDAO;

public class LoginActivity extends AppCompatActivity {

    private int mCounter = 3;

    private EditText mUsernameField;
    private EditText mPasswordField;

    private UserLogDAO mUserLogDAO;

    private String mUsername;
    private String mPassword;

    private UserLog mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mUserLogDAO = Util.getUserDatabase(this);

        wireUpDisplay();
    }

    private void wireUpDisplay() {
        mUsernameField = (EditText) findViewById(R.id.usernameEditText);
        mPasswordField = (EditText) findViewById(R.id.passwordEditText);

        Button mLoginButton = (Button) findViewById(R.id.loginBtn);
        ImageButton mBackButton = (ImageButton) findViewById(R.id.loginBackBtn);

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getValuesFromDisplay();

                if(checkForUserInDatabase()) {
                    if(!validatePassword()) {
                        Toast toast = Toast.makeText(LoginActivity.this, "Invalid Password", Toast.LENGTH_SHORT);
                        toast.show();
                        mCounter--;
                    } else {
                        Toast toast = Toast.makeText(LoginActivity.this, "Successfully Logged In", Toast.LENGTH_SHORT);
                        toast.show();

                        SharedPreferences sharedPref = getSharedPreferences("login", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putBoolean("isLogin", true);
                        editor.apply();

                        Intent intent = LandingPage.newIntent(getApplicationContext(), mUser.getUserId());
                        startActivity(intent);
                    }
                }

                checkAttempts(mCounter);
            }
        });

        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = MainActivity.newIntent(LoginActivity.this);
                startActivity(intent);
            }
        });
    }

    private void getValuesFromDisplay() {
        mUsername = mUsernameField.getText().toString();
        mPassword = mPasswordField.getText().toString();
    }

    private boolean checkForUserInDatabase() {
        mUser = mUserLogDAO.getUserByUsername(mUsername);
        if(mUser == null) {
            Toast toast = Toast.makeText(LoginActivity.this, "Invalid Username", Toast.LENGTH_SHORT);
            toast.show();
            mCounter--;
            return false;
        }
        return true;
    }

    private boolean validatePassword() {
        return mUser.getPassword().equals(mPassword);
    }

    private void checkAttempts(int counter) {
        if(counter == 0) {
            Toast t = Toast.makeText(getApplicationContext(), "Too many bad attempts", Toast.LENGTH_SHORT);
            t.show();
            Intent intent = MainActivity.newIntent(LoginActivity.this);
            startActivity(intent);
        }
    }

    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, LoginActivity.class);
        return intent;
    }
}