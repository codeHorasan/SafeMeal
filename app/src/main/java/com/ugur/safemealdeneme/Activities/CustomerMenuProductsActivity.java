package com.ugur.safemealdeneme.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
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
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.ugur.safemealdeneme.Adapters.CustomerProductAdapter;
import com.ugur.safemealdeneme.Models.CustomerProductModel;
import com.ugur.safemealdeneme.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class CustomerMenuProductsActivity extends AppCompatActivity {
    Toolbar toolbar;
    ImageView toolbarImageView;
    TextView toolbarText;

    androidx.appcompat.widget.SearchView searchView;
    RecyclerView recyclerView;
    CustomerProductAdapter adapter;
    ArrayList<CustomerProductModel> productList;

    private String companyID;
    private String departmentID;
    private String menuID;
    private String tableNO;
    private String categoryID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_menu_products);

        toolbar = findViewById(R.id.toolbar_company_menu);
        toolbarImageView = findViewById(R.id.toolbar_compmenu_image);
        toolbarText = findViewById(R.id.toolbar_compmenu_text);
        setSupportActionBar(toolbar);
        recyclerView = findViewById(R.id.customer_recyclerview_products);
        searchView = findViewById(R.id.search_view_product);

        Intent intent = getIntent();
        companyID = intent.getStringExtra("companyID");
        departmentID = intent.getStringExtra("departmentID");
        menuID = intent.getStringExtra("menuID");
        tableNO = intent.getStringExtra("tableNO");
        categoryID = intent.getStringExtra("categoryID");

        productList = new ArrayList<>();

        setLogo();
        toolbarText.setText("Table " + tableNO);

        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        try {
            loadProducts();
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public void loadProducts() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Companies").child(companyID).child("Menus")
                .child(menuID).child("Categories").child(categoryID).child("Products");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    HashMap<String,String> map = (HashMap<String, String>) ds.getValue();
                    String name = map.get("name");
                    String description = map.get("description");
                    String dateString = map.get("DateTime");
                    float price = Float.parseFloat(map.get("price"));

                    if (map.get("imageUri") == null) {
                        productList.add(new CustomerProductModel(ds.getKey(),name,description,price,dateString));
                    } else {
                        Uri imageUri = Uri.parse(map.get("imageUri"));
                        productList.add(new CustomerProductModel(ds.getKey(),name,description,price,imageUri,dateString));
                    }

                }

                Collections.sort(productList);
                buildAdapter();
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
        adapter = new CustomerProductAdapter(productList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(adapter);
    }

    public void goBack(View view) {
        finish();
    }
}