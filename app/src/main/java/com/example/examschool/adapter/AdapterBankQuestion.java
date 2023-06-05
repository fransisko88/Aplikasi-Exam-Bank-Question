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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.example.examschool.ActivityExam;
import com.example.examschool.MainActivity;
import com.example.examschool.R;
import com.example.examschool.ResultActivity;
import com.example.examschool.auth.Login;
import com.example.examschool.model.BankQuestion;
import com.example.examschool.teacher.QuestionActivity;
import com.example.examschool.teacher.TeacherActivity;
import com.example.examschool.ui.dashboard.DashboardFragment;
import com.example.examschool.ui.dashboard.DashboardViewModel;
import com.example.examschool.utils.FirebaseUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class AdapterBankQuestion extends RecyclerView.Adapter<AdapterBankQuestion.MyViewHolder>{

    private Context context;
    private ArrayList<BankQuestion> bankQuestions;
    private DashboardViewModel viewModel;

    public AdapterBankQuestion(Context context, ArrayList<BankQuestion> bankQuestions) {
        this.context = context;
        this.bankQuestions = bankQuestions;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.card_item_bank_lesson,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        BankQuestion bankQuestion = bankQuestions.get(position);
        holder.txt_nama_ujian.setText(bankQuestion.getLessonName());
        holder.txt_kode_ujian.setText("Kode Ujian : " + bankQuestion.getTokenQuestion());
        holder.txt_kelas_ujian.setText("Kelas : " + bankQuestion.getClassRoom());
        holder.txt_durasi_ujian.setText("Durasi waltu ujian : " + (int) Double.parseDouble(bankQuestion.getDuration().toString()) + " Menit");

        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(v.getContext(), QuestionActivity.class);
                intent.putExtra("bankQuestionId",bankQuestion.getBankQuestionId());
                intent.putExtra("teacherId", bankQuestion.getTeacherId());
                intent.putExtra("classRooom", bankQuestion.getClassRoom());
                intent.putExtra("lessonName", bankQuestion.getLessonName());
                intent.putExtra("tokenQuestion", bankQuestion.getTokenQuestion());
                intent.putExtra("duration", String.valueOf(bankQuestion.getDuration()));
                context.startActivity(intent);
            }
        });

        holder.btnResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(v.getContext(), ResultActivity.class);
                intent.putExtra("bankQuestionId",bankQuestion.getBankQuestionId());
                context.startActivity(intent);
            }
        });


        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(holder.btnDelete.getContext())
                        .setMessage("Menghapus Bank Soal ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                FirebaseUtils.getFirestore().collection("bankQuestion")
                                        .whereEqualTo("bankQuestionId", bankQuestion.getBankQuestionId())
                                        .get()
                                        .addOnSuccessListener(queryDocumentSnapshots -> {
                                            for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                                                snapshot.getReference().delete();
                                            }
                                        });
                                FirebaseUtils.getFirestore().collection("question")
                                        .whereEqualTo("bankQuestionId", bankQuestion.getBankQuestionId())
                                        .get()
                                        .addOnSuccessListener(queryDocumentSnapshots -> {
                                            for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                                                snapshot.getReference().delete();
                                            }
                                        });

                                FirebaseUtils.getFirestore().collection("bankQuestionStudent")
                                        .whereEqualTo("bankQuestionId", bankQuestion.getBankQuestionId())
                                        .get()
                                        .addOnSuccessListener(queryDocumentSnapshots -> {
                                            for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                                                snapshot.getReference().delete();
                                            }
                                        });
                                FirebaseUtils.getFirestore().collection("question_student")
                                        .whereEqualTo("bankQuestionId", bankQuestion.getBankQuestionId())
                                        .get()
                                        .addOnSuccessListener(queryDocumentSnapshots -> {
                                            for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                                                snapshot.getReference().delete();
                                            }
                                        });

                                final Intent intent = new Intent(v.getContext(), TeacherActivity.class);
                                context.startActivity(intent);

                            }

                        })
                        .setNegativeButton("No", null)
                        .show();



            }
        });

    }

    @Override
    public int getItemCount() {
        return bankQuestions.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView txt_nama_ujian,txt_durasi_ujian,txt_kode_ujian,txt_kelas_ujian;
        private Button btnEdit,btnDelete,btnResult;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_nama_ujian = itemView.findViewById(R.id.txt_nama_ujian);
            txt_durasi_ujian = itemView.findViewById(R.id.txt_durasi_ujian);
            txt_kode_ujian = itemView.findViewById(R.id.txt_token_ujian);
            txt_kelas_ujian = itemView.findViewById(R.id.txt_kelas_ujian);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnResult = itemView.findViewById(R.id.btnViewStudent);
        }
    }

    private void delete(String id){

    }

}
