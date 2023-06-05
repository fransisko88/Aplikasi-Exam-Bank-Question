package com.example.examschool.ui.dashboard;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.examschool.model.BankQuestion;
import com.example.examschool.utils.FirebaseUtils;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class DashboardViewModel extends ViewModel {

    private MutableLiveData<List<BankQuestion>> bankQuestions;
    private FirebaseFirestore db;

    public DashboardViewModel() {
        bankQuestions = new MutableLiveData<>();
        db = FirebaseUtils.getFirestore();
    }

    public LiveData<List<BankQuestion>> getBankQuestions() {
        return bankQuestions;
    }

    public void loadBankQuestions() {
        final String teacherId = FirebaseUtils.getFirebaseUser().getUid();
        List<BankQuestion> bank = new ArrayList<>();
        FirebaseUtils.getFirestore().collection("bankQuestion")
                .whereEqualTo("teacherId",teacherId)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(value != null && value.getDocumentChanges() != null ){

                            for (DocumentChange dc : value.getDocumentChanges()){
                                if(dc.getType() == DocumentChange.Type.ADDED){
                                    bank.add(dc.getDocument().toObject(BankQuestion.class));
                                }
                            }
                            bankQuestions.postValue(bank);
                        }
                    }
                });
    }
}