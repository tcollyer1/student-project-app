package com.example.farmbnb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.text.DateFormat;
import java.util.Date;
import java.text.SimpleDateFormat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

public class CreateBooking extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_booking);

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

        Button nextPageBtn = findViewById(R.id.btn_booking_nextpage);
        nextPageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText accommName = findViewById(R.id.input_accommname);
                EditText arrivalDate = findViewById(R.id.input_arrivaldate);
                EditText departureDate = findViewById(R.id.input_departuredate);

                AccommodationInfo accommInfo = new AccommodationInfo(accommName.getText().toString(), arrivalDate.getText().toString(), departureDate.getText().toString());

                boolean detailsPresent = allDetailsPresent(accommInfo.getAccommName(), accommInfo.getArrivalDate(), accommInfo.getDepartureDate());
                boolean validDates = datesValid(accommInfo.getArrivalDate(), accommInfo.getDepartureDate());

                if (detailsPresent && validDates) {
                    moveToNextPage();
                }
                else if (!detailsPresent) {
                    displayFieldsMissingSnackbar(nextPageBtn);
                }
                else {
                    displayInvalidDatesSnackbar(nextPageBtn);
                }
            }
        });
    }

    public void moveToNextPage() {
        Intent intent = new Intent(getApplicationContext(), CreateBookingPg2.class);
        startActivity(intent);
    }

    public void displayFieldsMissingSnackbar(View v) {
        Snackbar.make(v, "Please fill in all fields before proceeding.", Snackbar.LENGTH_SHORT).show();
    }

    public void displayInvalidDatesSnackbar(View v) {
        Snackbar.make(v, "One or more dates invalid - use format dd/mm/yy and make sure departure is after arrival", Snackbar.LENGTH_SHORT).show();
    }

    public boolean datesValid(String arrDate, String depDate) {

        DateFormat fm = new SimpleDateFormat("dd/MM/yy");
        Date checkArrDate;
        Date checkDepDate;

        try {
            checkArrDate = fm.parse(arrDate);
            checkDepDate = fm.parse(depDate);
        }
        catch (Exception exception) {
            return false;
        }

        return checkArrDate.before(checkDepDate);
    }

    public boolean allDetailsPresent(String accommNameTxt, String arrivalDateTxt, String departureDateTxt) {

        if ("".equals(accommNameTxt) || "".equals(arrivalDateTxt) || "".equals(departureDateTxt)) {
            return false;
        }
        else return true;
    }
}