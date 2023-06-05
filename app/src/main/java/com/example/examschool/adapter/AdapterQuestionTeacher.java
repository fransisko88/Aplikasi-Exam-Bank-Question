package com.example.examschool.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.examschool.R;
import com.example.examschool.model.BankQuestion;
import com.example.examschool.model.Question;
import com.example.examschool.teacher.QuestionActivity;

import java.util.ArrayList;

public class AdapterQuestionTeacher extends RecyclerView.Adapter<AdapterQuestionTeacher.MyViewHolder>{

    private Context context;
    private ArrayList<Question> questions;

    public AdapterQuestionTeacher(Context context, ArrayList<Question> questions) {
        this.context = context;
        this.questions = questions;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.layout_soal_items,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Question question = questions.get(position);
        holder.txt_teks_soal.setText(question.getTeksSoal());
        holder.txt_jawaban.setText("Pilihan : \n "
                        + "  A. " + question.getPilihanA() + "\n "
                        + "  B. " + question.getPilihanB() + "\n "
                        + "  C. " + question.getPilihanC() + "\n "
                        + "  D. " + question.getPilihanD() + "\n "
                        + "  E. " + question.getPilihanE() + "\n "
                        + "Jawaban Benar :  " + question.getJawabanBenar()
                );
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView txt_teks_soal,txt_jawaban;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_teks_soal = itemView.findViewById(R.id.txt_teks_soal);
            txt_jawaban = itemView.findViewById(R.id.txt_jawaban);
        }
    }

}
