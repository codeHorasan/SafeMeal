package com.ugur.safemealdeneme.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.muddzdev.styleabletoastlibrary.StyleableToast;
import com.ugur.safemealdeneme.R;

public class LogInActivity extends AppCompatActivity {
    private TextInputLayout emailText, passwordText;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        mAuth = FirebaseAuth.getInstance();

        emailText = findViewById(R.id.layout_login_email_input);
        passwordText = findViewById(R.id.layout_login_password_input);
    }

    public void confirmLogin(View view) {
        String email = emailText.getEditText().getText().toString().trim();
        String password = passwordText.getEditText().getText().toString().trim();
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("Load","Yes");
                    startActivity(intent);
                    StyleableToast.makeText(getApplicationContext(),"Successful Login",R.style.SuccessToast).show();
                } else {
                    StyleableToast.makeText(getApplicationContext(),"Unsuccessful Login!",R.style.FailureToast).show();
                }
            }
        });
    }
}