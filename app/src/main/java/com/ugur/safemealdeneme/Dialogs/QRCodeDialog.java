package com.ugur.safemealdeneme.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.ugur.safemealdeneme.Classes.Company;
import com.ugur.safemealdeneme.R;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class QRCodeDialog extends AppCompatDialogFragment {
    private ImageView qrCodeImage;

    private String departmentID;
    private String menuID;

    public QRCodeDialog() {
    }

    public void setDepartmentID(String departmentID) {
        this.departmentID = departmentID;
    }

    public void setMenuID(String menuID) {
        this.menuID = menuID;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_qr_code_dialog,null);

        builder.setView(view)
                .setTitle("QR Code")
                .setNegativeButton("Back", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setPositiveButton("Download", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.out.println("DOwnloading...");
                    }
                });

        qrCodeImage = view.findViewById(R.id.image_view_qrcode);
        String qrString = Company.getInstance().getUUID() + " " + departmentID + " " + menuID;
        QRGEncoder qrgEncoder = new QRGEncoder(qrString,null, QRGContents.Type.TEXT,100);
        Bitmap qrBits = qrgEncoder.getBitmap();
        qrCodeImage.setImageBitmap(qrBits);

        return builder.create();
    }
}
