package com.example.examschool.model;

public class BankQuestion {
    private String bankQuestionId;
    private String teacherId;
    private String classRoom;
    private String lessonName;
    private String tokenQuestion;
    private String createdAt;
    private Double duration;

    public String getLessonName() {
        return lessonName;
    }

    public void setLessonName(String lessonName) {
        this.lessonName = lessonName;
    }

    public String getBankQuestionId() {
        return bankQuestionId;
    }

    public void setBankQuestionId(String bankQuestionId) {
        this.bankQuestionId = bankQuestionId;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getClassRoom() {
        return classRoom;
    }

    public void setClassRoom(String classRoom) {
        this.classRoom = classRoom;
    }

    public String getTokenQuestion() {
        return tokenQuestion;
    }

    public void setTokenQuestion(String tokenQuestion) {
        this.tokenQuestion = tokenQuestion;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Double getDuration() {
        return duration;
    }

    public void setDuration(Double duration) {
        this.duration = duration;
    }
}
