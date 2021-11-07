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

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView nav = findViewById(R.id.nav_home);
        nav.setSelectedItemId(R.id.home_page);

        nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;
                switch (item.getItemId()) {
                    case (R.id.create_booking_page):
                        intent = new Intent (getApplicationContext(), CreateBooking.class);
                        startActivity(intent);
                        return true;

                    case (R.id.view_bookings_page):
                        intent = new Intent (getApplicationContext(), Bookings.class);
                        startActivity(intent);
                        return true;

                }
                return false;
            }
        });



        Button createBookingBtn = (Button) findViewById(R.id.btn_createbooking);
        createBookingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCreateBooking();
            }
        });

        Button createViewCurrentBookingsBtn = (Button) findViewById(R.id.btn_viewcurrentbookings);
        createViewCurrentBookingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openViewCurrentBookings();
            }
        });

        Button createViewPastBookingsBtn = (Button) findViewById(R.id.btn_accommodation);
        createViewPastBookingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAccommodation();
            }
        });
    }

    // Methods to open the different activities

    public void openCreateBooking() {
        Context context = getApplicationContext();
        Intent intent = new Intent(context, CreateBooking.class);
        startActivity(intent);
    }

    public void openViewCurrentBookings() {
        Context context = getApplicationContext();
        Intent intent = new Intent(context, Bookings.class);
        startActivity(intent);
    }

    public void openAccommodation() {
        Context context = getApplicationContext();
        Intent intent = new Intent(context, Accommodation.class);
        startActivity(intent);
    }
}