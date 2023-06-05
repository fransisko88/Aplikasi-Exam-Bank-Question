package com.example.examschool.ui.home_student;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.examschool.ActivityExam;
import com.example.examschool.EditProfile;
import com.example.examschool.R;
import com.example.examschool.adapter.AdapterBankQuestion;
import com.example.examschool.adapter.AdapterBankQuestionStudent;
import com.example.examschool.model.BankQuestion;
import com.example.examschool.model.BankQuestionStudent;
import com.example.examschool.model.Question;
import com.example.examschool.ui.dashboard.DashboardViewModel;
import com.example.examschool.utils.FirebaseUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class HomeStudentFragment extends Fragment {

    private String userId,kelas;
    private TextView userName,kelasStudent;
    private ProgressBar progressBar;
    private ArrayList<BankQuestionStudent> bankQuestions;
    private RecyclerView recyclerView;
    private AdapterBankQuestionStudent adapterBankQuestion;
    private HomeStudentViewModel viewModel;
    private View view;
    private String email,fullname,phoneNumber;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_home_student, container, false);
        FloatingActionButton btnReq = view.findViewById(R.id.btnReqBankQuestion);
        userId = FirebaseUtils.getFirebaseUser().getUid();
        userName = view.findViewById(R.id.userName);
        kelasStudent = view.findViewById(R.id.kelas);
        ImageButton btnUpdateProfile = view.findViewById(R.id.btnEditProfile);
        progressBar = view.findViewById(R.id.progressBar);
        recyclerView = view.findViewById(R.id.recycler_assignment_list);

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


        btnReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(kelas.isEmpty() || kelas == ""){
                    Toast.makeText(getActivity(), "Mohon untuk mengisi kelas Anda terlebih dahulu.", Toast.LENGTH_SHORT).show();
                }else{
                    DialogFormKodeUjian();
                }
            }
        });

        return view;
    }

    @Override
    public void onStart(){
        super.onStart();

        FirebaseUtils.getFirestore().collection("users")
                .document(userId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            kelas = documentSnapshot.getString("kelas");
                            userName.setText("Nama : " + documentSnapshot.getString("fullname"));
                            kelasStudent.setText("Kelas : " + kelas );
                            fullname = documentSnapshot.getString("fullname");
                            phoneNumber = documentSnapshot.getString("phoneNumber");
                            email = documentSnapshot.getString("email");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Gagal mengambil data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        bankQuestionStudenFalse();
        bankQuestions = new ArrayList<BankQuestionStudent>();
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapterBankQuestion = new AdapterBankQuestionStudent(getActivity(),bankQuestions);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapterBankQuestion);

    }

    private void bankQuestionStudenFalse(){
        FirebaseUtils.getFirestore().collection("bankQuestionStudent")
                .whereEqualTo("studentId",FirebaseUtils.getFirebaseUser().getUid())
                .whereEqualTo("done",false)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(value != null && value.getDocumentChanges() != null ){
                            for (DocumentChange dc : value.getDocumentChanges()){
                                if(dc.getType() == DocumentChange.Type.ADDED){
                                    bankQuestions.add(dc.getDocument().toObject(BankQuestionStudent.class));
                                }
                                adapterBankQuestion.notifyDataSetChanged();
                            }
                        }
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    private void DialogFormKodeUjian() {
        AlertDialog.Builder dialog;
        LayoutInflater inflater;
        View dialogView;
        dialog = new AlertDialog.Builder(getActivity());
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.dialog_kode_ujian, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);
        dialog.setIcon(R.drawable.ic_baseline_assignment_24);
        dialog.setTitle("Ujian");

        EditText tKodeUjian = (EditText) dialogView.findViewById(R.id.txt_kode_ujian);
        dialog.setCancelable(true);

        dialog.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String kodeUjian = tKodeUjian.getText().toString();
                if (kodeUjian.isEmpty()) {
                    Toast.makeText(getContext(), "Masukkan token ujian terlebih dahulu", Toast.LENGTH_SHORT).show();
                } else {
                    cekKodeUjian(kodeUjian);
                }
            }
        });

        dialog.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    private void cekKodeUjian(String kodeUjian){
        CollectionReference bankQuestionRef = FirebaseUtils.getFirestore().collection("bankQuestion");
        bankQuestionRef.whereEqualTo("tokenQuestion", kodeUjian)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            String classRoom = document.getString("classRoom");
                            String bankQuestionId = document.getString("bankQuestionId");
                            if (classRoom != null && classRoom.equalsIgnoreCase(kelas)) {
                                progressBar.setVisibility(View.VISIBLE);
                                createQuestionStudent(bankQuestionId);
                            }
                        }
                    }else{
                        Toast.makeText(getActivity(), "Kode ujian tidak valid", Toast.LENGTH_SHORT).show();
                    }


                })
                .addOnFailureListener(e -> Toast.makeText(getActivity(), "Gagal memeriksa kode ujian: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                progressBar.setVisibility(View.GONE);

    }

    public List<Integer> generateLCCG(int totalQuestion) {
        List<Integer> questionOrder = new ArrayList<>();

        // Calculate seed value
        long seed = System.currentTimeMillis();

        // Set up LCCG parameters
        int a = 16807;
        int m = (int) Math.pow(2, 31) - 1;
        int c = 0;

        // Generate LCCG sequence
        for (int i = 0; i < totalQuestion; i++) {
            seed = (a * seed + c) % m;
            int randomNumber = (int) (seed % totalQuestion) + 1;

            // Ensure no duplicate question numbers
            if (questionOrder.contains(randomNumber)) {
                i--;
                continue;
            }

            questionOrder.add(randomNumber);
        }

        return questionOrder;
    }


    private void createQuestionStudent(String bankQuestionId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference bankQuestionStudentRef = db.collection("bankQuestionStudent");

        bankQuestionStudentRef.whereEqualTo("studentId", FirebaseUtils.getFirebaseUser().getUid())
                .whereEqualTo("bankQuestionId", bankQuestionId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult().isEmpty()) {
                            // Jika belum pernah dikerjakan, tambahkan ke collection question_student
                            CollectionReference questionRef = db.collection("question");
                            Query query = questionRef.whereEqualTo("bankQuestionId", bankQuestionId);
                            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        Integer count = task.getResult().size();
                                        List<Integer> nomorUrutList = seed(count);
                                        int index = 0;
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            Question question = document.toObject(Question.class);
                                            question.setNomorurut(nomorUrutList.get(index));
                                            question.setJawabanStudent("");
                                            FirebaseUtils.getFirestore().collection("question_student")
                                                    .document(question.getQuestionId())
                                                    .set(question)
                                                    .addOnSuccessListener(e->{})
                                                    .addOnFailureListener(e -> {});
                                            index++;
                                        }

                                        Map<String, Object> data = new HashMap<>();
                                        String id = FirebaseUtils.getUUID();
                                        data.put("bankQuestionStudentId", id);
                                        data.put("studentId", FirebaseUtils.getFirebaseUser().getUid());
                                        data.put("bankQuestionId", bankQuestionId);
                                        data.put("done", false);
                                        data.put("skor", "0");
                                        data.put("fullname", fullname);
                                        data.put("createdAt", FirebaseUtils.getCurrentDateTime());
                                        bankQuestionStudentRef
                                                .document(id)
                                                .set(data)
                                                .addOnSuccessListener(documentReference -> {})
                                                .addOnFailureListener(e -> {});

                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(getActivity(), "Selamat Ujian!", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getActivity(), ActivityExam.class);
                                        intent.putExtra("bankQuestionId", bankQuestionId);
                                        startActivity(intent);
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(getActivity(), "Maaf, mata pelajaran ini sudah dikerjakan sebelumnya.", Toast.LENGTH_SHORT).show();
                        }
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }

    @NonNull
    private static List<Integer> seed(int totalQuestions) {
        List<Integer> result = new ArrayList<>();
        for (int i = 1; i <= totalQuestions; i++) {
            result.add(i);
        }
        Random random = new Random();
        for (int i = totalQuestions - 1; i >= 1; i--) {
            int j = random.nextInt(i + 1);
            int temp = result.get(i);
            result.set(i, result.get(j));
            result.set(j, temp);
        }
        return result;
    }

}