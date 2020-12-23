package com.ugur.safemealdeneme.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Spinner;

import com.ugur.safemealdeneme.Adapters.QRCodeVisualAdapter;
import com.ugur.safemealdeneme.Classes.Company;
import com.ugur.safemealdeneme.Models.QRCodeVisualModel;
import com.ugur.safemealdeneme.R;

import java.util.ArrayList;

public class QRCodeViewActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    QRCodeVisualAdapter adapter;
    private ArrayList<QRCodeVisualModel> qrModelList;

    String departmentID;
    String menuID;
    int tableAmount;

    public static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code_view);

        getSupportActionBar().setTitle("QR Codes");
        context = getApplicationContext();

        recyclerView = findViewById(R.id.recycler_view_qr_code_visual);
        qrModelList = new ArrayList<>();

        Intent intent = getIntent();
        departmentID = intent.getStringExtra("departmentID");
        menuID = intent.getStringExtra("menuUUID");
        tableAmount = Integer.parseInt(intent.getStringExtra("amount"));

        ActivityCompat.requestPermissions(QRCodeViewActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        ActivityCompat.requestPermissions(QRCodeViewActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);

        for (int i=0; i<tableAmount; i++) {
            String compositeID = Company.getInstance().getUUID() + " " + departmentID + " " + menuID + " " + (i+1);
            String tableInfo = "Table " + (i+1);
            QRCodeVisualModel model = new QRCodeVisualModel(compositeID,tableInfo);
            qrModelList.add(model);
        }

        //Set Adapter
        recyclerView.setHasFixedSize(true);
        adapter = new QRCodeVisualAdapter(qrModelList);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 4));
        recyclerView.setAdapter(adapter);
    }
}