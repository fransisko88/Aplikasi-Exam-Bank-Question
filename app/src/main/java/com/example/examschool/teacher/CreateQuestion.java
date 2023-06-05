package com.example.examschool.teacher;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.examschool.R;
import com.example.examschool.model.Question;
import com.example.examschool.utils.FirebaseUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Timestamp;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class CreateQuestion extends AppCompatActivity {
    private String bankQuestionId,lessonName,duration,token,classRoom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_question);
        Intent data = getIntent();
        bankQuestionId = data.getStringExtra("bankQuestionId");
        lessonName = data.getStringExtra("lessonName");
        duration = data.getStringExtra("duration");
        token = data.getStringExtra("tokenQuestion");
        classRoom = data.getStringExtra("classRooom");

        Button btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createQuestion(bankQuestionId);
            }
        });
    }

    private void createQuestion(String bankId){
        TextInputEditText edt_soal_name = (TextInputEditText) findViewById(R.id.txtSoal);
        EditText edt_pilihan_a = (EditText) findViewById(R.id.edt_soal_pilihan_jawaban_a);
        EditText edt_pilihan_b = (EditText) findViewById(R.id.edt_soal_pilihan_jawaban_b);
        EditText edt_pilihan_c = (EditText) findViewById(R.id.edt_soal_pilihan_jawaban_c);
        EditText edt_pilihan_d = (EditText) findViewById(R.id.edt_soal_pilihan_jawaban_d);
        EditText edt_pilihan_e = (EditText) findViewById(R.id.edt_soal_pilihan_jawaban_e);
        RadioGroup rg_jawaban_benar = (RadioGroup) findViewById(R.id.rg_jawaban_benar);
        RadioButton jawabanA = (RadioButton) findViewById(R.id.rb_soal_jawaban_a);
        RadioButton jawabanB = (RadioButton) findViewById(R.id.rb_soal_jawaban_b);
        RadioButton jawabanC = (RadioButton) findViewById(R.id.rb_soal_jawaban_c);
        RadioButton jawabanD = (RadioButton) findViewById(R.id.rb_soal_jawaban_d);
        RadioButton jawabanE = (RadioButton) findViewById(R.id.rb_soal_jawaban_e);

        String questionId = UUID.randomUUID().toString();
        Timestamp timestamp = Timestamp.now();
        Date date = timestamp.toDate();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy HH:mm:ss", Locale.getDefault());
        String formattedDate = dateFormat.format(date);

        Question soalModel = new Question();
        soalModel.setQuestionId(questionId);
        soalModel.setBankQuestionId(bankId);
        soalModel.setTeksSoal(edt_soal_name.getText().toString());
        soalModel.setPilihanA(edt_pilihan_a.getText().toString());
        soalModel.setPilihanB(edt_pilihan_b.getText().toString());
        soalModel.setPilihanC(edt_pilihan_c.getText().toString());
        soalModel.setPilihanD(edt_pilihan_d.getText().toString());
        soalModel.setPilihanE(edt_pilihan_e.getText().toString());
        soalModel.setCreatedAt(formattedDate);
        if(jawabanA.isChecked()){
            soalModel.setJawabanBenar("A");
        }else if(jawabanB.isChecked()){
            soalModel.setJawabanBenar("B");
        }else if(jawabanC.isChecked()){
            soalModel.setJawabanBenar("C");
        }else if(jawabanD.isChecked()){
            soalModel.setJawabanBenar("D");
        } else if(jawabanE.isChecked()) {
            soalModel.setJawabanBenar("E");
        }else{
            Toast.makeText(CreateQuestion.this, "Silahkan pilih jawaban benar terlebih dahulu!", Toast.LENGTH_SHORT).show();
        }
        FirebaseUtils.getFirestore().collection("question")
                .document(soalModel.getQuestionId())
                .set(soalModel)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(CreateQuestion.this, "Soal Soal Berhasil dibuat", Toast.LENGTH_SHORT).show();
                        final Intent intent = new Intent(CreateQuestion.this, QuestionActivity.class);
                        intent.putExtra("bankQuestionId",bankQuestionId);
                        intent.putExtra("classRoom",classRoom);
                        intent.putExtra("lessonName",lessonName);
                        intent.putExtra("tokenQuestion", token);
                        intent.putExtra("duration", duration);
                        startActivity(intent);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Data gagal disimpan ke Firestore.", e);
                    }
                });
    }
}