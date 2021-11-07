package com.example.farmbnb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ViewPastBooking extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_past_booking);

        BottomNavigationView nav = findViewById(R.id.nav_home);
        nav.setSelectedItemId(R.id.view_bookings_page);

        nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;
                switch (item.getItemId()) {
                    case (R.id.home_page):
                        intent = new Intent (getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        return true;

                    case (R.id.create_booking_page):
                        intent = new Intent (getApplicationContext(), CreateBooking.class);
                        startActivity(intent);
                        return true;
                }
                return false;
            }
        });

        Button backBtn = findViewById(R.id.btn_back_pastbooking);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
    }

    public void back() {
        Context context = getApplicationContext();
        Intent intent = new Intent(context, Bookings.class);
        startActivity(intent);
    }
}