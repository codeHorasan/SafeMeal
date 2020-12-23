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
import com.squareup.picasso.Picasso;
import com.ugur.safemealdeneme.Classes.Company;
import com.ugur.safemealdeneme.DepartmentConstantsClass;
import com.ugur.safemealdeneme.R;

import java.io.IOException;
import java.util.UUID;

public class ChangeProductActivity extends AppCompatActivity {
    TextInputLayout nameInput, priceInput;
    TextView descriptionText;
    ImageView imageView;
    CheckBox checkBox;
    ImageView imageProduct;

    String productID, productName, productDescription;
    Double productPrice;
    Uri productUri;
    String categoryID;

    private Uri newImageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_product);

        nameInput = findViewById(R.id.text_product_change_name);
        priceInput = findViewById(R.id.text_product_change_price);
        descriptionText = findViewById(R.id.text_product_change_description);
        imageView = findViewById(R.id.product_change_image);
        checkBox = findViewById(R.id.check_box_product_change_image);
        imageProduct = findViewById(R.id.product_change_image);

        Intent intent = getIntent();
        categoryID = intent.getStringExtra("categoryID");
        productID = intent.getStringExtra("productID");
        productName = intent.getStringExtra("productName");
        productDescription = intent.getStringExtra("productDescription");
        productPrice = intent.getDoubleExtra("productPrice",0);
        try {
            productUri = Uri.parse(intent.getStringExtra("productImageURI"));
            newImageUri = productUri;
        } catch (Exception e) {
            System.out.println("URI Alınamadı");
        }

        nameInput.getEditText().setText(productName);
        descriptionText.setText(productDescription);
        priceInput.getEditText().setText(String.valueOf(productPrice));
        if (productUri != null) {
            checkBox.setChecked(true);
            imageView.setVisibility(View.VISIBLE);
            Picasso.with(getApplicationContext())
                    .load(productUri)
                    .placeholder(R.drawable.image_selection_border)
                    .fit().centerCrop()
                    .into(imageView);
        } else {
            checkBox.setChecked(false);
            imageView.setVisibility(View.GONE);
        }

    }

    public void checkBoxChangeClick(View view) {
        if (checkBox.isChecked()) {
            imageView.setVisibility(View.VISIBLE);
        } else {
            imageView.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 111 && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            newImageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), newImageUri);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void changeProductImage(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 111);
    }

    public void changeProduct(View view) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference reference = database.getReference();

        reference.child("Companies").child(Company.getInstance().getUUID()).child("Menus").child(DepartmentConstantsClass.CURRENT_MENU_UUID)
                .child("Categories").child(categoryID).child("Products").child(productID).child("name").setValue(nameInput.getEditText().getText().toString().trim());
        reference.child("Companies").child(Company.getInstance().getUUID()).child("Menus").child(DepartmentConstantsClass.CURRENT_MENU_UUID)
                .child("Categories").child(categoryID).child("Products").child(productID).child("description").setValue(descriptionText.getText().toString().trim());
        reference.child("Companies").child(Company.getInstance().getUUID()).child("Menus").child(DepartmentConstantsClass.CURRENT_MENU_UUID)
                .child("Categories").child(categoryID).child("Products").child(productID).child("price").setValue(priceInput.getEditText().getText().toString().trim());

        if (checkBox.isChecked()) {
            if (newImageUri != null) {
                if (newImageUri == productUri) {
                    finish();
                } else {
                    String randomImageID = UUID.randomUUID().toString();
                    final StorageReference storageReference = FirebaseStorage.getInstance().getReference("products").child(randomImageID + ".");
                    storageReference.putFile(newImageUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
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
                                        .child("Categories").child(categoryID).child("Products").child(productID).child("imageUri").setValue(downloadUri.toString());
                                finish();
                            }
                        }
                    });
                }
            } else {
                StyleableToast.makeText(getApplicationContext(),"Please select an image",R.style.FailureToast).show();
                return;
            }
        } else {
            try {
                reference.child("Companies").child(Company.getInstance().getUUID()).child("Menus").child(DepartmentConstantsClass.CURRENT_MENU_UUID)
                        .child("Categories").child(categoryID).child("Products").child(productID).child("imageUri").removeValue();
            } catch (Exception e) {
                e.printStackTrace();
            }

            finish();
        }

    }
}