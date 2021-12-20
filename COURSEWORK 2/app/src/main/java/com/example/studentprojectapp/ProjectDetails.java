package com.example.studentprojectapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.Base64.*;

public class ProjectDetails extends AppCompatActivity {
    private StudentProject sp;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sp = getProjectInfo();

        setEditTexts();

        setPhoto();

        Button updateBtn = findViewById(R.id.btn_update);
        Button deleteBtn = findViewById(R.id.btn_delete);

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToUpdateProject();
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDialog();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                Intent resultIntent = new Intent();
                //resultIntent.putExtra("projectID", Integer.toString(sp.getProjectID()));
                resultIntent.putExtra("studentID", Integer.toString(sp.getStudentID()));
                //resultIntent.putExtra("title", sp.getTitle());
                //resultIntent.putExtra("description", sp.getDescription());
                //resultIntent.putExtra("year", Integer.toString(sp.getYear()));
                //resultIntent.putExtra("first_name", sp.getFirst_name());
                //resultIntent.putExtra("second_name", sp.getSecond_name());
                setResult(Activity.RESULT_OK, resultIntent);
                this.finish();

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setPhoto() {
        ImageView imgView = findViewById(R.id.img_projphoto);
        String base64 = sp.getPhoto();

        if (base64 != "null") {
            byte[] bytes = Base64.getDecoder().decode(base64);
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            imgView.setImageBitmap(bitmap);
        }
    }

    private void setEditTexts() {
        TextView idTxt = findViewById(R.id.lbl_id_value);
        TextView titleTxt = findViewById(R.id.lbl_title_value);
        TextView descriptionTxt = findViewById(R.id.lbl_description_value);
        TextView yearTxt = findViewById(R.id.lbl_year_value);
        TextView nameTxt = findViewById(R.id.lbl_name_value);

        String fullName = sp.getFirst_name() + " " + sp.getSecond_name();
        String strYear = Integer.toString(sp.getYear());
        String strID = Integer.toString(sp.getStudentID());

        idTxt.setText(strID);
        titleTxt.setText(sp.getTitle());
        descriptionTxt.setText(sp.getDescription());
        yearTxt.setText(strYear);
        nameTxt.setText(fullName);

    }

    private StudentProject getProjectInfo() {
        Intent intent = getIntent();

        int projectID = Integer.parseInt(intent.getStringExtra("projectID"));
        int studentID = Integer.parseInt(intent.getStringExtra("studentID")); // this is iffy
        String title = intent.getStringExtra("title");
        String description = intent.getStringExtra("description");
        int year = Integer.parseInt(intent.getStringExtra("year")); //  this is iffy
        String first_name = intent.getStringExtra("first_name");
        String second_name = intent.getStringExtra("second_name");
        String photo = intent.getStringExtra("photo");

        return new StudentProject(projectID, studentID, title, description, year, first_name, second_name, photo);
    }

    private void goToUpdateProject() {
        Context context = getApplicationContext();
        Intent intent = new Intent(context, UpdateProject.class);

        intent.putExtra("projectID", Integer.toString(sp.getProjectID()));
        intent.putExtra("studentID", Integer.toString(sp.getStudentID()));
        intent.putExtra("title", sp.getTitle());
        intent.putExtra("description", sp.getDescription());
        intent.putExtra("year", Integer.toString(sp.getYear()));
        intent.putExtra("first_name", sp.getFirst_name());
        intent.putExtra("second_name", sp.getSecond_name());
        intent.putExtra("photo", sp.getPhoto());

        startActivity(intent);
    }

    private void openViewProjects(String studentID) {
        Context context = getApplicationContext();
        Intent intent = new Intent(context, ViewProjects.class);
        intent.putExtra("studentID", studentID);
        startActivity(intent);
    }

    private void createDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage("Are you sure you want to delete " + sp.getTitle() + "?");
        dialog.setCancelable(false);

        dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                deleteProject();
        }
        });

        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        dialog.create().show();
    }

    private void deleteProject() {
        RequestQueue queue = Volley.newRequestQueue(ProjectDetails.this);

        int projectID = sp.getProjectID();
        String apiURL = "http://web.socem.plymouth.ac.uk/COMP2000/api/students/" + projectID;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE, apiURL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                    }
                }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ProjectDetails.this, "Project deleted.", Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(request);

        openViewProjects(Integer.toString(sp.getStudentID()));

    }
}