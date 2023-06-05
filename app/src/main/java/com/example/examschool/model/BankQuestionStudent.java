package com.example.examschool.model;

public class BankQuestionStudent {
    private String bankQuestionStudentId;
    private String bankQuestionId;
    private String createdAt;
    private String studentId;
    private Boolean done;
    private String skor;
    private String fullname;


    public BankQuestionStudent() {}

    public BankQuestionStudent(String bankQuestionStudentId, String bankQuestionId, String createdAt, String studentId, Boolean done, String skor, String fullname) {
        this.bankQuestionStudentId = bankQuestionStudentId;
        this.bankQuestionId = bankQuestionId;
        this.createdAt = createdAt;
        this.studentId = studentId;
        this.done = done;
        this.skor = skor;
        this.fullname = fullname;
    }

    public String getBankQuestionStudentId() {
        return bankQuestionStudentId;
    }

    public void setBankQuestionStudentId(String bankQuestionStudentId) {
        this.bankQuestionStudentId = bankQuestionStudentId;
    }

    public String getBankQuestionId() {
        return bankQuestionId;
    }

    public void setBankQuestionId(String bankQuestionId) {
        this.bankQuestionId = bankQuestionId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public Boolean getDone() {
        return done;
    }

    public void setDone(Boolean done) {
        this.done = done;
    }

    public String getSkor() {
        return skor;
    }

    public void setSkor(String skor) {
        this.skor = skor;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }
}
