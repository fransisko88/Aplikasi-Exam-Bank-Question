package com.example.examschool.teacher;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.examschool.R;
import com.example.examschool.adapter.AdapterBankQuestion;
import com.example.examschool.adapter.AdapterQuestionTeacher;
import com.example.examschool.model.BankQuestion;
import com.example.examschool.model.Question;
import com.example.examschool.utils.FirebaseUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class QuestionActivity extends AppCompatActivity {
    private String bankQuestionId,lessonName,duration,token,classRoom;
    private ArrayList<Question> questions;
    private RecyclerView recyclerView;
    private AdapterQuestionTeacher adapterQuestionTeacher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        recyclerView = findViewById(R.id.recycler_question_list);

        TextView txtExamName = findViewById(R.id.txtExamName);
        TextInputEditText txtClassroom = findViewById(R.id.txtClassRoom);
        TextInputEditText txtToken = findViewById(R.id.txtToken);
        TextInputEditText txtDuration = findViewById(R.id.txtDurasi);


        Intent data = getIntent();
        bankQuestionId = data.getStringExtra("bankQuestionId");
        lessonName = data.getStringExtra("lessonName");
        duration = data.getStringExtra("duration");
        token = data.getStringExtra("tokenQuestion");
        classRoom = data.getStringExtra("classRooom");

        txtExamName.setText(lessonName + " ");
        txtClassroom.setText(classRoom);
        txtToken.setText(token);
        txtDuration.setText(duration);

        ImageButton btnBank = findViewById(R.id.btnBack);
        btnBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QuestionActivity.super.onBackPressed();
            }
        });

        FloatingActionButton btnAddQuestion = findViewById(R.id.btnAddQuestion);
        btnAddQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(v.getContext(), CreateQuestion.class);
                intent.putExtra("bankQuestionId",bankQuestionId);
                intent.putExtra("classRooom",classRoom);
                intent.putExtra("lessonName",lessonName);
                intent.putExtra("tokenQuestion", token);
                intent.putExtra("duration", duration);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    protected void onStart(){
        super.onStart();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(QuestionActivity.this));
        questions = new ArrayList<Question>();
        listQuestion();
        adapterQuestionTeacher = new AdapterQuestionTeacher(QuestionActivity.this,questions);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapterQuestionTeacher);
    }

    private void listQuestion(){
        FirebaseUtils.getFirestore().collection("question").whereEqualTo("bankQuestionId",bankQuestionId).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(value != null && value.getDocumentChanges() != null ){
                    for (DocumentChange dc : value.getDocumentChanges()){
                        if(dc.getType() == DocumentChange.Type.ADDED){
                            questions.add(dc.getDocument().toObject(Question.class));
                        }
                        adapterQuestionTeacher.notifyDataSetChanged();
                    }
                }
            }
        });
    }

}