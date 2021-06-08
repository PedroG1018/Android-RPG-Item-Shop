/**
 * Author: Pedro Gutierrez Jr.
 * Last Updated: May 14, 2021
 * File Name: CharacterLog.java
 * Description: Creates a table with names of Final Fantasy characters
 *      Columns: mCharacterId, mCharacterName, mIsVillain
 */

package com.example.rpgitemshop;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.rpgitemshop.db.AppDatabase;

@Entity(tableName = AppDatabase.CHARACTER_TABLE)
public class CharacterLog {

    @PrimaryKey(autoGenerate = true)
    private int mCharacterId;

    private String mCharacterName;

    private boolean mIsVillain;

    public CharacterLog(String characterName, boolean isVillain) {
        this.mCharacterName = characterName;
        this.mIsVillain = isVillain;
    }

    public void setCharacterId (int characterId) {
        this.mCharacterId = characterId;
    }

    public int getCharacterId () {
        return mCharacterId;
    }

    public String getCharacterName() {
        return mCharacterName;
    }

    public void setCharacterName(String characterName) {
        this.mCharacterName = characterName;
    }

    public boolean getIsVillain() {
        return mIsVillain;
    }

    public void setIsVillain(boolean isVillain) {
        this.mIsVillain = isVillain;
    }
}
