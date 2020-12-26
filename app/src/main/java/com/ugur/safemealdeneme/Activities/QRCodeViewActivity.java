package com.ugur.safemealdeneme.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Spinner;

import com.muddzdev.styleabletoastlibrary.StyleableToast;
import com.ugur.safemealdeneme.Adapters.QRCodeVisualAdapter;
import com.ugur.safemealdeneme.Classes.Company;
import com.ugur.safemealdeneme.Models.QRCodeVisualModel;
import com.ugur.safemealdeneme.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_download_all_qr:
                //Download All the QR Codes
                saveImage();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.download_all_qrcodes,menu);
        return true;
    }

    public void saveImage() {
        final OutputStream[] outputStream = new OutputStream[1];
        for (QRCodeVisualModel model : qrModelList) {
            String qrString = model.getQrCompositeID();
            QRGEncoder qrgEncoder = new QRGEncoder(qrString,null, QRGContents.Type.TEXT,100);
            Bitmap qrBits = qrgEncoder.getBitmap();

            /*String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString()+ "/Camera/" + model.getQrInfo();
            File myDir = new File(root);
            myDir.mkdirs();
            String fname = "Image-" + model.getQrInfo() + ".png";
            File file = new File(myDir, fname);
            if (file.exists()) file.delete();
            try {
                FileOutputStream out = new FileOutputStream(file);
                qrBits.compress(Bitmap.CompressFormat.PNG, 100, out);
                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            MediaScannerConnection.scanFile(context, new String[]{file.getPath()}, new String[]{"image/jpeg"}, null);*/

            File filepath = Environment.getExternalStorageDirectory();
            File dir = new File(filepath.getPath() + "/" + model.getQrInfo() + "/");
            dir.mkdir();
            File file = new File(dir,model.getQrInfo() + ".jpg");
            try {
                outputStream[0] = new FileOutputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            qrBits.compress(Bitmap.CompressFormat.JPEG,100, outputStream[0]);

            try {
                outputStream[0].flush();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                outputStream[0].close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            MediaScannerConnection.scanFile(context, new String[]{file.getPath()}, new String[]{"image/jpeg"}, null);

            //String savedImageUrl = MediaStore.Images.Media.insertImage(getContentResolver(),qrBits,model.getQrInfo(),model.getQrInfo());
            //Uri savedImageUri = Uri.parse(savedImageUrl);

        }


        StyleableToast.makeText(getApplicationContext(),"All QR Codes Downloaded Successfully!",R.style.SuccessToast).show();
    }
}