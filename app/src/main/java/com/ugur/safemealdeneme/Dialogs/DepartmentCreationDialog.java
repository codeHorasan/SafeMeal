package com.ugur.safemealdeneme.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ugur.safemealdeneme.Classes.Company;
import com.ugur.safemealdeneme.Classes.Department;
import com.ugur.safemealdeneme.Classes.Menu;
import com.ugur.safemealdeneme.Fragments.DepartmentFragment;
import com.ugur.safemealdeneme.Fragments.MenuFragment;
import com.ugur.safemealdeneme.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DepartmentCreationDialog extends AppCompatDialogFragment {
    private TextInputLayout nameInput;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_department_creation, null);

        builder.setView(view)
                .setTitle("Create Department")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = nameInput.getEditText().getText().toString().trim();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
                        String currentDateTime = sdf.format(new Date());
                        Department department = new Department(name);
                        department.setDateString(currentDateTime);
                        try {
                            department.setCreationDate(sdf.parse(currentDateTime));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        Company.getInstance().getDepartmentList().add(department);

                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                        reference.child("Companies").child(Company.getInstance().getUUID()).child("Departments")
                                .child(department.getUuid()).child("Name").setValue(department.getName());

                        reference.child("Companies").child(Company.getInstance().getUUID()).child("Departments")
                                .child(department.getUuid()).child("DateTime").setValue(department.getDateString());

                        DepartmentFragment.loadDepartments(DepartmentFragment.view);
                    }
                });

        nameInput = view.findViewById(R.id.dialog_text_department_name);

        return builder.create();
    }
}
