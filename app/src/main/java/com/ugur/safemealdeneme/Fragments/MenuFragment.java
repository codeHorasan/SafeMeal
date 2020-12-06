package com.ugur.safemealdeneme.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ugur.safemealdeneme.Classes.Company;
import com.ugur.safemealdeneme.Classes.Menu;
import com.ugur.safemealdeneme.Classes.MenuRowItem;
import com.ugur.safemealdeneme.Classes.MenuRowItemAdapter;
import com.ugur.safemealdeneme.Dialogs.ChangeMenuNameDialog;
import com.ugur.safemealdeneme.Dialogs.MenuCreationDialog;
import com.ugur.safemealdeneme.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class MenuFragment extends Fragment {
    FloatingActionButton floatingActionButton;
    private static TextView noMenuText;
    static RecyclerView recyclerView;
    static RecyclerView.Adapter adapter;
    static RecyclerView.LayoutManager manager;
    static ArrayList<MenuRowItem> menuList;
    public static View view;

    public MenuFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_menu, container, false);

        floatingActionButton = view.findViewById(R.id.floating_button_menu);
        recyclerView = view.findViewById(R.id.menu_recycler_view);
        noMenuText = view.findViewById(R.id.text_view_no_menu);
        menuList = new ArrayList<>();

        loadMenus(view);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });

        return view;
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        super.onContextItemSelected(item);
        switch (item.getItemId()) {
            //Change Name
            case 10:
                openMenuNameDialog(menuList.get(item.getGroupId()).getUuid());
                return true;
             //Delete
            case 20:
                String uuid = menuList.get(item.getGroupId()).getUuid();
                menuList.remove(item.getGroupId());
                adapter.notifyDataSetChanged();
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                reference.child("Companies").child(Company.getInstance().getUUID()).child("Menus").child(uuid).removeValue();
                loadMenus(view);
                return true;
        }
        return false;
    }

    public void openMenuNameDialog(String uuid) {
        ChangeMenuNameDialog changeMenuNameDialog = new ChangeMenuNameDialog(uuid);
        changeMenuNameDialog.show(getFragmentManager(), "change menu name");
    }

    public static void loadMenus(final View view) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Companies").child(Company.getInstance().getUUID()).child("Menus");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Company.getInstance().getMenuList().clear();
                menuList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    HashMap<String,String> map = (HashMap<String, String>) ds.getValue();
                    String uuid = ds.getKey();
                    String name = map.get("Name");
                    String dateString = map.get("DateTime");

                    Menu menu = new Menu(name);
                    menu.setUuid(uuid);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
                    menu.setDateString(dateString);
                    try {
                        menu.setCreationDate(sdf.parse(menu.getDateString()));
                        menuList.add(new MenuRowItem(name,uuid,sdf.parse(menu.getDateString())));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    Company.getInstance().getMenuList().add(menu);

                }

                if (menuList.size() == 0) {
                    noMenuText.setVisibility(View.VISIBLE);
                } else {
                    noMenuText.setVisibility(View.GONE);
                }

                Collections.sort(menuList);
                setRecyclerViewLayout(view);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public static void setRecyclerViewLayout(View view) {
        recyclerView.setHasFixedSize(true);
        manager = new GridLayoutManager(view.getContext(),2);
        adapter = new MenuRowItemAdapter(menuList);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
    }

    public void openDialog() {
        MenuCreationDialog menuCreationDialog = new MenuCreationDialog();
        menuCreationDialog.show(getFragmentManager(), "menu creation dialog");
    }
}
