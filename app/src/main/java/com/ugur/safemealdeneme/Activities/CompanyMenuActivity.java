package com.ugur.safemealdeneme.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;
import com.ugur.safemealdeneme.Classes.Company;
import com.ugur.safemealdeneme.Classes.CompanyMenuCategoryItem;
import com.ugur.safemealdeneme.R;

import java.util.ArrayList;

public class CompanyMenuActivity extends AppCompatActivity {
    Toolbar toolbar;
    ImageView toolbarImageView;
    TextView textMenu;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    ArrayList<CompanyMenuCategoryItem> categoryItems;
    FloatingActionButton floatingActionButton;

    private String name;
    private String uuid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_menu);

        recyclerView = findViewById(R.id.recycler_view_menu_categories_dept);
        floatingActionButton = findViewById(R.id.floating_button_add_category);
        toolbar = findViewById(R.id.toolbar_company_menu);
        toolbarImageView = findViewById(R.id.toolbar_compmenu_image);
        textMenu = findViewById(R.id.toolbar_compmenu_text);
        setSupportActionBar(toolbar);

        categoryItems = new ArrayList<>();

        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        uuid = intent.getStringExtra("uuid");

        //Loading Image
        Picasso.with(getApplicationContext())
                .load(Company.getInstance().getImageUri())
                .placeholder(R.drawable.border_selected)
                .into(toolbarImageView);

        textMenu.setText("Menu: " + name);


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), CategoryAdditionActivity.class));
            }
        });

    }

    public void goBack(View view) {
        finish();
    }
}