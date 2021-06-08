package com.example.rpgitemshop.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.rpgitemshop.UserLog;

import java.util.List;

@Dao
public interface UserLogDAO {
    @Insert
    void insert(UserLog... userLogs);

    @Update
    void update(UserLog... userLogs);

    @Delete
    void delete (UserLog userLog);

    @Query("SELECT * FROM " + AppDatabase.USERLOG_TABLE + " ORDER BY mUsername DESC")
    List<UserLog> getUserLogs();

    @Query("SELECT * FROM " + AppDatabase.USERLOG_TABLE + " WHERE mUsername = :username")
    UserLog getUserByUsername(String username);

    @Query("SELECT * FROM " + AppDatabase.USERLOG_TABLE + " WHERE mUserId = :userId")
    UserLog getUserLogsById(int userId);

}
