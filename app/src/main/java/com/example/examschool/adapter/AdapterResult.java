package com.example.examschool.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.examschool.ActivityExam;
import com.example.examschool.R;
import com.example.examschool.model.BankQuestion;
import com.example.examschool.model.BankQuestionStudent;
import com.example.examschool.model.Users;
import com.example.examschool.ui.dashboard.DashboardViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class AdapterResult extends RecyclerView.Adapter<AdapterResult.MyViewHolder>{

    private Context context;
    private List<BankQuestionStudent> bankQuestions;
    private DashboardViewModel viewModel;
    private View v;

    public AdapterResult(Context context, List<BankQuestionStudent> bankQuestions) {
        this.context = context;
        this.bankQuestions = bankQuestions;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        v = LayoutInflater.from(context).inflate(R.layout.result_item,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        BankQuestionStudent bankQuestion = bankQuestions.get(position);
        holder.txtLessonName.setText(bankQuestion.getFullname());
        holder.txtSkor.setText("Nilai : " +bankQuestion.getSkor());
    }

    @Override
    public int getItemCount() {
        return bankQuestions.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView txtLessonName,txtSkor;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtLessonName = itemView.findViewById(R.id.nama);
            txtSkor = itemView.findViewById(R.id.txtDuration);
        }
    }
}
