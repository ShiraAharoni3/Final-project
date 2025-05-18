package com.ashcollege.entities;

import java.time.LocalDateTime;

public class DailyChallengeQuestionEntity extends BaseEntity
{
    private String questionText ;
    private double answer ;
    private LocalDateTime createdAt;

    public DailyChallengeQuestionEntity(String questionText, int answer)
    {
        this.questionText = questionText;
        this.answer = answer;
        this.createdAt = LocalDateTime.now();
    }
    public DailyChallengeQuestionEntity()
    {
        this.createdAt = LocalDateTime.now();
    }



    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime date) {
        this.createdAt = date;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public double getAnswer() {
        return answer;
    }

    public void setAnswer(double answer) {
        this.answer = answer;
    }
}
