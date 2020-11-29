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
import com.ugur.safemealdeneme.Classes.Menu;
import com.ugur.safemealdeneme.Fragments.MenuFragment;
import com.ugur.safemealdeneme.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MenuCreationDialog extends AppCompatDialogFragment {
    private TextInputLayout nameInput;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_menu_creation_dialog, null);

        builder.setView(view)
                .setTitle("Create Menu")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = nameInput.getEditText().getText().toString().trim();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
                        String currentDateTime = sdf.format(new Date());
                        Menu menu = new Menu(name);
                        menu.setDateString(currentDateTime);
                        try {
                            menu.setCreationDate(sdf.parse(currentDateTime));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        Company.getInstance().getMenuList().add(menu);

                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                        reference.child("Companies").child(Company.getInstance().getUUID()).child("Menus")
                                .child(menu.getUuid()).child("Name").setValue(menu.getName());

                        reference.child("Companies").child(Company.getInstance().getUUID()).child("Menus")
                                .child(menu.getUuid()).child("DateTime").setValue(menu.getDateString());

                        MenuFragment.loadMenus(MenuFragment.view);
                    }
                });

        nameInput = view.findViewById(R.id.dialog_text_menu_name);

        return builder.create();
    }
}
