package com.ugur.safemealdeneme.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.ugur.safemealdeneme.Classes.Company;
import com.ugur.safemealdeneme.R;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    ImageView toolbarImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar_main_layout);
        toolbarImageView = toolbar.findViewById(R.id.toolbar_image_view);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        if (intent.getStringExtra("Load") != null && intent.getStringExtra("Load").equals("Yes")) {
            loadData();
        } else {
            getLogo();
        }
    }

    public void getLogo() {
        Picasso.with(getApplicationContext())
                .load(Company.getInstance().getImageUri())
                .placeholder(R.drawable.border_selected)
                .into(toolbarImageView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_sign_out:
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();
                startActivity(new Intent(getApplicationContext(), EntranceActivity.class));
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void loadData() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Companies");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    HashMap<String,String> map = (HashMap<String, String>) ds.getValue();
                    if (ds.getKey().matches(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        Company company = Company.getInstance();
                        company.setUUID(ds.getKey());
                        company.setEmail(map.get("Email"));
                        company.setName(map.get("Name"));
                        company.setImageUri(Uri.parse(map.get("LogoUri")));

                        Picasso.with(getApplicationContext())
                                .load(Company.getInstance().getImageUri())
                                .placeholder(R.drawable.border_selected)
                                .into(toolbarImageView);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}