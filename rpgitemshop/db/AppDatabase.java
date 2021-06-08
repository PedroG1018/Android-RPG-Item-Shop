package com.example.rpgitemshop.db;


import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.rpgitemshop.CharacterLog;
import com.example.rpgitemshop.ItemLog;
import com.example.rpgitemshop.OrderLog;
import com.example.rpgitemshop.UserLog;

@Database(entities = {UserLog.class, ItemLog.class, OrderLog.class, CharacterLog.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public static final String DB_NAME = "RPG_SHOP_DATABASE";
    public static final String USERLOG_TABLE = "USERLOG_TABLE";
    public static final String ITEMLOG_TABLE = "ITEMLOG_TABLE";
    public static final String ORDERLOG_TABLE = "ORDERLOG_TABLE";
    public static final String CHARACTER_TABLE = "CHARACTER_TABLE";

    public abstract UserLogDAO getUserLogDAO();
    public abstract ItemLogDAO getItemLogDAO();
    public abstract OrderLogDAO getOrderLogDAO();
    public abstract CharacterLogDAO getCharacterLogDAO();

}
