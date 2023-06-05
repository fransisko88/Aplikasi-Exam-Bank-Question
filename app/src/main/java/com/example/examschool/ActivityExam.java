package com.example.examschool;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import com.example.examschool.model.Question;
import com.example.examschool.student.StudentActivity;
import com.example.examschool.teacher.TeacherActivity;
import com.example.examschool.ui.finished.FinishedFragment;
import com.example.examschool.ui.home_student.HomeStudentFragment;
import com.example.examschool.ui.home_student.HomeStudentViewModel;
import com.example.examschool.utils.FirebaseUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ActivityExam extends AppCompatActivity {
    private Intent intent;
    private String count,bankQuestionId,time;
    private TextView noSoal,txt_soal,textViewTimer;
    private ConstraintLayout btnNext;
    private int currentQuestionIndex = 0;
    private int questionListSize;
    private RadioButton rbA,rbB,rbC,rbD,rbE;
    private ProgressBar progressBar;
    private List<Question> questionStudentList = new ArrayList<>();
    private CountDownTimer countDownTimer;
    private Integer skorStudent = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);
        noSoal = findViewById(R.id.textView2);
        btnNext = findViewById(R.id.btnNext);
        txt_soal = findViewById(R.id.txt_soal);
        rbA = findViewById(R.id.rb_pilihan_jawaban_a);
        rbB = findViewById(R.id.rb_pilihan_jawaban_b);
        rbC = findViewById(R.id.rb_pilihan_jawaban_c);
        rbD = findViewById(R.id.rb_pilihan_jawaban_d);
        rbE = findViewById(R.id.rb_pilihan_jawaban_e);
        progressBar = findViewById(R.id.progressBar);
        textViewTimer = findViewById(R.id.textViewTimer);

        intent = getIntent();
        count = intent.getStringExtra("count");
        bankQuestionId = intent.getStringExtra("bankQuestionId");
        time = intent.getStringExtra("time");

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentQuestionIndex++;
                progressBar.setVisibility(View.VISIBLE);
                if (currentQuestionIndex >= questionListSize) {
                    progressBar.setVisibility(View.GONE);
                    AlertDialog.Builder builder = new AlertDialog.Builder(ActivityExam.this);
                    builder.setTitle("Pengumpulan");
                    builder.setMessage("Apakah Anda ingin mengakhiri ujian ?");
                    builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            done(bankQuestionId);
                            Intent intent = new Intent(ActivityExam.this, StudentActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            dialog.dismiss();
                            finish();
                        }
                    });
                    builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            displayQuestion(currentQuestionIndex);
                            progressBar.setVisibility(View.GONE);
                        }
                    },1000);
                }
            }
        });

        rbA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setJawabanStudent(questionStudentList.get(currentQuestionIndex).getQuestionId(),"A");
                setSkor("A",questionStudentList.get(currentQuestionIndex).getJawabanBenar());
            }
        });

        rbB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setJawabanStudent(questionStudentList.get(currentQuestionIndex).getQuestionId(),"B");
                setSkor("B",questionStudentList.get(currentQuestionIndex).getJawabanBenar());
            }
        });

        rbC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setJawabanStudent(questionStudentList.get(currentQuestionIndex).getQuestionId(),"C");
                setSkor("C",questionStudentList.get(currentQuestionIndex).getJawabanBenar());
            }
        });

        rbD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setJawabanStudent(questionStudentList.get(currentQuestionIndex).getQuestionId(),"D");
                setSkor("D",questionStudentList.get(currentQuestionIndex).getJawabanBenar());
            }
        });
        rbE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setJawabanStudent(questionStudentList.get(currentQuestionIndex).getQuestionId(),"E");
                setSkor("E",questionStudentList.get(currentQuestionIndex).getJawabanBenar());
            }
        });

        ImageButton btnBank = findViewById(R.id.btnBack);
        btnBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityExam.super.onBackPressed();
            }
        });

    }

    @Override
    protected void onStart(){
        super.onStart();
        showDialog();
//        startCountdown(time);
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Konfirmasi Ujian");
        builder.setMessage("Apakah Anda siap untuk memulai ujian?");
        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                getQuestion(bankQuestionId);
            }
        });
        builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ActivityExam.super.onBackPressed();
                dialog.dismiss();
                finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void getQuestion(String bankQuestionId){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference questionStudentRef = db.collection("question_student");

        questionStudentRef.whereEqualTo("bankQuestionId", bankQuestionId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot querySnapshot) {

                        for (QueryDocumentSnapshot document : querySnapshot) {
                            Question questionStudent = document.toObject(Question.class);
                            questionStudentList.add(questionStudent);
                        }
                        questionListSize = questionStudentList.size();
                        progressBar.setVisibility(View.VISIBLE);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                displayQuestion(currentQuestionIndex);
                                progressBar.setVisibility(View.GONE);
                            }
                        },500);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

    }

    private void setSkor(String jawabanStudent,String jawabanBenar){
        if(jawabanBenar.equalsIgnoreCase(jawabanStudent)){
            skorStudent += 5;
        }
    }

    private void displayQuestion(int index) {
        txt_soal.setText(questionStudentList.get(index).getTeksSoal());
        noSoal.setText("Soal " + (currentQuestionIndex + 1) + "/" + String.valueOf(count));
        rbA.setText("A. " + questionStudentList.get(index).getPilihanA());
        rbB.setText("B. " + questionStudentList.get(index).getPilihanB());
        rbC.setText("C. " + questionStudentList.get(index).getPilihanC());
        rbD.setText("D. " + questionStudentList.get(index).getPilihanD());
        rbE.setText("E. " + questionStudentList.get(index).getPilihanD());

        String jawaban = questionStudentList.get(index).getJawabanStudent();
        switch (jawaban) {
            case "A":
                rbA.setChecked(true);
                break;
            case "B":
                rbB.setChecked(true);
                break;
            case "C":
                rbC.setChecked(true);
                break;
            case "D":
                rbD.setChecked(true);
                break;
            case "E":
                rbE.setChecked(true);
                break;
        }

    }

    private void setJawabanStudent(String questionId,String jawaban){
        FirebaseFirestore db = FirebaseUtils.getFirestore();
        db.collection("question_student")
                .whereEqualTo("questionId", questionId)
                .whereEqualTo("bankQuestionId", bankQuestionId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            db.collection("question_student").document(questionId)
                                    .update("jawabanStudent", jawaban)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            // update berhasil
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // update gagal
                                        }
                                    });
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // error saat query data
                    }
                });

    }

    private void startCountdown(String time) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        Date endDate = null;
        try {
            endDate = dateFormat.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long endTime = endDate.getTime() + (60 * 60 * 1000); // tambahkan 60 menit
        long currentTime = System.currentTimeMillis();

        long timeDiff = endTime - currentTime;

        countDownTimer = new CountDownTimer(timeDiff, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long days = TimeUnit.MILLISECONDS.toDays(millisUntilFinished);
                long hours = TimeUnit.MILLISECONDS.toHours(millisUntilFinished) - TimeUnit.DAYS.toHours(days);
                long minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(hours) - TimeUnit.DAYS.toMinutes(days);
                long seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(minutes) - TimeUnit.HOURS.toSeconds(hours) - TimeUnit.DAYS.toSeconds(days);

                String countdownText = String.format("%02d:%02d:%02d:%02d", days, hours, minutes, seconds);
                textViewTimer.setText("Waktu : " + countdownText);
            }

            @Override
            public void onFinish() {
                textViewTimer.setText("Waktu Habis!");
                Toast.makeText(ActivityExam.this, "Ujian Berakhir!", Toast.LENGTH_SHORT).show();
                finish();
            }
        };

        countDownTimer.start();
    }

    private void done(String bankQuestionId ){
        FirebaseUtils.getFirestore().collection("bankQuestionStudent")
                .whereEqualTo("bankQuestionId", bankQuestionId)
                .whereEqualTo("studentId", FirebaseUtils.getFirebaseUser().getUid())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            FirebaseUtils.getFirestore().collection("bankQuestionStudent")
                                    .document(document.getId())
                                    .update("done", true, "skor", String.valueOf(skorStudent))
                                    .addOnSuccessListener(aVoid -> Log.d(TAG, "DocumentSnapshot successfully updated!"))
                                    .addOnFailureListener(e -> Log.w(TAG, "Error updating document", e));
                        }
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });

        HomeStudentViewModel viewModel = new ViewModelProvider(this).get(HomeStudentViewModel.class);
        viewModel.loadBankQuestions();
        viewModel.loadBankQuestionsFinish();
        startActivity(new Intent(ActivityExam.this,ActivityExam.class));
        finish();
    }

    private void stopCountdown() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }


}