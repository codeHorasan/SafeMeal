package com.ugur.safemealdeneme.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.ugur.safemealdeneme.Classes.Company;
import com.ugur.safemealdeneme.Fragments.DepartmentFragment;
import com.ugur.safemealdeneme.Fragments.MenuFragment;
import com.ugur.safemealdeneme.R;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    ImageView toolbarImageView;
    TextView textLogo;

    private ViewPager viewPager;
    private TabLayout tabLayout;
    DepartmentFragment departmentFragment;
    MenuFragment menuFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar_main_layout);
        toolbarImageView = toolbar.findViewById(R.id.toolbar_image_view);
        textLogo = findViewById(R.id.logo_text_view);
        setSupportActionBar(toolbar);

        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tab_layout);
        departmentFragment = new DepartmentFragment();
        menuFragment = new MenuFragment();
        tabLayout.setupWithViewPager(viewPager);

        Intent intent = getIntent();
        if (intent.getStringExtra("Load") != null && intent.getStringExtra("Load").equals("Yes")) {
            System.out.println("Load Data");
            loadData();
        } else {
            System.out.println("Get Logo");
            getLogo();
            textLogo.setText(Company.getInstance().getName());
        }

    }

    public void setFragments() {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(),0);
        viewPagerAdapter.addFragment(departmentFragment, "Departments");
        viewPagerAdapter.addFragment(menuFragment, "Menus");
        viewPager.setAdapter(viewPagerAdapter);
    }

    public void getLogo() {
        Picasso.with(getApplicationContext())
                .load(Company.getInstance().getImageUri())
                .placeholder(R.drawable.border_selected)
                .into(toolbarImageView);

        setFragments();
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
                        textLogo.setText(company.getName());
                    }
                }

                setFragments();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private ArrayList<Fragment> fragments = new ArrayList<>();
        private ArrayList<String> fragmentTitles = new ArrayList<>();

        public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        public void addFragment(Fragment fragment, String title) {
            fragments.add(fragment);
            fragmentTitles.add(title);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitles.get(position);
        }
    }
}