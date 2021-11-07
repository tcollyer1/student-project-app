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
    public String SHARED_PREFS = "sharedPrefs";
    public String ACCOMM_NAME = "text";
    public String ARRIVAL_DATE = "text";
    public String DEP_DATE = "text";

    private String accommNameTxt;
    private String arrivalDateTxt;
    private String departureDateTxt;

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
                if (allDetailsPresent() && datesValid()) {
                    moveToNextPage();
                }
                else if (!allDetailsPresent()) {
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

    public boolean datesValid() {
        EditText arrivalDate = findViewById(R.id.input_arrivaldate);
        EditText departureDate = findViewById(R.id.input_departuredate);

        String tempArrivalDateTxt = arrivalDate.getText().toString();
        String tempDepDateTxt = departureDate.getText().toString();

        DateFormat fm = new SimpleDateFormat("dd/MM/yy");
        Date checkArrDate;
        Date checkDepDate;

        try {
            checkArrDate = fm.parse(tempArrivalDateTxt);
            checkDepDate = fm.parse(tempDepDateTxt);
        }
        catch (Exception exception) {
            return false;
        }

        return checkArrDate.before(checkDepDate);
    }

    public boolean allDetailsPresent() {
        EditText accommName = findViewById(R.id.input_accommname);
        EditText arrivalDate = findViewById(R.id.input_arrivaldate);
        EditText departureDate = findViewById(R.id.input_departuredate);

        accommNameTxt = accommName.getText().toString();
        arrivalDateTxt = arrivalDate.getText().toString();
        departureDateTxt = departureDate.getText().toString();

        if ("".equals(accommNameTxt) || "".equals(arrivalDateTxt) || "".equals(departureDateTxt)) {
            return false;
        }
        else return true;
    }
}