package com.example.examschool.model;

public class Question {
    private String questionId;
    private String bankQuestionId;
    private String teksSoal;
    private String pilihanA;
    private String pilihanB;
    private String pilihanC;
    private String pilihanD;
    private String pilihanE;
    private String jawabanBenar;
    private String createdAt;
    private Integer nomorurut;
    private String jawabanStudent;

    public String getJawabanStudent() {
        return jawabanStudent;
    }

    public void setJawabanStudent(String jawabanStudent) {
        this.jawabanStudent = jawabanStudent;
    }

    public Integer getNomorurut() {
        return nomorurut;
    }

    public void setNomorurut(Integer nomorurut) {
        this.nomorurut = nomorurut;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getBankQuestionId() {
        return bankQuestionId;
    }

    public void setBankQuestionId(String bankQuestionId) {
        this.bankQuestionId = bankQuestionId;
    }

    public String getTeksSoal() {
        return teksSoal;
    }

    public void setTeksSoal(String teksSoal) {
        this.teksSoal = teksSoal;
    }

    public String getPilihanA() {
        return pilihanA;
    }

    public void setPilihanA(String pilihanA) {
        this.pilihanA = pilihanA;
    }

    public String getPilihanB() {
        return pilihanB;
    }

    public void setPilihanB(String pilihanB) {
        this.pilihanB = pilihanB;
    }

    public String getPilihanC() {
        return pilihanC;
    }

    public void setPilihanC(String pilihanC) {
        this.pilihanC = pilihanC;
    }

    public String getPilihanD() {
        return pilihanD;
    }

    public String getPilihanE() {
        return pilihanE;
    }

    public void setPilihanE(String pilihanE) {
        this.pilihanE = pilihanE;
    }

    public void setPilihanD(String pilihanD) {
        this.pilihanD = pilihanD;
    }

    public String getJawabanBenar() {
        return jawabanBenar;
    }

    public void setJawabanBenar(String jawabanBenar) {
        this.jawabanBenar = jawabanBenar;
    }
}
