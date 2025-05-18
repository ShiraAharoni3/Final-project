package com.ashcollege.responses;

import com.ashcollege.entities.UserStatistics;

public class BasicResponse {
    private boolean success;
    private Integer errorCode;
    private String token;
    private UserStatistics user ;

    public BasicResponse(boolean success, Integer errorCode) {
        this.success = success;
        this.errorCode = errorCode;
    }
    public BasicResponse(String token) {
        this.success = true;
        this.token = token;
        this.errorCode = null;
    }

    public BasicResponse(boolean success, Integer errorCode, UserStatistics user) {
        this.success = success;
        this.errorCode = errorCode;
        this.user = user;
    }

    public UserStatistics getUser() {
        return user;
    }

    public void setUser(UserStatistics user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public BasicResponse() {
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }
}
