package com.example.examschool;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.examschool.adapter.AdapterBankQuestion;
import com.example.examschool.adapter.AdapterBankQuestionStudent;
import com.example.examschool.adapter.AdapterResult;
import com.example.examschool.model.BankQuestion;
import com.example.examschool.model.BankQuestionStudent;
import com.example.examschool.utils.FirebaseUtils;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity {

    private ArrayList<BankQuestionStudent> bankQuestions;
    private RecyclerView recyclerView;
    private AdapterResult adapterBankQuestion;
    private String bankQuestionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Intent intent = getIntent();
        bankQuestionId = intent.getStringExtra("bankQuestionId");
        recyclerView = findViewById(R.id.recycler_assignment_list);
    }

    @Override
    public void onStart(){
        super.onStart();
        list(bankQuestionId);
        bankQuestions = new ArrayList<BankQuestionStudent>();
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapterBankQuestion = new AdapterResult(this,bankQuestions);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapterBankQuestion);
    }

    private void list(String bankQuestionId){
        FirebaseUtils.getFirestore().collection("bankQuestionStudent")
                .whereEqualTo("bankQuestionId",bankQuestionId)
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
}