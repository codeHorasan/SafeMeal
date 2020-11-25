package com.ugur.safemealdeneme.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ugur.safemealdeneme.R;

public class EntranceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrance);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtra("Load","Yes");
            startActivity(intent);
        }
    }

    public void clickedSignUp(View view) {
        startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
    }

    public void clickedLogIn(View view) {
        startActivity(new Intent(getApplicationContext(), LogInActivity.class));
    }

    public void clickedReadQR(View view) {
        startActivity(new Intent(getApplicationContext(), ReadQRActivity.class));
    }
}