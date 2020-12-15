package com.ugur.safemealdeneme.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.muddzdev.styleabletoastlibrary.StyleableToast;
import com.squareup.picasso.Picasso;
import com.ugur.safemealdeneme.Classes.Company;
import com.ugur.safemealdeneme.Models.CompanyMenuCategoryModel;
import com.ugur.safemealdeneme.Adapters.CompanyMenuCategoryItemAdapter;
import com.ugur.safemealdeneme.DepartmentConstantsClass;
import com.ugur.safemealdeneme.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class CompanyMenuActivity extends AppCompatActivity {
    Toolbar toolbarNormal;
    Toolbar toolbarDeletion;
    ImageView toolbarImageView;
    TextView textMenu;
    RecyclerView recyclerView;
    CompanyMenuCategoryItemAdapter adapter;
    ArrayList<CompanyMenuCategoryModel> categoryItems;
    FloatingActionButton floatingActionButton;

    public static boolean ifActionMode = false;
    public static ArrayList<CompanyMenuCategoryModel> deletionItems;
    private  ArrayList<CompanyMenuCategoryModel> deletionCopies;
    private ItemTouchHelper itemTouchHelper;

    public static Context context;

    private String name;
    private String uuid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_menu);

        context = getApplicationContext();

        recyclerView = findViewById(R.id.recycler_view_menu_categories_dept);
        floatingActionButton = findViewById(R.id.floating_button_add_category);
        toolbarNormal = findViewById(R.id.toolbar_company_menu);
        toolbarImageView = findViewById(R.id.toolbar_compmenu_image);
        textMenu = findViewById(R.id.toolbar_compmenu_text);
        setSupportActionBar(toolbarNormal);
        toolbarDeletion = findViewById(R.id.toolbar_action_category_deletion);

        categoryItems = new ArrayList<>();
        deletionItems = new ArrayList<>();


        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        uuid = intent.getStringExtra("uuid");

        if (name == null) {
            name = DepartmentConstantsClass.CURRENT_MENU_NAME;
            uuid = DepartmentConstantsClass.CURRENT_MENU_UUID;
        }

        DepartmentConstantsClass.CURRENT_MENU_NAME = name;
        DepartmentConstantsClass.CURRENT_MENU_UUID = uuid;

        //Loading Image
        Picasso.with(getApplicationContext())
                .load(Company.getInstance().getImageUri())
                .placeholder(R.drawable.border_selected)
                .into(toolbarImageView);

        textMenu.setText("Categories of " + name);

        try {
            loadCategories();
        } catch (Exception e) {
            e.printStackTrace();
        }

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CategoryAdditionActivity.class);
                intent.putExtra("name",name);
                intent.putExtra("uuid",uuid);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //To Reset the categories
        categoryItems.clear();
        loadCategories();
    }

    public void cancelCategoryDeletionMode(View view) {
        ifActionMode = false;
        toolbarNormal.setVisibility(View.VISIBLE);
        toolbarDeletion.setVisibility(View.GONE);
        floatingActionButton.setVisibility(View.VISIBLE);
        adapter.notifyDataSetChanged();
    }

    public void deleteCategories(View view) {
        ifActionMode = false;
        deleteCategories();
        toolbarDeletion.setVisibility(View.GONE);
        toolbarNormal.setVisibility(View.VISIBLE);
        floatingActionButton.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.company_category_deletion_menu, menu);
        return true;
    }

    public void deleteCategories() {
        if (deletionItems.size() > 0) {
            deletionCopies = new ArrayList<>(deletionItems);
            for (CompanyMenuCategoryModel item : deletionItems) {
                //Delete from DB
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                reference.child("Companies").child(Company.getInstance().getUUID()).child("Menus").child(uuid).child("Categories")
                        .child(item.getUuid()).removeValue();

                categoryItems.remove(item);
            }
            //Reset the adapter
            adapter.notifyDataSetChanged();

            //Clearing selected positions to delete for avoiding re-deleting
            deletionItems.clear();

            StyleableToast.makeText(getApplicationContext(), "Category deletion completed!", R.style.SuccessToast).show();
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.open_deletion_mode:
                if (!ifActionMode) {
                    ifActionMode = true;
                    toolbarNormal.setVisibility(View.GONE);
                    toolbarDeletion.setVisibility(View.VISIBLE);
                    floatingActionButton.setVisibility(View.GONE);
                } else {
                    ifActionMode = false;
                }
                adapter.notifyDataSetChanged();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void loadCategories() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Companies").child(Company.getInstance().getUUID()).child("Menus")
                .child(uuid).child("Categories");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    HashMap<String,String> map = (HashMap<String, String>) ds.getValue();
                    String categoryName = map.get("name");
                    Uri imageUri = Uri.parse(map.get("imageUri"));
                    String uuid = ds.getKey();
                    String dateString = map.get("DateTime");
                    categoryItems.add(new CompanyMenuCategoryModel(imageUri, categoryName, dateString, uuid));
                }

                Collections.sort(categoryItems);
                buildAdapter();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void buildAdapter() {
        recyclerView.setHasFixedSize(true);
        adapter = new CompanyMenuCategoryItemAdapter(categoryItems);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        recyclerView.setAdapter(adapter);
        itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN |
            ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT,0) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            int fromPosition = viewHolder.getAdapterPosition();
            int toPosition = target.getAdapterPosition();

            if (fromPosition < toPosition) {
                for (int i = fromPosition; i < toPosition; i++) {
                    String swapHelper = categoryItems.get(i).getDateString();
                    categoryItems.get(i).setDateString(categoryItems.get(i+1).getDateString());
                    categoryItems.get(i+1).setDateString(swapHelper);
                    Collections.swap(categoryItems, i, i + 1);
                }
            } else {
                for (int i = fromPosition; i > toPosition; i--) {
                    String swapHelper = categoryItems.get(i).getDateString();
                    categoryItems.get(i).setDateString(categoryItems.get(i-1).getDateString());
                    categoryItems.get(i-1).setDateString(swapHelper);
                    Collections.swap(categoryItems, i, i - 1);
                }
            }

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference reference = database.getReference();
            for (CompanyMenuCategoryModel model : categoryItems) {
                reference.child("Companies").child(Company.getInstance().getUUID()).child("Menus").child(uuid).child("Categories")
                        .child(model.getUuid()).child("DateTime").setValue(model.getDateString());
            }

            adapter.notifyItemMoved(fromPosition,toPosition);

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