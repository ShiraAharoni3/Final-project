package com.ashcollege.jobs;

import com.ashcollege.entities.DailyChallengeQuestionEntity;
import com.ashcollege.entities.DailyUserSuccess;
import com.ashcollege.service.Persist;
import com.ashcollege.utils.QuestionGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class inactiveUsersJob
{
    @Autowired
    private Persist persist;


    @Scheduled(cron = "0 0 0 * * SUN")
    public void resetChallengeUsersData()
    {
        System.out.println("נכנסו לפונקציה");
        List<DailyUserSuccess> dailyUserSuccesses = persist.loadList(DailyUserSuccess.class);
        for(DailyUserSuccess d : dailyUserSuccesses)
        {
            d.setSunday(0);
            d.setMonday(0);
            d.setTuesday(0);
            d.setWednesday(0);
            d.setThursday(0);
            d.setFriday(0);
            d.setSaturday(0);
            persist.save(d);
        }

    }
    public void generateQuestions() {
        for (int level = 1; level <= 3; level++) {
            QuestionGenerator.generateAddition(level, "חיבור", 1, persist);
            QuestionGenerator.generateSubtraction(level, "חיסור", 2, persist);
            QuestionGenerator.generateMultiplication(level, "כפל", 3, persist);
            QuestionGenerator.generateDivision(level, "חילוק", 4, persist);
        }
        System.out.println("✅ שאלות נוצרו ונשמרו למסד.");
    }

    @Scheduled(cron = "0 0 0 ? * TUE,THU") // שלישי וחמישי
    public void generateQuestionsScheduled() {
        generateQuestions(); // קריאה ללוגיקה הראשית
    }
}

