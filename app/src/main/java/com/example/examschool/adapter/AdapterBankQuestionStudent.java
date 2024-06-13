package com.example.examschool.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.examschool.ActivityExam;
import com.example.examschool.R;
import com.example.examschool.model.BankQuestion;
import com.example.examschool.model.BankQuestionStudent;
import com.example.examschool.model.Users;
import com.example.examschool.teacher.QuestionActivity;
import com.example.examschool.teacher.TeacherActivity;
import com.example.examschool.ui.dashboard.DashboardViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AdapterBankQuestionStudent extends RecyclerView.Adapter<AdapterBankQuestionStudent.MyViewHolder>{

    private Context context;
    private List<BankQuestionStudent> bankQuestions;
    private DashboardViewModel viewModel;
    private View v;
    public AdapterBankQuestionStudent(Context context, List<BankQuestionStudent> bankQuestions) {
        this.context = context;
        this.bankQuestions = bankQuestions;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        v = LayoutInflater.from(context).inflate(R.layout.layout_bank_question_student,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        BankQuestionStudent bankQuestion = bankQuestions.get(position);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference bankQuestionRef = db.collection("bankQuestion");
        Query query = bankQuestionRef.whereEqualTo("bankQuestionId", bankQuestion.getBankQuestionId());
        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    BankQuestion data = document.toObject(BankQuestion.class);
                    FirebaseFirestore db1 = FirebaseFirestore.getInstance();
                    CollectionReference userRef = db1.collection("users");
                    Query query1 = userRef.whereEqualTo("userId", data.getTeacherId());
                    query1.get().addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            for (QueryDocumentSnapshot document1 : task1.getResult()) {
                                Users data1 = document1.toObject(Users.class);
                                holder.txtTeacherName.setText("Nama guru : " + data1.getFullname());
                                holder.txtLessonName.setText(data.getLessonName());

                                if (!bankQuestion.getDone()) {
                                    holder.txtDuration.setText((int) Double.parseDouble(data.getDuration().toString()) + " Menit");
                                    holder.txtSkor.setVisibility(View.GONE);
                                    holder.txtSkor.setText("");
                                } else {
                                    holder.txtDuration.setVisibility(View.GONE);
                                    holder.txtSkor.setVisibility(View.VISIBLE);
                                    holder.txtCreated.setText("Tanggal / Waktu : " + bankQuestion.getCreatedAt());
                                    holder.linearClik.setClickable(false);

                                    CollectionReference questionStudentRef = db.collection("question_student");
                                    Query query2 = questionStudentRef
                                            .whereEqualTo("bankQuestionId", bankQuestion.getBankQuestionId());
                                    query2.get().addOnCompleteListener(task2 -> {
                                        if (task2.isSuccessful()) {
                                            int skor = 0;
                                            int totalSoal = 0;
                                            for (QueryDocumentSnapshot document2 : task2.getResult()) {
                                                String jawabanBenar = document2.getString("jawabanBenar");
                                                String jawabanStudent = document2.getString("jawabanStudent");

                                                // Periksa apakah jawabanStudent sama dengan jawabanBenar
                                                if (jawabanBenar != null && jawabanStudent != null && !jawabanStudent.equals(jawabanBenar)) {
                                                    skor++;
                                                }
                                                totalSoal++;
                                            }
                                            double persentase = ((double) skor / totalSoal) * 100;
                                            int roundedPersentase = (int) Math.round(persentase);
                                            holder.txtSkor.setText("Skor Anda : " + roundedPersentase );
                                        }
                                    });
                                }
                            }
                        }
                    });
                }
            }
        });


        holder.linearClik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                CollectionReference questionStudentRef = db.collection("question_student");
                Query query = questionStudentRef.whereEqualTo("bankQuestionId", bankQuestion.getBankQuestionId());
                query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int count = task.getResult().size();
                            final Intent intent = new Intent(v.getContext(), ActivityExam.class);
                            intent.putExtra("bankQuestionId",bankQuestion.getBankQuestionId());
                            intent.putExtra("count",String.valueOf(count));
                            intent.putExtra("time",bankQuestion.getCreatedAt());
                            context.startActivity(intent);
                        }
                    }
                });


            }
        });

    }

    @Override
    public int getItemCount() {
        return bankQuestions.size();
    }

    public void setBank(List<BankQuestionStudent> list){
        bankQuestions = list;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView txtLessonName,txtTeacherName,txtCreated,txtDuration,txtSkor;
        private LinearLayout linearClik;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtLessonName = itemView.findViewById(R.id.txtLessonName);
            txtTeacherName = itemView.findViewById(R.id.txtTeacherName);
            txtDuration = itemView.findViewById(R.id.txtDuration);
            txtCreated = itemView.findViewById(R.id.txtCreated);
            linearClik = itemView.findViewById(R.id.linearClik);
            txtSkor = itemView.findViewById(R.id.txtSkor);
        }
    }
}
