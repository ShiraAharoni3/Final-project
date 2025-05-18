package com.ashcollege.responses;

import com.ashcollege.entities.UserStatistics;

public class StatisticResponse extends BasicResponse
{
    private UserStatistics user ;
    private String recommendedSubjectForPractice;

    public StatisticResponse(boolean success, Integer errorCode, UserStatistics user, String recommendedSubjectForPractice) {
        super(success, errorCode);
        this.user = user;
        this.recommendedSubjectForPractice = recommendedSubjectForPractice;
    }
    public StatisticResponse() {
    }

    @Override
    public UserStatistics getUser() {
        return user;
    }

    @Override
    public void setUser(UserStatistics user) {
        this.user = user;
    }

    public String getRecommendedSubjectForPractice() {
        return recommendedSubjectForPractice;
    }

    public void setRecommendedSubjectForPractice(String recommendedSubjectForPractice) {
        this.recommendedSubjectForPractice = recommendedSubjectForPractice;
    }
}
