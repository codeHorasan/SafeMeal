package com.ugur.safemealdeneme.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.ugur.safemealdeneme.Adapters.DepartmentProductAdapter;
import com.ugur.safemealdeneme.Classes.Company;
import com.ugur.safemealdeneme.DepartmentConstantsClass;
import com.ugur.safemealdeneme.Models.DepartmentProductModel;
import com.ugur.safemealdeneme.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class CompanyMenuProductsActivity extends AppCompatActivity {
    Toolbar toolbar;
    ImageView toolbarImageView;
    TextView textCategory;
    static RecyclerView recyclerView;
    static DepartmentProductAdapter adapter;
    static ArrayList<DepartmentProductModel> productList;
    FloatingActionButton floatingActionButton;

    public static Context context;

    public static String categoryID;
    String categoryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_menu_products);

        context = getApplicationContext();

        toolbar = findViewById(R.id.toolbar_company_menu);
        toolbarImageView = findViewById(R.id.toolbar_compmenu_image);
        textCategory = findViewById(R.id.toolbar_compmenu_text);
        setSupportActionBar(toolbar);
        recyclerView = findViewById(R.id.recycler_view_company_menu_products);
        floatingActionButton = findViewById(R.id.floating_button_add_product);

        Intent intent = getIntent();
        categoryID = intent.getStringExtra("uuid");
        categoryName = intent.getStringExtra("name");

        if (categoryID == null) {
            categoryName = DepartmentConstantsClass.CURRENT_CATEGORY_NAME;
            categoryID = DepartmentConstantsClass.CURRENT_CATEGORY_UUID;
        }

        DepartmentConstantsClass.CURRENT_CATEGORY_NAME = categoryName;
        DepartmentConstantsClass.CURRENT_CATEGORY_UUID = categoryID;

        //Loading Image
        Picasso.with(getApplicationContext())
                .load(Company.getInstance().getImageUri())
                .placeholder(R.drawable.border_selected)
                .into(toolbarImageView);
        textCategory.setText("Category " + categoryName);

        productList = new ArrayList<>();

        try {
            loadProducts();
        } catch (Exception e) {
            e.printStackTrace();
        }

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ProductAdditionActivity.class);
                intent.putExtra("uuid",categoryID);
                intent.putExtra("name",categoryName);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        loadProducts();
    }

    public static void loadProducts() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Companies").child(Company.getInstance().getUUID()).child("Menus")
                .child(DepartmentConstantsClass.CURRENT_MENU_UUID).child("Categories").child(categoryID).child("Products");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    HashMap<String,String> map = (HashMap<String, String>) ds.getValue();
                    String name = map.get("name");
                    String description = map.get("description");
                    String dateString = map.get("DateTime");
                    Double price = Double.parseDouble(map.get("price"));

                    if (map.get("imageUri") == null) {
                        productList.add(new DepartmentProductModel(ds.getKey(),name,description,price,dateString));
                        System.out.println("Image Uri NUll " + name);
                    } else {
                        Uri imageUri = Uri.parse(map.get("imageUri"));
                        productList.add(new DepartmentProductModel(ds.getKey(),name,description,price,imageUri,dateString));
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

    public static void buildAdapter() {
        recyclerView.setHasFixedSize(true);
        adapter = new DepartmentProductAdapter(productList);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(adapter);
    }

    static ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP |ItemTouchHelper.DOWN, 0) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            int fromPosition = viewHolder.getAdapterPosition();
            int toPosition = target.getAdapterPosition();

            if (fromPosition < toPosition) {
                for (int i = fromPosition; i < toPosition; i++) {
                    String swapHelper = productList.get(i).getDateString();
                    productList.get(i).setDateString(productList.get(i+1).getDateString());
                    productList.get(i+1).setDateString(swapHelper);
                    Collections.swap(productList, i, i + 1);
                }
            } else {
                for (int i = fromPosition; i > toPosition; i--) {
                    String swapHelper = productList.get(i).getDateString();
                    productList.get(i).setDateString(productList.get(i-1).getDateString());
                    productList.get(i-1).setDateString(swapHelper);
                    Collections.swap(productList, i, i - 1);
                }
            }

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference reference = database.getReference();
            for (DepartmentProductModel product : productList) {
                reference.child("Companies").child(Company.getInstance().getUUID()).child("Menus").child(DepartmentConstantsClass.CURRENT_MENU_UUID).child("Categories")
                        .child(categoryID).child("Products").child(product.getProductId()).child("DateTime").setValue(product.getDateString());
            }

            adapter.notifyItemMoved(fromPosition,toPosition);

            return false;
        }

        @Override
        public boolean isLongPressDragEnabled() {
            return true;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        }
    };


    public void goBack(View view) {
        finish();
    }
}