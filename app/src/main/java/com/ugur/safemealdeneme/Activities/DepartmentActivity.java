package com.ugur.safemealdeneme.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.ugur.safemealdeneme.Adapters.PanelProductAdapter;
import com.ugur.safemealdeneme.Classes.Company;
import com.ugur.safemealdeneme.DepartmentConstantsClass;
import com.ugur.safemealdeneme.Dialogs.OrderTableDeletionDialog;
import com.ugur.safemealdeneme.Models.PanelProductModel;
import com.ugur.safemealdeneme.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DepartmentActivity extends AppCompatActivity {
    Spinner menuSelectionSpinner;
    TextInputLayout tableAmountText;
    Button button;
    FloatingActionButton floatingActionButton;
    ArrayList<String> menuNames;
    ArrayList<String> menuIDs;
    String departmentName;
    public static String departmentID;

    ArrayAdapter<String> spinnerAdapter;
    public static String menuUUID;
    private String menuName;
    private String tableAmount;

    RecyclerView recyclerView;
    PanelProductAdapter adapter;
    ArrayList<PanelProductModel> modelList;

    Toolbar toolbar;
    ImageView toolbarImageView;
    TextView toolbarText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_department);

        menuSelectionSpinner = findViewById(R.id.spinner_menu_selection);
        tableAmountText = findViewById(R.id.menu_table_amount);
        button = findViewById(R.id.button_set_menu);
        floatingActionButton = findViewById(R.id.floating_button_delete_table);
        recyclerView = findViewById(R.id.recycler_view_panel);
        toolbar = findViewById(R.id.toolbar_company_menu);
        toolbarImageView = findViewById(R.id.toolbar_compmenu_image);
        toolbarText = findViewById(R.id.toolbar_compmenu_text);
        setSupportActionBar(toolbar);
        menuNames = new ArrayList<>();
        menuIDs = new ArrayList<>();
        modelList = new ArrayList<>();

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

        setLogo();
        toolbarText.setText("Panel " + departmentName);

        if (menuUUID == null) {
            try {
                loadMenuUUID();
            } catch (Exception e) {
            }
        }

        controlIfFirstTime();

        spinnerAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,menuNames);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    public void deleteTableOrders(View view) {
        openDialog();
    }

    public void openDialog() {
        OrderTableDeletionDialog deletionDialog = new OrderTableDeletionDialog();
        deletionDialog.show(getSupportFragmentManager(), "delete table from orders");
    }

    public void loadMenuUUID() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Companies").child(Company.getInstance().getUUID())
                .child("Departments").child(departmentID).child("menuID");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                menuUUID = snapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void loadProducts() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Customers");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                modelList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String dbCompanyID = ds.child("companyID").getValue(String.class);
                    String dbDepartmentID =  ds.child("departmentID").getValue(String.class);
                    String dbMenuID = ds.child("menuID").getValue(String.class);
                    String dbTableNo = ds.child("tableNO").getValue(String.class);
                    String dateString = ds.child("dateString").getValue(String.class);

                    try {
                        if (dbCompanyID.equals(Company.getInstance().getUUID()) && dbDepartmentID.equals(departmentID) && dbMenuID.equals(menuUUID)) {
                            for (DataSnapshot ds2 : ds.child("Orders").getChildren()) {
                                String productName = ds2.child("name").getValue(String.class);
                                String productDescription = ds2.child("description").getValue(String.class);
                                float productPrice = ds2.child("price").getValue(float.class);
                                productPrice = (float) (Math.round(productPrice * 100) / 100.0);
                                PanelProductModel model;

                                if (ds2.child("imageUri").getValue(String.class) != null) {
                                    Uri imageUri = Uri.parse(ds2.child("imageUri").getValue(String.class));
                                    model = new PanelProductModel(productName, productDescription, productPrice, dateString, dbTableNo, imageUri);
                                } else {
                                    model = new PanelProductModel(productName, productDescription, productPrice, dateString, dbTableNo);
                                }

                                try {
                                    if (ds2.child("done") != null) {
                                        if (ds2.child("done").getValue(String.class).equals("yes")) {
                                            model.setDone(true);
                                        }
                                    }
                                } catch (Exception e) {
                                }

                                model.setID(ds2.getKey());
                                modelList.add(model);
                            }
                        }
                    } catch (Exception e) {
                        System.out.println("Hata: " + e.getLocalizedMessage() + " " + e.getMessage() + " " + e.getCause());
                    }

                }

                Collections.sort(modelList);
                buildAdapter();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void buildAdapter() {
        recyclerView.setHasFixedSize(true);
        adapter = new PanelProductAdapter(modelList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(adapter);
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

    public void setLogo() {
        Picasso.with(getApplicationContext())
                .load(Company.getInstance().getImageUri())
                .placeholder(R.drawable.border_selected)
                .into(toolbarImageView);
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

        loadProducts();
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

                loadProducts();
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

    public void goBack(View view) {
        finish();
    }
}