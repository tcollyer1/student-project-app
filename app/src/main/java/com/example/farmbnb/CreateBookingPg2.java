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
    // Variables to store booking data from the previous page
    private String accommName;
    private String arrDate;
    private String depDate;

    private int customerID = 0; // Would be set according to whatever customer ID the user has (based on their login)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_booking_pg2);

        BottomNavigationView nav = findViewById(R.id.nav_home);
        nav.setSelectedItemId(R.id.create_booking_page);

        // Get booking info from previous page
        getBookingInfo();

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
                EditText customerName = findViewById(R.id.input_customername);
                EditText phoneNo = findViewById(R.id.input_phoneno);
                EditText address = findViewById(R.id.input_address);

                // Store customer details for later external storage
                Customer customer = new Customer(customerID, customerName.getText().toString(), address.getText().toString(), phoneNo.getText().toString());

                boolean detailsPresent = allDetailsPresent(customer.getName(), customer.getPhone(), customer.getAddress());

                if (detailsPresent) {
                    // Store full booking details in FinalBooking class object for later external storage
                    FinalBooking customerBooking = new FinalBooking(customerID, accommName, arrDate, depDate);

                    displaySuccessSnackbar(nextPageBtn);
                }
                else {
                    displayFailureSnackbar(nextPageBtn);
                }
            }
        });
    }

    public void displaySuccessSnackbar(View v) {
        String msg = "Booking created for " + accommName + " on " + arrDate + "!";
        Snackbar.make(v, msg, Snackbar.LENGTH_SHORT).show();
    }

    public void displayFailureSnackbar(View v) {
        Snackbar.make(v, "Error: not all fields filled", Snackbar.LENGTH_SHORT).show();
    }

    public boolean allDetailsPresent(String customerNameTxt, String phoneNoTxt, String addressTxt) {

        if ("".equals(phoneNoTxt) || "".equals(addressTxt) || "".equals(customerNameTxt)) {
            return false;
        }
        else return true;
    }

    public void getBookingInfo() {
        Intent intent = getIntent();
        accommName = intent.getStringExtra("AccommName");
        arrDate = intent.getStringExtra("ArrivalDate");
        depDate = intent.getStringExtra("DepartureDate");
    }
}