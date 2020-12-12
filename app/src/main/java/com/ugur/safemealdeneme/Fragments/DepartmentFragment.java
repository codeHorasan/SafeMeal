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
import com.ugur.safemealdeneme.Classes.Department;
import com.ugur.safemealdeneme.Models.DepartmentRowModel;
import com.ugur.safemealdeneme.Adapters.DepartmentRowItemAdapter;
import com.ugur.safemealdeneme.Dialogs.ChangeDepartmentNameDialog;
import com.ugur.safemealdeneme.Dialogs.DepartmentCreationDialog;
import com.ugur.safemealdeneme.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class DepartmentFragment extends Fragment {
    FloatingActionButton floatingActionButton;
    static RecyclerView recyclerView;
    static RecyclerView.Adapter adapter;
    static RecyclerView.LayoutManager manager;
    static ArrayList<DepartmentRowModel> departmentList;
    public static View view;
    private static TextView noDepartmentText;

    public DepartmentFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_department, container, false);

        floatingActionButton = view.findViewById(R.id.floating_button_department);
        recyclerView = view.findViewById(R.id.department_recycler_view);
        noDepartmentText = view.findViewById(R.id.text_view_no_department);
        departmentList = new ArrayList<>();

        loadDepartments(view);

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
            case 11:
                openNameDialog(departmentList.get(item.getGroupId()).getUuid());
                return true;
            //Delete
            case 21:
                String uuid = departmentList.get(item.getGroupId()).getUuid();
                departmentList.remove(item.getGroupId());
                adapter.notifyDataSetChanged();
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                reference.child("Companies").child(Company.getInstance().getUUID()).child("Departments").child(uuid).removeValue();
                loadDepartments(view);
                return true;
        }
        return false;
    }

    public void openNameDialog(String uuid) {
        ChangeDepartmentNameDialog changeDepartmentNameDialog = new ChangeDepartmentNameDialog(uuid);
        changeDepartmentNameDialog.show(getFragmentManager(), "change department name");
    }

    public static void loadDepartments(final View view) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Companies").child(Company.getInstance().getUUID()).child("Departments");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Company.getInstance().getDepartmentList().clear();
                departmentList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    HashMap<String,String> map = (HashMap<String, String>) ds.getValue();
                    String uuid = ds.getKey();
                    String name = map.get("Name");
                    String dateString = map.get("DateTime");

                    Department department = new Department(name);
                    department.setUuid(uuid);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
                    department.setDateString(dateString);
                    try {
                        department.setCreationDate(sdf.parse(department.getDateString()));
                        departmentList.add(new DepartmentRowModel(name,uuid,sdf.parse(department.getDateString())));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    Company.getInstance().getDepartmentList().add(department);

                }

                if (departmentList.size() == 0) {
                    noDepartmentText.setVisibility(View.VISIBLE);
                } else {
                    noDepartmentText.setVisibility(View.GONE);
                }

                Collections.sort(departmentList);
                setRecyclerView(view);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public static void setRecyclerView(View view) {
        recyclerView.setHasFixedSize(true);
        manager = new GridLayoutManager(view.getContext(),2);
        adapter = new DepartmentRowItemAdapter(departmentList);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
    }

    public void openDialog() {
        DepartmentCreationDialog departmentCreationDialog = new DepartmentCreationDialog();
        departmentCreationDialog.show(getFragmentManager(), "department creation dialog");
    }
}
