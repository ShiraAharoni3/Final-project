package com.ashcollege.responses;

import com.ashcollege.entities.DailyUserSuccess;

public class DailyUserSuccessResponse extends BasicResponse
{
    DailyUserSuccess dailyUserSuccess;

    public DailyUserSuccessResponse(boolean success, Integer errorCode, DailyUserSuccess dailyUserSuccess) {
        super(success, errorCode);
        this.dailyUserSuccess = dailyUserSuccess;
    }
    public DailyUserSuccessResponse() {

    }

    public DailyUserSuccess getDailyUserSuccess() {
        return dailyUserSuccess;
    }

    public void setDailyUserSuccess(DailyUserSuccess dailyUserSuccess) {
        this.dailyUserSuccess = dailyUserSuccess;
    }
}
