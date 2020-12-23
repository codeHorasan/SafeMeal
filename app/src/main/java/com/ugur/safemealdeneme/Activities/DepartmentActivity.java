package com.ugur.safemealdeneme.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ugur.safemealdeneme.Classes.Company;
import com.ugur.safemealdeneme.DepartmentConstantsClass;
import com.ugur.safemealdeneme.R;

import java.util.ArrayList;
import java.util.HashMap;

public class DepartmentActivity extends AppCompatActivity {
    Spinner menuSelectionSpinner;
    TextInputLayout tableAmountText;
    Button button;
    ArrayList<String> menuNames;
    ArrayList<String> menuIDs;
    String departmentName, departmentID;

    ArrayAdapter<String> spinnerAdapter;
    private String menuUUID;
    private String menuName;
    private String tableAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_department);

        menuSelectionSpinner = findViewById(R.id.spinner_menu_selection);
        tableAmountText = findViewById(R.id.menu_table_amount);
        button = findViewById(R.id.button_set_menu);
        menuNames = new ArrayList<>();
        menuIDs = new ArrayList<>();

        Intent intent = getIntent();
        departmentName = intent.getStringExtra("name");
        departmentID = intent.getStringExtra("id");

        if (departmentID != null && departmentName != null) {
            DepartmentConstantsClass.CURRENT_DEPARTMENT_NAME = departmentName;
            DepartmentConstantsClass.CURRENT_DEPARTMENT_UUID = departmentID;
        }

        if (departmentName == null || departmentID == null) {
            departmentName = DepartmentConstantsClass.CURRENT_DEPARTMENT_NAME;
            departmentID = DepartmentConstantsClass.CURRENT_DEPARTMENT_UUID;
        }

        controlIfFirstTime();

        spinnerAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,menuNames);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.department_menu_qr:
                //openDialog();
                Intent intent = new Intent(getApplicationContext(), QRCodeViewActivity.class);
                intent.putExtra("departmentID", departmentID);
                intent.putExtra("menuUUID", menuUUID);
                intent.putExtra("amount", tableAmount);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.department_qr_menu,menu);
        return true;
    }

    public void openDialog() {
        /*QRCodeDialog qrCodeDialog = new QRCodeDialog();
        qrCodeDialog.setDepartmentID(departmentID);
        qrCodeDialog.setMenuID(menuUUID);
        qrCodeDialog.show(getSupportFragmentManager(), "QR Code");*/
    }

    public void setMenuForDepartment(View view) {
        menuUUID = menuIDs.get(menuSelectionSpinner.getSelectedItemPosition());
        menuName = menuNames.get(menuSelectionSpinner.getSelectedItemPosition());
        tableAmount = tableAmountText.getEditText().getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Companies").child(Company.getInstance().getUUID()).child("Departments")
                .child(departmentID).child("menuID").setValue(menuUUID);
        reference.child("Companies").child(Company.getInstance().getUUID()).child("Departments")
                .child(departmentID).child("menuName").setValue(menuName);
        reference.child("Companies").child(Company.getInstance().getUUID()).child("Departments")
                .child(departmentID).child("tableAmount").setValue(tableAmount);

        menuSelectionSpinner.setVisibility(View.GONE);
        tableAmountText.setVisibility(View.GONE);
        button.setVisibility(View.GONE);
        getSupportActionBar().setTitle("Department " + departmentName);
        getSupportActionBar().show();
    }

    public void controlIfFirstTime() {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
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
                    tableAmountText.setVisibility(View.GONE);
                    button.setVisibility(View.GONE);
                    getSupportActionBar().setTitle("Department " + departmentName);
                    DatabaseReference getMenuInfoRef = database.getReference("Companies").child(Company.getInstance().getUUID())
                            .child("Departments").child(departmentID);
                    getMenuInfoRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            menuUUID = snapshot.child("menuID").getValue(String.class);
                            menuName = snapshot.child("menuName").getValue(String.class);
                            tableAmount = snapshot.child("tableAmount").getValue(String.class);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
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
                tableAmountText.setVisibility(View.VISIBLE);
                button.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }
}