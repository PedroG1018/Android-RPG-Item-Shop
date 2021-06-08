package com.example.rpgitemshop.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.rpgitemshop.ItemLog;

import java.util.List;

@Dao
public interface ItemLogDAO {
    @Insert
    void insert(ItemLog... itemLogs);

    @Update
    void update(ItemLog... itemLogs);

    @Delete
    void delete (ItemLog itemLog);

    @Query("SELECT * FROM " + AppDatabase.ITEMLOG_TABLE + " ORDER BY mItemPrice AND mItemName")
    List<ItemLog> getItemLogs();

    @Query("SELECT * FROM " + AppDatabase.ITEMLOG_TABLE + " WHERE mItemId = :itemId")
    ItemLog getItemByItemId(int itemId);

    @Query("SELECT * FROM " + AppDatabase.ITEMLOG_TABLE + " WHERE mUserId = :userId")
    List<ItemLog> getItemLogsByUserId(int userId);

    @Query("SELECT * FROM " + AppDatabase.ITEMLOG_TABLE + " WHERE mItemName = :itemName")
    ItemLog getItemByItemName(String itemName);

    @Query("SELECT * FROM " + AppDatabase.ITEMLOG_TABLE + " WHERE mItemName LIKE :itemName")
    List<ItemLog> getMultipleItemsByName(String itemName);

    @Query("SELECT DISTINCT mItemName FROM " + AppDatabase.ITEMLOG_TABLE + " ORDER BY mItemName")
    List<String> getDistinctItems();

    @Query("SELECT COUNT(*) FROM " + AppDatabase.ITEMLOG_TABLE + " WHERE mItemName = :itemName")
    int getItemStock(String itemName);
}
