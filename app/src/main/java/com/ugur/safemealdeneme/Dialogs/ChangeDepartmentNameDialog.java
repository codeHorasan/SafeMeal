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
import com.ugur.safemealdeneme.Fragments.DepartmentFragment;
import com.ugur.safemealdeneme.R;

public class ChangeDepartmentNameDialog extends AppCompatDialogFragment {
    private TextInputLayout nameInput;
    private String uuid;

    public ChangeDepartmentNameDialog(String uuid) {
        this.uuid = uuid;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_change_department_name,null);

        builder.setView(view)
                .setTitle("Change Department Name")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setPositiveButton("Change", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = nameInput.getEditText().getText().toString().trim();
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                        reference.child("Companies").child(Company.getInstance().getUUID()).child("Departments")
                                .child(uuid).child("Name").setValue(name);

                        DepartmentFragment.loadDepartments(DepartmentFragment.view);
                    }
                });

        nameInput = view.findViewById(R.id.dialog_text_change_department_name);

        return builder.create();
    }
}
