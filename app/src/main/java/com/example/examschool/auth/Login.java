package com.example.examschool.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.examschool.R;
import com.example.examschool.student.StudentActivity;
import com.example.examschool.teacher.TeacherActivity;
import com.example.examschool.utils.FirebaseUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        TextInputEditText txtEmail = findViewById(R.id.txtEmail);
        TextInputEditText txtPassword = findViewById(R.id.txtPassword);
        ProgressBar progressBar = findViewById(R.id.progressBar);

        TextView txtSigUp = (TextView) findViewById(R.id.txtSignUp);
        txtSigUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Register.class));
                finish();
            }
        });

        Button onSignIn = (Button) findViewById(R.id.btnLogin);
        onSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = txtEmail.getText().toString().trim();
                String password = txtPassword.getText().toString().trim();
                if(TextUtils.isEmpty(email)){
                    txtEmail.setError("Email cannot be empty!");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    txtPassword.setError("Password cannot be empty");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                FirebaseUtils.getFirebaseAuth().signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                    FirebaseFirestore.getInstance().collection("users").document(uid)
                                            .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        DocumentSnapshot document = task.getResult();
                                                        if (document.exists()) {
                                                            String role = document.getString("role");
                                                            if (role != null) {
                                                                if (role.equals("STD")) {
                                                                    startActivity(new Intent(Login.this, StudentActivity.class));
                                                                } else if (role.equals("TCH")) {
                                                                    startActivity(new Intent(Login.this, TeacherActivity.class));
                                                                } else {
                                                                    Toast.makeText(Login.this, "Invalid role", Toast.LENGTH_SHORT).show();
                                                                }
                                                                finish();
                                                            } else {
                                                                Toast.makeText(Login.this, "Role not found", Toast.LENGTH_SHORT).show();
                                                                progressBar.setVisibility(View.GONE);
                                                            }
                                                        } else {
                                                            Toast.makeText(Login.this, "User not found", Toast.LENGTH_SHORT).show();
                                                            progressBar.setVisibility(View.GONE);
                                                        }
                                                    } else {
                                                        // Error occurred
                                                        Toast.makeText(Login.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                        progressBar.setVisibility(View.GONE);
                                                    }
                                                }
                                            });
                                } else {
                                    Toast.makeText(Login.this, "Gagal ! Password Salah ", Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);
                                }
                            }
                        });

            }
        });


    }
}