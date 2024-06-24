package com.example.wifianalyzer;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class Menu extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    FirstFragment firstFragment = new FirstFragment();
    SecondFragment secondFragment = new SecondFragment();
    ThirdFragment thirdFragment = new ThirdFragment();
    com.example.wifianalyzer.FourthFragment fourthFragment = new com.example.wifianalyzer.FourthFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        getSupportFragmentManager().beginTransaction().replace(R.id.container, firstFragment).commit();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int itemId = item.getItemId();
                if(itemId == R.id.first){
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, firstFragment).commit();
                    return true;
                } else if(itemId == R.id.second){
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, secondFragment).commit();
                    return true;
                } else if(itemId == R.id.third){
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, thirdFragment).commit();
                    return true;
                }
                if(itemId == R.id.fourth){
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, fourthFragment).commit();
                    return true;
                }
                else return false;
            }
        });
    }
}