package com.example.examschool.ui.home_student;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.examschool.model.BankQuestion;
import com.example.examschool.model.BankQuestionStudent;
import com.example.examschool.utils.FirebaseUtils;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HomeStudentViewModel extends ViewModel {

    private MutableLiveData<List<BankQuestionStudent>> bankQuestions;
    private MutableLiveData<List<BankQuestionStudent>> bankQuestionsFinish;
    public HomeStudentViewModel() {
        bankQuestions = new MutableLiveData<>();
        bankQuestionsFinish = new MutableLiveData<>();
    }

    public MutableLiveData<List<BankQuestionStudent>> getBankQuestions() {
        return bankQuestions;
    }

    public MutableLiveData<List<BankQuestionStudent>> getBankQuestionsFinish() {
        return bankQuestionsFinish;
    }

    public void loadBankQuestions() {
        final String studentId = FirebaseUtils.getFirebaseUser().getUid();
        List<BankQuestionStudent> bank = new ArrayList<>();
        FirebaseUtils.getFirestore().collection("bankQuestionStudent")
                .whereEqualTo("studentId",studentId)
                .whereEqualTo("done",false)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(value != null && value.getDocumentChanges() != null ){
                            for (DocumentChange dc : value.getDocumentChanges()){
                                if(dc.getType() == DocumentChange.Type.ADDED){
                                    String bankQuestionId = dc.getDocument().getString("bankQuestionId");
                                    String createdAt = dc.getDocument().getString("createdAt");
                                    FirebaseUtils.getFirestore().collection("bankQuestion")
                                            .whereEqualTo("bankQuestionId",bankQuestionId)
                                            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                                @Override
                                                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                                    if(value != null && value.getDocumentChanges() != null ){
                                                        for (DocumentChange dc1 : value.getDocumentChanges()){
                                                            BankQuestionStudent bankQuestionStudent = dc.getDocument().toObject(BankQuestionStudent.class);
                                                            bank.add(bankQuestionStudent);
                                                        }
                                                        bankQuestions.postValue(bank);
                                                    }
                                                }
                                            });
                                }

                            }
                        }
                    }
                });
    }


    public void loadBankQuestionsFinish() {
        final String studentId = FirebaseUtils.getFirebaseUser().getUid();
        List<BankQuestionStudent> bank = new ArrayList<>();
        FirebaseUtils.getFirestore().collection("bankQuestionStudent")
                .whereEqualTo("studentId", studentId)
                .whereEqualTo("done", true)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (value != null && value.getDocumentChanges() != null) {
                            for (DocumentChange dc : value.getDocumentChanges()) {
                                if (dc.getType() == DocumentChange.Type.ADDED) {
                                    String bankQuestionId = dc.getDocument().getString("bankQuestionId");
                                    String createdAt = dc.getDocument().getString("createdAt");
                                    FirebaseUtils.getFirestore().collection("bankQuestion")
                                            .whereEqualTo("bankQuestionId", bankQuestionId)
                                            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                                @Override
                                                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                                    if (value != null && !value.isEmpty()) {
                                                        bank.addAll(value.toObjects(BankQuestionStudent.class));
                                                        bankQuestionsFinish.postValue(bank);
                                                    }
                                                }
                                            });
                                }
                            }
                        }
                    }
                });
    }


}
