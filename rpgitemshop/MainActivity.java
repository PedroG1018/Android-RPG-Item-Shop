/**
 * Author: Pedro Gutierrez Jr.
 * Last Updated: May 14, 2021
 * File Name: MainActivity.java
 * Description: The main page you see when starting the app, assuming nobody is logged in
 */

package com.example.rpgitemshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.rpgitemshop.db.CharacterLogDAO;
import com.example.rpgitemshop.db.ItemLogDAO;
import com.example.rpgitemshop.db.UserLogDAO;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private UserLogDAO mUserLogDAO;
    private ItemLogDAO mItemLogDAO;
    private CharacterLogDAO mCharacterLogDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getDatabase();
        setCharacters();
        setPredefinedUsers();
        stockUpStore();
        wireUpDisplay();
    }

    private void getDatabase() {
        mUserLogDAO = Util.getUserDatabase(this);
        mItemLogDAO = Util.getItemDatabase( this);
        mCharacterLogDAO = Util.getCharacterDatabase(this);
    }

    private void setCharacters() {
        List<CharacterLog> characters = mCharacterLogDAO.getCharacterLogs();

        if(characters.size() <= 0) {
            CharacterLog cecil = new CharacterLog("Cecil", false);
            CharacterLog bartz = new CharacterLog("Bartz", false);
            CharacterLog terra = new CharacterLog("Terra", false);
            CharacterLog cloud = new CharacterLog("Cloud", false);
            CharacterLog aerith = new CharacterLog("Aerith", false);
            CharacterLog squall = new CharacterLog("Squall", false);
            CharacterLog rinoa = new CharacterLog("Rinoa", false);
            CharacterLog zidane = new CharacterLog("Zidane", false);
            CharacterLog garnet = new CharacterLog("Garnet", false);
            CharacterLog tidus = new CharacterLog("Tidus", false);
            CharacterLog yuna = new CharacterLog("Yuna", false);
            CharacterLog vaan = new CharacterLog("Vaan", false);
            CharacterLog lightning = new CharacterLog("Lightning", false);
            CharacterLog noctis = new CharacterLog("Noctis", false);

            CharacterLog zemus = new CharacterLog("Zemus", true);
            CharacterLog gilgamesh = new CharacterLog("Gilgamesh", true);
            CharacterLog exdeath = new CharacterLog("Exdeath", true);
            CharacterLog kefka = new CharacterLog("Kefka", true);
            CharacterLog sephiroth = new CharacterLog("Sephiroth", true);
            CharacterLog kuja = new CharacterLog("Kuja", true);
            CharacterLog necron = new CharacterLog("Necron", true);
            CharacterLog seymour = new CharacterLog("Seymour", true);
            CharacterLog vayne = new CharacterLog("Vayne", true);
            CharacterLog galenth = new CharacterLog("Galength", true);
            CharacterLog ardyn = new CharacterLog("Ardyn", true);

            mCharacterLogDAO.insert(cecil, bartz, terra, cloud, aerith, squall, rinoa, zidane, garnet, tidus, yuna, vaan, lightning, noctis, zemus, gilgamesh, exdeath, kefka, sephiroth, kuja, necron, seymour, vayne, galenth, ardyn);
        }
    }

    private void setPredefinedUsers() {
        List<UserLog> users = mUserLogDAO.getUserLogs();

        if(users.size() <= 0) {
            UserLog admin2 = new UserLog("testuser1", "testuser1", false, 1.0);
            UserLog testuser1 = new UserLog("admin2", "admin2", true, 1.0);
            UserLog cloud = new UserLog("cloud", "cloud", false, 0.9);
            UserLog sephiroth = new UserLog("sephiroth", "sephiroth", false, 1.1);
            mUserLogDAO.insert(admin2, testuser1, cloud, sephiroth);
        }
    }

    private void stockUpStore() {
        List<ItemLog> items = mItemLogDAO.getItemLogs();

        if(items.size() <= 0) {
            ItemLog potion = new ItemLog(500, "Potion", "Restores 50 HP", 'U');
            ItemLog ether = new ItemLog(1000, "Ether", "Restores 50 MP", 'U');
            ItemLog hi_potion = new ItemLog(750, "Hi-Potion", "Restores 150 HP", 'U');
            ItemLog antidote = new ItemLog(500, "Antidote", "Cures Poison", 'U');
            int REGULAR_ITEM = 10;
            for(int i = 0; i < REGULAR_ITEM; i++) {
                mItemLogDAO.insert(potion, ether, antidote, hi_potion);
            }

            ItemLog elixir = new ItemLog(2000, "Elixir", "Fully Restores HP and MP", 'U');
            ItemLog phoenix_down = new ItemLog(2000, "Phoenix Down", "Revives One Fallen Ally", 'U');
            ItemLog mega_potion = new ItemLog(2000, "Mega Potion", "Restores 500 HP", 'U');
            int SPECIAL_ITEM = 5;
            for(int i = 0; i < SPECIAL_ITEM; i++) {
                mItemLogDAO.insert(elixir, phoenix_down, mega_potion);
            }

            ItemLog mythril_gloves = new ItemLog(2500, "Mythril Gloves", "Lightweight glove mode of mythril. Casts Protect when the wearer is critically wounded.", 'U');
            ItemLog mythril_shield = new ItemLog(2500, "Mythril Shield", "A shield constructed of the featherlight metal known as mythril. It is surprisingly light and easy to wield.", 'U');
            ItemLog mythril_helm = new ItemLog(2500, "Mythril Helm", "A helm made from the valuable metal known as mythril. It is very sturdy and surprisingly lightweight.", 'U');
            ItemLog mythril_armor = new ItemLog(3000, "Mythril Armor", "Extraordinarily beautiful full-body armor that uses layer upon layer of plates forged from mythril.", 'U');
            ItemLog mega_elixir = new ItemLog(2500, "Megaelixir", "Fully Restores HP and MP To All Party Members", 'U');
            int RARE = 3;
            for(int i = 0; i < RARE; i++) {
                mItemLogDAO.insert(mythril_gloves, mythril_helm, mythril_shield, mythril_armor, mega_elixir);
            }

            ItemLog buster_sword = new ItemLog(10000, "Buster Sword", "A large broadsword that has inherited the hopes of those who fight.", 'U');
            ItemLog masamune = new ItemLog(10000, "Masamune", "Sephiroth's personal weapon, it is said he is the only one strong enough to wield it.", 'U');
            ItemLog brotherhood = new ItemLog(10000, "Brotherhood", "To bestow this blade to another is to anoint them your best friend forever.", 'U');
            ItemLog save_the_queen = new ItemLog(7500, "Save The Queen", "Long sword used by holy knights.", 'U');
            ItemLog magitek_armor = new ItemLog(8000, "Magitek Armor", "The primary strength of the Gestahlian Empire that has allowed them to begin their conquest of the world.", 'U');
            ItemLog ultima_weapon = new ItemLog(50000, "Ultima Weapon", "The ultimate weapon a warrior could ask for. It has taken many incarnations.", 'U');
            mItemLogDAO.insert(buster_sword, masamune, brotherhood, save_the_queen, magitek_armor, ultima_weapon);
        }
    }

    private void wireUpDisplay() {
        TextView mAppName = (TextView) findViewById(R.id.appNameTextView);

        Button mLoginButton = (Button) findViewById(R.id.mainLoginBtn);
        Button mCreateAccountButton = (Button) findViewById(R.id.createAccountBtn);

        SharedPreferences sharedPref = getSharedPreferences("login", Context.MODE_PRIVATE);

        if(sharedPref.getBoolean("isLogin", true)) {
            Intent intent = LandingPage.loginIntent(MainActivity.this);
            startActivity(intent);
        }

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = LoginActivity.newIntent(MainActivity.this);
                startActivity(intent);
            }
        });

        mCreateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = CreateAccountActivity.newIntent(MainActivity.this);
                startActivity(intent);
            }
        });
    }

    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, MainActivity.class);
        return intent;
    }
}