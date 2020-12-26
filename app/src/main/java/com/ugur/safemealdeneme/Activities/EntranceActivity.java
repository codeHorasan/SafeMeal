package com.ugur.safemealdeneme.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

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
        setUpPermissions();
        if (ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            startActivity(new Intent(getApplicationContext(), ReadQRActivity.class));
        }
    }

    public void setUpPermissions() {
        int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            makeRequest();
        }
    }

    public void makeRequest() {
        ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.CAMERA},100);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults.length == 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this,"Kamera İzini Almanız Gerek!",Toast.LENGTH_LONG).show();
            } else {
                //Başarılı
                startActivity(new Intent(getApplicationContext(), ReadQRActivity.class));
            }
        }
    }
}