package com.example.examschool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.examschool.auth.Login;
import com.example.examschool.utils.FirebaseUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditProfile extends AppCompatActivity {
    private TextInputEditText txtEmail, txtFullname, txtKelas, txtPhoneNumber;
    private TextView btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        txtEmail = findViewById(R.id.txtEmail);
        txtFullname = findViewById(R.id.txtFullname);
        txtKelas = findViewById(R.id.txtKelas);
        txtPhoneNumber = findViewById(R.id.txtPhoneNumber);
        btnSave = findViewById(R.id.btnSave);

        Intent intent = getIntent();
        String email = intent.getStringExtra("email");
        String fullname = intent.getStringExtra("fullname");
        String phoneNumber = intent.getStringExtra("phoneNumber");
        String userId = intent.getStringExtra("userId");
        String kelas = intent.getStringExtra("kelas");

        txtEmail.setText(email);
        txtFullname.setText(fullname);
        txtKelas.setText(kelas);
        txtPhoneNumber.setText(phoneNumber);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("users").document(userId)
                        .update("email", txtEmail.getText().toString(),
                                "fullname", txtFullname.getText().toString(),
                                "kelas", txtKelas.getText().toString(),
                                "phoneNumber", txtPhoneNumber.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(EditProfile.this, "Sukses Update Profile", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });

            }
        });

        Button btnLogut = findViewById(R.id.btnLogOut);
        btnLogut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(EditProfile.this)
                        .setMessage("Ingin Keluar aplikasi ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                FirebaseUtils.logout();
                                Intent intent = new Intent(EditProfile.this, Login.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditProfile.super.onBackPressed();
                finish();
            }
        });
    }
}