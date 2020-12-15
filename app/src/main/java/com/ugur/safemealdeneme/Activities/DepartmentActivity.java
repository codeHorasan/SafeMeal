package com.ugur.safemealdeneme.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ugur.safemealdeneme.Classes.Company;
import com.ugur.safemealdeneme.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DepartmentActivity extends AppCompatActivity {
    Spinner menuSelectionSpinner;
    Button button;
    ArrayList<String> menuNames;
    ArrayList<String> menuIDs;
    String departmentName, departmentID;

    ArrayAdapter<String> spinnerAdapter;
    private String menuUUID;
    private String menuName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_department);

        menuSelectionSpinner = findViewById(R.id.spinner_menu_selection);
        button = findViewById(R.id.button_set_menu);
        menuNames = new ArrayList<>();
        menuIDs = new ArrayList<>();

        Intent intent = getIntent();
        departmentName = intent.getStringExtra("name");
        departmentID = intent.getStringExtra("id");

        controlIfFirstTime();

        spinnerAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,menuNames);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    public void setMenuForDepartment(View view) {
        menuUUID = menuIDs.get(menuSelectionSpinner.getSelectedItemPosition());
        menuName = menuNames.get(menuSelectionSpinner.getSelectedItemPosition());
        System.out.println("Menu UUID: " + menuUUID);
        System.out.println("Menu Name: " + menuName);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Companies").child(Company.getInstance().getUUID()).child("Departments")
                .child(departmentID).child("menuID").setValue(menuUUID);
        reference.child("Companies").child(Company.getInstance().getUUID()).child("Departments")
                .child(departmentID).child("menuName").setValue(menuName);

        menuSelectionSpinner.setVisibility(View.GONE);
        button.setVisibility(View.GONE);
        getSupportActionBar().setTitle("Department " + departmentName);
        getSupportActionBar().show();
    }

    public void controlIfFirstTime() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Companies").child(Company.getInstance().getUUID()).child("Departments")
                .child(departmentID);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("menuID").getValue(String.class) == null) {
                    getSupportActionBar().hide();
                    setAdapter();
                } else {
                    menuSelectionSpinner.setVisibility(View.GONE);
                    button.setVisibility(View.GONE);
                    getSupportActionBar().setTitle("Department " + departmentName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

    public void setAdapter() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Companies").child(Company.getInstance().getUUID()).child("Menus");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    HashMap<String,String> map = (HashMap<String, String>) ds.getValue();
                    menuIDs.add(ds.getKey());
                    menuNames.add(map.get("Name"));
                }

                menuSelectionSpinner.setAdapter(spinnerAdapter);
                menuSelectionSpinner.setVisibility(View.VISIBLE);
                button.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }
}