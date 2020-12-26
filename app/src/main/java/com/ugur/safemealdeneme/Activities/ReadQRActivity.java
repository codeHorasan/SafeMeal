package com.ugur.safemealdeneme.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.budiyev.android.codescanner.AutoFocusMode;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.budiyev.android.codescanner.ScanMode;
import com.google.zxing.Result;
import com.ugur.safemealdeneme.R;

public class ReadQRActivity extends AppCompatActivity {
    CodeScanner codeScanner;
    CodeScannerView codeScannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_qr);

        codeScannerView = findViewById(R.id.qr_scanner_view);
        codeScanner = new CodeScanner(this,codeScannerView);

        codeScanner.setCamera(CodeScanner.CAMERA_BACK);
        codeScanner.setFormats(CodeScanner.ALL_FORMATS);
        codeScanner.setAutoFocusMode(AutoFocusMode.SAFE);
        codeScanner.setScanMode(ScanMode.CONTINUOUS);
        codeScanner.setAutoFocusEnabled(true);
        codeScanner.setFlashEnabled(false);

        codeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull Result result) {
                String wholeString = result.getText();
                String[] array = new String[4];
                array = wholeString.split(" ");

                String companyID = array[0];
                String departmentID = array[1];
                String menuID = array[2];
                String tableNO = array[3];

                Intent intent = new Intent(getApplicationContext(), CustomerCategoryActivity.class);
                intent.putExtra("companyID", companyID);
                intent.putExtra("departmentID", departmentID);
                intent.putExtra("menuID", menuID);
                intent.putExtra("tableNO", tableNO);
                startActivity(intent);
            }
        });

        codeScannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                codeScanner.startPreview();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        codeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        codeScanner.releaseResources();
        super.onPause();
    }
}