package com.ugur.safemealdeneme.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.ugur.safemealdeneme.Adapters.CustomerMenuCategoryItemAdapter;
import com.ugur.safemealdeneme.Classes.Customer;
import com.ugur.safemealdeneme.Models.CustomerMenuCategoryModel;
import com.ugur.safemealdeneme.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class CustomerCategoryActivity extends AppCompatActivity {
    Toolbar toolbar;
    ImageView toolbarImageView;
    TextView toolbarText;

    RecyclerView recyclerView;
    CustomerMenuCategoryItemAdapter adapter;
    ArrayList<CustomerMenuCategoryModel> categoryList;

    private String companyID;
    private String departmentID;
    private String menuID;
    private String tableNO;
    private String categoryID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_category);

        toolbar = findViewById(R.id.toolbar_main_layout);
        toolbarImageView = findViewById(R.id.toolbar_image_view);
        toolbarText = findViewById(R.id.logo_text_view);
        setSupportActionBar(toolbar);
        recyclerView = findViewById(R.id.customer_recyclerview_categories);

        Intent intent = getIntent();
        companyID = intent.getStringExtra("companyID");
        departmentID = intent.getStringExtra("departmentID");
        menuID = intent.getStringExtra("menuID");
        tableNO = intent.getStringExtra("tableNO");

        categoryList = new ArrayList<>();

        setLogo();
        toolbarText.setText("Table " + tableNO);

        try {
            loadData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        categoryList.clear();
        loadData();
    }

    public void setLogo() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Companies").child(companyID).child("LogoUri");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String uri = snapshot.getValue(String.class);
                Picasso.with(getApplicationContext())
                        .load(uri)
                        .placeholder(R.drawable.border_selected)
                        .into(toolbarImageView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void loadData() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Companies").child(companyID).child("Menus")
                .child(menuID).child("Categories");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    HashMap<String,String> map = (HashMap<String, String>) ds.getValue();
                    String categoryName = map.get("name");
                    Uri imageUri = Uri.parse(map.get("imageUri"));
                    String uuid = ds.getKey();
                    String dateString = map.get("DateTime");
                    categoryList.add(new CustomerMenuCategoryModel(imageUri, categoryName, dateString, uuid));
                }

                Collections.sort(categoryList);
                buildAdapter();

                Customer customer = Customer.getInstance();
                customer.setCompanyID(companyID);
                customer.setDepartmentID(departmentID);
                customer.setMenuID(menuID);
                customer.setTableNO(tableNO);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.product_basket_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.product_basket:
                Intent intent = new Intent(getApplicationContext(), BasketActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void buildAdapter() {
        recyclerView.setHasFixedSize(true);
        adapter = new CustomerMenuCategoryItemAdapter(categoryList);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new CustomerMenuCategoryItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                categoryID = categoryList.get(position).getUuid();
                Intent intent = new Intent(getApplicationContext(),CustomerMenuProductsActivity.class);
                intent.putExtra("companyID", companyID);
                intent.putExtra("departmentID", departmentID);
                intent.putExtra("menuID", menuID);
                intent.putExtra("tableNO", tableNO);
                intent.putExtra("categoryID", categoryID);
                startActivity(intent);
            }
        });
    }
}