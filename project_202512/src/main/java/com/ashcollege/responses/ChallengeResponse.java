package com.ashcollege.responses;

import com.ashcollege.entities.DailyChallengeQuestionEntity;
import com.ashcollege.entities.UserStatistics;

public class ChallengeResponse extends BasicResponse
{
    DailyChallengeQuestionEntity dailyChallengeQuestion;

    public ChallengeResponse(boolean success, Integer errorCode,  DailyChallengeQuestionEntity dailyChallengeQuestion)
    {
        super(success, errorCode);
        this.dailyChallengeQuestion = dailyChallengeQuestion;
    }

    public DailyChallengeQuestionEntity getDailyChallengeQuestion() {
        return dailyChallengeQuestion;
    }

    public void setDailyChallengeQuestion(DailyChallengeQuestionEntity dailyChallengeQuestion) {
        this.dailyChallengeQuestion = dailyChallengeQuestion;
    }
}
