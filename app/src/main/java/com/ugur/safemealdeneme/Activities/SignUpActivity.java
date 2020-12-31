package com.ugur.safemealdeneme.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.muddzdev.styleabletoastlibrary.StyleableToast;
import com.ugur.safemealdeneme.Classes.Company;
import com.ugur.safemealdeneme.Classes.UploadImage;
import com.ugur.safemealdeneme.R;

public class SignUpActivity extends AppCompatActivity {
    private TextInputLayout emailText, passwordText, pwControlText, companyText;
    private FirebaseAuth mAuth;
    private TextView textViewImageSelection;
    private static final int PICK_IMAGE_REQUEST = 1;

    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();

        emailText = findViewById(R.id.text_input_email);
        passwordText = findViewById(R.id.text_input_password);
        pwControlText = findViewById(R.id.text_input_password_control);
        companyText = findViewById(R.id.text_input_company_name);
        textViewImageSelection = findViewById(R.id.text_view_image_selection);

        textViewImageSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

    }

    public void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    public void checkBoxClick(View view) {
        CheckBox checkBox = (CheckBox) view;
        if (checkBox.isChecked()) {
            textViewImageSelection.setVisibility(View.VISIBLE);
        } else {
            textViewImageSelection.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            Company.getInstance().setImageUri(data.getData());
            textViewImageSelection.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.border_selected));
            textViewImageSelection.setTextColor(Color.GREEN);
            textViewImageSelection.setText("Image Selected!");
        }
    }

    private boolean validateEmail() {
        String emailInput = emailText.getEditText().getText().toString().trim();

        if (emailInput.isEmpty()) {
            emailText.setError("Email is empty!");
            return false;
        } else {
            emailText.setError(null);
            return true;
        }
    }

    private boolean validatePassword() {
        String passwordInput = passwordText.getEditText().getText().toString().trim();

        if (passwordInput.isEmpty()) {
            passwordText.setError("Password is empty!");
            return false;
        } else {
            passwordText.setError(null);
            return true;
        }
    }

    private boolean validatePasswordControl() {
        String passwordInput = pwControlText.getEditText().getText().toString().trim();

        if (!passwordInput.matches(passwordText.getEditText().getText().toString().trim())) {
            pwControlText.setError("Passwords are not the same!");
            return false;
        } else if (passwordInput.isEmpty()) {
            pwControlText.setError("Password control is empty!");
            return false;
        } else {
            pwControlText.setError(null);
            return true;
        }
    }

    private boolean validateCompanyName() {
        String companyInput = companyText.getEditText().getText().toString().trim();

        if (companyInput.isEmpty()) {
            return false;
        } else if (companyInput.length() > 30) {
            return false;
        } else {
            companyText.setError(null);
            return true;
        }
    }

    public void confirmInput(View view) {
        if (!validateEmail() | !validatePassword() | !validatePasswordControl() | !validateCompanyName()) {
            return;
        }

        mAuth.createUserWithEmailAndPassword(emailText.getEditText().getText().toString().trim(), passwordText.getEditText().getText().toString().trim())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Company company = Company.getInstance();
                            FirebaseUser user = mAuth.getCurrentUser();
                            company.setEmail(user.getEmail());
                            company.setName(companyText.getEditText().getText().toString().trim());
                            company.setUUID(user.getUid());

                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference reference = database.getReference();
                            reference.child("Companies").child(company.getUUID()).child("Email").setValue(company.getEmail());
                            reference.child("Companies").child(company.getUUID()).child("Name").setValue(company.getName());

                            storageReference = FirebaseStorage.getInstance().getReference("logos").child(company.getUUID() + ".");
                            if (Company.getInstance().getImageUri() != null) {
                                storageReference.putFile(Company.getInstance().getImageUri()).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
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

                                            UploadImage uploadImage = new UploadImage(Company.getInstance().getUUID(), downloadUri.toString());
                                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                                            DatabaseReference reference = database.getReference();
                                            reference.child("Companies").child(Company.getInstance().getUUID()).child("LogoUri").setValue(uploadImage.getImageUri());
                                        }
                                    }
                                });
                            }

                            StyleableToast.makeText(getApplicationContext(),"Signed up successfully!", R.style.SuccessToast).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        } else {
                          StyleableToast.makeText(getApplicationContext(),"Error!", R.style.FailureToast).show();
                            System.out.println("Sebep: " + task.getException().getLocalizedMessage() + task.getException().getMessage());
                        }
                    }
                });
    }
}