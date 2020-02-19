package com.newiplquizgame.myipl.pkg;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class QuestionArray {
    @SerializedName("QueId")
    @Expose
    private Integer queId;
    @SerializedName("Question")
    @Expose
    private String question;
    @SerializedName("Answers")
    @Expose
    private String answers;
    @SerializedName("Point")
    @Expose
    private Double point;
    @SerializedName("Attempt")
    @Expose
    private Integer attempt;

    public Integer getAttempt() {
        return attempt;
    }

    public void setAttempt(Integer attempt) {
        this.attempt = attempt;
    }

    public Integer getQueId() {
        return queId;
    }

    public void setQueId(Integer queId) {
        this.queId = queId;
    }
    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswers() {
        return answers;
    }

    public void setAnswers(String answers) {
        this.answers = answers;
    }

    public Double getPoint() {
        return point;
    }

    public void setPoint(Double point) {
        this.point = point;
    }


}
