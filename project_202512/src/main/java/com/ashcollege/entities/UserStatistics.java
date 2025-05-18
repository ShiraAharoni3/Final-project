package com.ashcollege.entities;

public class UserStatistics extends BaseEntity
{
    private double additionStatistic ;
    private double subtractionStatistic;
    private double multiplicationStatistic;
    private double divisionStatistic;
    private double successStatistic ;
    private UserEntity userEntity ;

    public UserStatistics(double additionStatistic, double subtractionStatistic, double multiplicationStatistic,
                          double divisionStatistic, double successStatistic , UserEntity userEntity) {
        this.additionStatistic = additionStatistic;
        this.subtractionStatistic = subtractionStatistic;
        this.multiplicationStatistic = multiplicationStatistic;
        this.divisionStatistic = divisionStatistic;
        this.successStatistic = successStatistic;
        this.userEntity = userEntity;
    }
    public UserStatistics() {
    }
    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    public double getAdditionStatistic() {
        return additionStatistic;
    }

    public void setAdditionStatistic(double additionStatistic) {
        this.additionStatistic = additionStatistic;
    }

    public double getSubtractionStatistic() {
        return subtractionStatistic;
    }

    public void setSubtractionStatistic(double subtractionStatistic) {
        this.subtractionStatistic = subtractionStatistic;
    }

    public double getMultiplicationStatistic() {
        return multiplicationStatistic;
    }

    public void setMultiplicationStatistic(double multiplicationStatistic) {
        this.multiplicationStatistic = multiplicationStatistic;
    }

    public double getDivisionStatistic() {
        return divisionStatistic;
    }

    public void setDivisionStatistic(double divisionStatistic) {
        this.divisionStatistic = divisionStatistic;
    }

    public double getSuccessStatistic() {
        return successStatistic;
    }

    public void setSuccessStatistic(double successStatistic) {
        this.successStatistic = successStatistic;
    }

    @Override
    public String toString() {
        return "UserStatistics{" +
                "additionStatistic=" + additionStatistic +
                ", subtractionStatistic=" + subtractionStatistic +
                ", multiplicationStatistic=" + multiplicationStatistic +
                ", divisionStatistic=" + divisionStatistic +
                ", successStatistic=" + successStatistic +
                ", userEntity=" + userEntity +
                '}';
    }
}
