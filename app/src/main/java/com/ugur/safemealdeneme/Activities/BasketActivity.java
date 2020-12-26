package com.ugur.safemealdeneme.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.ugur.safemealdeneme.Adapters.OrderBasketAdapter;
import com.ugur.safemealdeneme.Classes.Customer;
import com.ugur.safemealdeneme.Models.CustomerProductModel;
import com.ugur.safemealdeneme.R;

import java.text.DecimalFormat;

public class BasketActivity extends AppCompatActivity {
    Toolbar toolbar;
    ImageView toolbarImageView;
    TextView toolbarText;

    public static TextView totalPriceText;
    public static Button approveOrderButton;

    RecyclerView recyclerView;
    OrderBasketAdapter adapter;
    Customer customer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);

        recyclerView = findViewById(R.id.recycler_view_basket);
        totalPriceText = findViewById(R.id.text_view_basket_total_price);
        approveOrderButton = findViewById(R.id.button_approve_order);

        toolbar = findViewById(R.id.toolbar_company_menu);
        toolbarImageView = findViewById(R.id.toolbar_compmenu_image);
        toolbarText = findViewById(R.id.toolbar_compmenu_text);
        setSupportActionBar(toolbar);

        customer = Customer.getInstance();

        setLogo();
        toolbarText.setText("Table " + customer.getTableNO() + " Order Basket");

        approveOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                approveOrder();
            }
        });

        buildAdapter();
    }

    public void setLogo() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Companies").child(customer.getCompanyID()).child("LogoUri");
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

    public void buildAdapter() {
        recyclerView.setHasFixedSize(true);
        adapter = new OrderBasketAdapter(customer.getProductModelList());
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(adapter);
        setTotalPrice();
    }

    public static void setTotalPrice() {
        float totalPrice = 0;
        for (CustomerProductModel model : Customer.getInstance().getProductModelList()) {
            totalPrice += model.getPrice();
        }

        totalPriceText.setText(totalPrice + "$");
    }

    public void approveOrder() {
        System.out.println("Onaylandı!!");
    }

    public void goBack(View view) {
        finish();
    }
}