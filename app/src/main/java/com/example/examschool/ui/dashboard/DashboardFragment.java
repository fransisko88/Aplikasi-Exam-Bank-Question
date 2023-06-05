package com.example.examschool.ui.dashboard;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.examschool.R;
import com.example.examschool.adapter.AdapterBankQuestion;
import com.example.examschool.databinding.FragmentDashboardBinding;
import com.example.examschool.model.BankQuestion;
import com.example.examschool.model.BankQuestionStudent;
import com.example.examschool.utils.FirebaseUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private ArrayList<BankQuestion> bankQuestions;
    private RecyclerView recyclerView;
    private AdapterBankQuestion adapterBankQuestion;
    private DashboardViewModel viewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        DashboardViewModel dashboardViewModel =
//                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        recyclerView = binding.recyclerBankLessonList;

        FloatingActionButton btnAddBank = binding.btnAddBankQuestion;
        btnAddBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddDialog();
            }
        });

        return root;
    }

    @Override
    public void onStart(){
        super.onStart();
        list();
        bankQuestions = new ArrayList<BankQuestion>();
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapterBankQuestion = new AdapterBankQuestion(getActivity(),bankQuestions);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapterBankQuestion);
    }

    private void list(){
        FirebaseUtils.getFirestore().collection("bankQuestion")
                .whereEqualTo("teacherId",FirebaseUtils.getFirebaseUser().getUid())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(value != null && value.getDocumentChanges() != null ){
                            for (DocumentChange dc : value.getDocumentChanges()){
                                if(dc.getType() == DocumentChange.Type.ADDED){
                                    bankQuestions.add(dc.getDocument().toObject(BankQuestion.class));
                                }
                                adapterBankQuestion.notifyDataSetChanged();
                            }
                        }
                    }
                });
    }


    private void showAddDialog() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getContext());
        builder.setTitle("Tambah Data Ujian");
        builder.setMessage("Silahkan isi informasi dibawah ini...");

        View itemView = LayoutInflater.from(getContext()).inflate(R.layout.layout_update_bank_soal,null);
        EditText edt_nama = (EditText) itemView.findViewById(R.id.edt_nama);
        EditText edt_kelas = (EditText) itemView.findViewById(R.id.edt_kelas);
        EditText edt_durasi = (EditText) itemView.findViewById(R.id.edt_durasi);

        builder.setNegativeButton("Batal", (dialogInterface, i) -> dialogInterface.dismiss());
        builder.setPositiveButton("Tambah", (dialogInterface, i) -> {

            if(edt_nama.getText().toString().isEmpty() || edt_durasi.getText().toString().isEmpty() || edt_kelas.getText().toString().isEmpty()){
                Toast.makeText(getActivity(), "Data ujian tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                return;
            }
            String bankId = UUID.randomUUID().toString();
            Timestamp timestamp = Timestamp.now();
            Date date = timestamp.toDate();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy HH:mm:ss", Locale.getDefault());
            String formattedDate = dateFormat.format(date);
            String teacherId = FirebaseUtils.getFirebaseUser().getUid();
            final BankQuestion bank = new BankQuestion();
            bank.setBankQuestionId(bankId);
            bank.setClassRoom(edt_kelas.getText().toString());
            bank.setLessonName(edt_nama.getText().toString());
            bank.setDuration(Double.parseDouble(edt_durasi.getText().toString()));
            bank.setCreatedAt(formattedDate);
            bank.setTeacherId(teacherId);
            bank.setTokenQuestion(generatedToken());


                FirebaseUtils.getFirestore().collection("bankQuestion")
                        .document(bank.getBankQuestionId())
                        .set(bank)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getActivity(), "Bank Soal Berhasil dibuat", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Data gagal disimpan ke Firestore.", e);
                            }
                        });

            });

        builder.setView(itemView);
        androidx.appcompat.app.AlertDialog dialog = builder.create();
        dialog.show();
    }

    public String generatedToken() {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 5;
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int)
                    (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        String generatedString = buffer.toString().toUpperCase();
        return generatedString;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}