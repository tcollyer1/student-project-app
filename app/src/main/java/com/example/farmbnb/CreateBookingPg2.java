package com.example.farmbnb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

public class CreateBookingPg2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_booking_pg2);

        BottomNavigationView nav = findViewById(R.id.nav_home);
        nav.setSelectedItemId(R.id.create_booking_page);

        nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;
                switch (item.getItemId()) {
                    case (R.id.home_page):
                        intent = new Intent (getApplicationContext(), MainActivity.class);
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

        Button nextPageBtn = findViewById(R.id.btn_submitbooking);
        nextPageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (allDetailsPresent()) {
                    displaySuccessSnackbar(nextPageBtn);
                }
                else {
                    displayFailureSnackbar(nextPageBtn);
                }
            }
        });
    }

    public void displaySuccessSnackbar(View v) {
        Snackbar.make(v, "Booking created!", Snackbar.LENGTH_SHORT).show();
    }

    public void displayFailureSnackbar(View v) {
        Snackbar.make(v, "Error: not all fields filled", Snackbar.LENGTH_SHORT).show();
    }

    public boolean allDetailsPresent() {
        EditText customerName = findViewById(R.id.input_customername);
        EditText phoneNo = findViewById(R.id.input_phoneno);
        EditText address = findViewById(R.id.input_address);

        String customerNameTxt = customerName.getText().toString();
        String phoneNoTxt = phoneNo.getText().toString();
        String addressTxt = address.getText().toString();

        if ("".equals(phoneNoTxt) || "".equals(addressTxt) || "".equals(customerNameTxt)) {
            return false;
        }
        else return true;
    }
}