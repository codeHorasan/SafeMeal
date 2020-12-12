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
import android.widget.ImageView;

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
import com.ugur.safemealdeneme.R;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class CategoryAdditionActivity extends AppCompatActivity {
    private TextInputLayout inputText;
    private ImageView imageView;
    Uri imageUri;

    public static String name;
    public static String uuid;

    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_addition);

        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        uuid = intent.getStringExtra("uuid");

        inputText = findViewById(R.id.category_addition_name);
        imageView = findViewById(R.id.category_addition_image);

        getSupportActionBar().setTitle("Add Category to " + name);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setImageView(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    public void addCategory(View view) {
        if (inputText.getEditText().getText().toString().trim().equals("")) {
            inputText.setError("Please give a name to your category");
            return;
        }

        final String categoryName = inputText.getEditText().getText().toString().trim();
        final String categoryUUID = UUID.randomUUID().toString();

        final StorageReference storageReference = FirebaseStorage.getInstance().getReference("Categories").child(categoryUUID + ".");
        //Checking if image is the default
        if (imageUri == null) {
            imageUri = Uri.parse("android.resource://com.ugur.safemealdeneme/" + R.drawable.logo);
        }
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
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
                    String currentDateTime = sdf.format(new Date());
                    //Category category = new Category(categoryName, downloadUri, categoryUUID);

                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference reference = database.getReference();
                    reference.child("Companies").child(Company.getInstance().getUUID()).child("Menus").child(uuid)
                            .child("Categories").child(categoryUUID).child("name").setValue(categoryName);

                    reference.child("Companies").child(Company.getInstance().getUUID()).child("Menus").child(uuid)
                            .child("Categories").child(categoryUUID).child("imageUri").setValue(downloadUri.toString());

                    reference.child("Companies").child(Company.getInstance().getUUID()).child("Menus").child(uuid)
                            .child("Categories").child(categoryUUID).child("DateTime").setValue(currentDateTime);

                    finish();

                } else {
                    StyleableToast.makeText(getApplicationContext(), "Error!", R.style.FailureToast).show();
                }
            }
        });
    }
}