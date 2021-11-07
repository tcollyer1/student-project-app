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

public class Bookings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookings);

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

        Button detailsBtnCurrentEx = findViewById(R.id.btn_details_currentbooking);
        detailsBtnCurrentEx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewCurrentBookingExample();
            }
        });

        Button detailsBtnPastEx = findViewById(R.id.btn_details_pastbooking);
        detailsBtnPastEx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPastBookingExample();
            }
        });
    }

    public void viewCurrentBookingExample() {
        Context context = getApplicationContext();
        Intent intent = new Intent(context, ViewCurrentBooking.class);
        startActivity(intent);
    }

    public void viewPastBookingExample() {
        Context context = getApplicationContext();
        Intent intent = new Intent(context, ViewPastBooking.class);
        startActivity(intent);
    }
}