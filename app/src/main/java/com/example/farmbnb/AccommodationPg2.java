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

public class AccommodationPg2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accommodation_pg2);

        BottomNavigationView nav = findViewById(R.id.nav_home);
        nav.setSelectedItemId(R.id.invisible);

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

                    case (R.id.view_bookings_page):
                        intent = new Intent (getApplicationContext(), Bookings.class);
                        startActivity(intent);
                        return true;

                }
                return false;
            }
        });

        Button nextButton = findViewById(R.id.btn_next_pg2);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPg3();
            }
        });

        Button prevButton = findViewById(R.id.btn_prev_pg2);
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPg1();
            }
        });

        Button moreDetailsBtn = findViewById(R.id.btn_moredetails_barn);
        moreDetailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBarn();
            }
        });
    }

    public void openPg3() {
        Context context = getApplicationContext();
        Intent intent = new Intent(context, AccommodationPg3.class);
        startActivity(intent);
    }

    public void openPg1() {
        Context context = getApplicationContext();
        Intent intent = new Intent(context, Accommodation.class);
        startActivity(intent);
    }


    public void openBarn() {
        Context context = getApplicationContext();
        Intent intent = new Intent(context, Barn.class);
        startActivity(intent);
    }
}