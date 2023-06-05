package com.example.examschool.ui.finished;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.examschool.R;
import com.example.examschool.adapter.AdapterBankQuestionStudent;
import com.example.examschool.databinding.FragmentFinishedBinding;
import com.example.examschool.model.BankQuestionStudent;
import com.example.examschool.ui.home_student.HomeStudentViewModel;
import com.example.examschool.utils.FirebaseUtils;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class FinishedFragment extends Fragment {


    private View view;
    private ArrayList<BankQuestionStudent> bankQuestions;
    private RecyclerView recyclerView;
    private AdapterBankQuestionStudent adapterBankQuestion;
    private HomeStudentViewModel viewModel;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_finished, container, false);
        recyclerView = view.findViewById(R.id.recycler_assignment_list);
        return view;
    }

    @Override
    public void onStart(){
        super.onStart();

        bankQuestions = new ArrayList<BankQuestionStudent>();
        bankQuestionStudenTrue();
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapterBankQuestion = new AdapterBankQuestionStudent(getActivity(),bankQuestions);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapterBankQuestion);

    }

    private void bankQuestionStudenTrue(){
        FirebaseUtils.getFirestore().collection("bankQuestionStudent")
                .whereEqualTo("studentId",FirebaseUtils.getFirebaseUser().getUid())
                .whereEqualTo("done",true)
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
}