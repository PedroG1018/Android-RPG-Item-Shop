/**
 * Author: Pedro Gutierrez Jr.
 * Last Updated: May 14, 2021
 * File Name: Util.java
 * Description: Class contains various static fields and methods that can be accessed by activities if needed
 */

package com.example.rpgitemshop;

import android.content.Context;

import androidx.room.Room;

import com.example.rpgitemshop.db.AppDatabase;
import com.example.rpgitemshop.db.CharacterLogDAO;
import com.example.rpgitemshop.db.ItemLogDAO;
import com.example.rpgitemshop.db.OrderLogDAO;
import com.example.rpgitemshop.db.UserLogDAO;

public class Util {
    public static final String USER_ID_KEY = "com.example.rpgitemshop.userIdKey";
    public static final String PREFERENCES_KEY = "come.example.rpgitemshop.preferencesKey";


    public static UserLogDAO getUserDatabase(Context context) {
        UserLogDAO userLogDAO = Room.databaseBuilder(context, AppDatabase.class, AppDatabase.DB_NAME)
                .allowMainThreadQueries()
                .build()
                .getUserLogDAO();

        return userLogDAO;
    }

    public static ItemLogDAO getItemDatabase(Context context) {
        ItemLogDAO itemLogDAO = Room.databaseBuilder(context, AppDatabase.class, AppDatabase.DB_NAME)
                .allowMainThreadQueries()
                .build()
                .getItemLogDAO();

        return itemLogDAO;
    }

    public static OrderLogDAO getOrderDatabase(Context context) {
        OrderLogDAO orderLogDAO = Room.databaseBuilder(context, AppDatabase.class, AppDatabase.DB_NAME)
                .allowMainThreadQueries()
                .build()
                .getOrderLogDAO();

        return orderLogDAO;
    }

    public static CharacterLogDAO getCharacterDatabase(Context context) {
        CharacterLogDAO characterLogDAO = Room.databaseBuilder(context, AppDatabase.class, AppDatabase.DB_NAME)
                .allowMainThreadQueries()
                .build()
                .getCharacterLogDAO();

        return characterLogDAO;
    }
}
