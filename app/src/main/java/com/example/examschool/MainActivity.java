package com.example.examschool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.examschool.auth.Login;
import com.example.examschool.student.StudentActivity;
import com.example.examschool.teacher.TeacherActivity;
import com.example.examschool.utils.FirebaseUtils;
import com.google.android.gms.common.internal.service.Common;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

public class MainActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = findViewById(R.id.progressBar);

    }

    public void onStart(){
        super.onStart();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.VISIBLE);
                cekAkun();
            }
        }, 500);
    }
    protected void cekAkun() {
        if(isNetworkAvailable(MainActivity.this)) {
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            if (currentUser != null) {
                DocumentReference userRef = FirebaseUtils.getFirestore().collection("users").document(currentUser.getUid());
                userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String role = documentSnapshot.getString("role");
                        if(role.equals("STD")){
                            Intent intent = new Intent(MainActivity.this, StudentActivity.class);
                            startActivity(intent);
                            finish();
                        } else if(role.equals("TCH")){
                            Intent intent = new Intent(MainActivity.this, TeacherActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Gagal mengambil data user", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Intent intent = new Intent(this, Login.class);
                startActivity(intent);
                finish();
            }
        }else{
            Toast.makeText(this, "Tidak ada koneksi internet, silahkan periksa jaringan anda", Toast.LENGTH_SHORT).show();
        }

    }

//    private void cekakun(String username,String password){
//        if(isNetworkAvailable(MainActivity.this)){
//            DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().child(username);
//            //rootRef.child(username);
//            rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot snapshot) {
//                    if (snapshot.getValue() == null) {
//                        // The child doesn't exist
//                        startActivity(new Intent(SplashscreenActivity.this,LoginActivity.class));
//                        finish();
//                    }else{
//                        UserModel userModel = snapshot.getValue(UserModel.class);
//                        if(userModel.getPassword() != null){
//                            if(userModel.getPassword().equals(password)){
//                                Common.userModel = userModel;
//                                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(SplashscreenActivity.this);
//                                SharedPreferences.Editor editor = preferences.edit();
//                                editor.putString("username",userModel.getUsername());
//                                editor.putString("password",userModel.getPassword());
//                                editor.apply();
//                                Intent intent = new Intent(SplashscreenActivity.this, HomeActivity.class);
//                                startActivity(intent);
//                            }else{
//                                startActivity(new Intent(SplashscreenActivity.this,LoginActivity.class));
//                            }
//                        }else{
//                            startActivity(new Intent(SplashscreenActivity.this,LoginActivity.class));
//                        }
//                        finish();
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//                    Toast.makeText(SplashscreenActivity.this, ""+error.getMessage().toString(), Toast.LENGTH_SHORT).show();
//                    startActivity(new Intent(SplashscreenActivity.this,LoginActivity.class));
//                    finish();
//                }
//            });
//        }else{
//            Toast.makeText(this, "Tidak ada koneksi internet, silahkan periksa jaringan anda", Toast.LENGTH_SHORT).show();
//        }
//    }

    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}