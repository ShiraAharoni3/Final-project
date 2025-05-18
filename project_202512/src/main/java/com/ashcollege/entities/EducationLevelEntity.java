package com.ashcollege.entities;

public class EducationLevelEntity extends BaseEntity
{
    private int level ;
    public EducationLevelEntity()
    {

    }


    public EducationLevelEntity(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
