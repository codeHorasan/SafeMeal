package com.ugur.safemealdeneme.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.muddzdev.styleabletoastlibrary.StyleableToast;
import com.ugur.safemealdeneme.Classes.Company;
import com.ugur.safemealdeneme.DepartmentConstantsClass;
import com.ugur.safemealdeneme.R;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

public class ProductAdditionActivity extends AppCompatActivity {
    private TextInputLayout nameInput;
    private TextView descriptionText;
    private TextInputLayout priceText;
    private CheckBox checkBox;
    private ImageView productImage;
    private Uri imageUri;

    private String uuid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_addition);

        getSupportActionBar().setTitle("Product Addition");

        nameInput = findViewById(R.id.text_product_name);
        descriptionText = findViewById(R.id.text_product_description);
        priceText = findViewById(R.id.text_product_price);
        checkBox = findViewById(R.id.check_box_product_image);
        productImage = findViewById(R.id.product_image);

        Intent intent = getIntent();
        uuid = intent.getStringExtra("uuid");

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2020 && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                productImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void checkBoxClick(View view) {
        if (checkBox.isChecked()) {
            productImage.setVisibility(View.VISIBLE);
        } else {
            productImage.setVisibility(View.GONE);
        }
    }

    public void setProductImage(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 2020);
    }

    public void addProduct(View view) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference reference = database.getReference();

        final String productID = UUID.randomUUID().toString();
        final String productName = nameInput.getEditText().getText().toString().trim();
        final String productDescription = descriptionText.getText().toString().trim();
        final String price = priceText.getEditText().getText().toString().trim();
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
        final String currentDateTime = sdf.format(new Date());

        reference.child("Companies").child(Company.getInstance().getUUID()).child("Menus").child(DepartmentConstantsClass.CURRENT_MENU_UUID)
                .child("Categories").child(uuid).child("Products").child(productID).child("name").setValue(productName);
        reference.child("Companies").child(Company.getInstance().getUUID()).child("Menus").child(DepartmentConstantsClass.CURRENT_MENU_UUID)
                .child("Categories").child(uuid).child("Products").child(productID).child("description").setValue(productDescription);
        reference.child("Companies").child(Company.getInstance().getUUID()).child("Menus").child(DepartmentConstantsClass.CURRENT_MENU_UUID)
                .child("Categories").child(uuid).child("Products").child(productID).child("DateTime").setValue(currentDateTime);
        reference.child("Companies").child(Company.getInstance().getUUID()).child("Menus").child(DepartmentConstantsClass.CURRENT_MENU_UUID)
                .child("Categories").child(uuid).child("Products").child(productID).child("price").setValue(price);

        if (checkBox.isChecked()) {
            if (imageUri != null) {
                String randomImageID = UUID.randomUUID().toString();
                final StorageReference storageReference = FirebaseStorage.getInstance().getReference("products").child(randomImageID + ".");
                storageReference.putFile(imageUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        return storageReference.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri downloadUri = task.getResult();
                            reference.child("Companies").child(Company.getInstance().getUUID()).child("Menus").child(DepartmentConstantsClass.CURRENT_MENU_UUID)
                                    .child("Categories").child(uuid).child("Products").child(productID).child("imageUri").setValue(downloadUri.toString());
                            finish();
                        }
                    }
                });
            } else {
                StyleableToast.makeText(getApplicationContext(),"Please select an image",R.style.FailureToast).show();
                return;
            }
        } else {
            finish();
        }

    }
}