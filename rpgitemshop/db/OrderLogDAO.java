package com.example.rpgitemshop.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.rpgitemshop.OrderLog;

import java.util.List;

@Dao
public interface OrderLogDAO {
    @Insert
    void insert(OrderLog... orderLogs);

    @Update
    void update(OrderLog... orderLogs);

    @Delete
    void delete (OrderLog orderLogs);

    @Query("SELECT * FROM " + AppDatabase.ORDERLOG_TABLE + " ORDER BY mOrderPrice")
    List<OrderLog> getOrders();

    @Query("SELECT * FROM " + AppDatabase.ORDERLOG_TABLE + " WHERE mOrderId = :orderId")
    OrderLog getOrderByOrderId(int orderId);

    @Query("SELECT * FROM " + AppDatabase.ORDERLOG_TABLE + " WHERE mUserId = :userId")
    List<OrderLog> getOrdersByUserId(int userId);

    @Query("SELECT * FROM " + AppDatabase.ORDERLOG_TABLE + " WHERE mOrderName = :orderName")
    List<OrderLog> getOrdersByOrderName(String orderName);

    @Query("SELECT * FROM " + AppDatabase.ORDERLOG_TABLE + " WHERE mOrderName = :orderName")
    OrderLog getIndividualOrderByOrderName(String orderName);
}
