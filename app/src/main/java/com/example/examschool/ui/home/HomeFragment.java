package com.example.examschool.ui.home;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.examschool.EditProfile;
import com.example.examschool.auth.Login;
import com.example.examschool.databinding.FragmentHomeBinding;
import com.example.examschool.utils.FirebaseUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private TextView txtName,txtTotalTeacher,txtTotalStudent;
    private CollectionReference collectionTeacher,collectionStudent;
    private String email,fullname,phoneNumber,userId,kelas;
    private Button btnUpdateProfile;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        ImageButton btnLogout = binding.btnLogout;
        txtName = binding.txtName;
        txtTotalTeacher = binding.txtTotalTeacher;
        txtTotalStudent = binding.txtTotalStudent;
        btnUpdateProfile = binding.btnViewStudent;
        btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditProfile.class);
                intent.putExtra("email", email);
                intent.putExtra("fullname", fullname);
                intent.putExtra("phoneNumber", phoneNumber);
                intent.putExtra("userId", userId);
                intent.putExtra("kelas", kelas);
                startActivity(intent);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setMessage("Ingin Keluar aplikasi ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                FirebaseUtils.logout();
                                Intent intent = new Intent(getActivity(), Login.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                getActivity().finish();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });
        View root = binding.getRoot();
        return root;
    }

    @Override
    public void onStart(){
        super.onStart();

        userId = FirebaseUtils.getFirebaseUser().getUid();
        FirebaseUtils.getFirestore().collection("users")
                .document(userId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            txtName.setText(documentSnapshot.getString("fullname"));
                            kelas = documentSnapshot.getString("kelas");
                            fullname = documentSnapshot.getString("fullname");
                            phoneNumber = documentSnapshot.getString("phoneNumber");
                            email = documentSnapshot.getString("email");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });

        collectionTeacher = FirebaseUtils.getFirestore().collection("users");
        collectionTeacher.whereEqualTo("role", "TCH").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                int count = queryDocumentSnapshots.size();
                txtTotalTeacher.setText(String.valueOf(count));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

        collectionStudent = FirebaseUtils.getFirestore().collection("users");
        collectionStudent.whereEqualTo("role", "STD").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                int count = queryDocumentSnapshots.size();
                txtTotalStudent.setText(String.valueOf(count));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}