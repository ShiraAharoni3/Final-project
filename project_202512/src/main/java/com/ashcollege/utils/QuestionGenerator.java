package com.ashcollege.utils;

import com.ashcollege.entities.QuestionEntity;
import com.ashcollege.service.Persist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Random;

public class QuestionGenerator
{
    @Autowired
    private Persist persist;

    @Scheduled(cron = "0 12 0 * * SUN")
    public static void generateQuestions(Persist persist) {
        for (int level = 1; level <= 3; level++)
        {
            QuestionGenerator.generateAddition(level, "חיבור" ,1,persist);
            QuestionGenerator.generateSubtraction(level, "חיסור", 2,persist);
            QuestionGenerator.generateMultiplication(level, "כפל", 3,persist);
            QuestionGenerator.generateDivision(level, "חילוק", 4,persist);
        }
        System.out.println("✅ שאלות נוצרו ונשמרו למסד.");
    }

    public static QuestionEntity generateAddition(int level , String topic, int topicID ,Persist persist) {
        Random rand = new Random();
        int a = getRandomByLevel(rand, level ,true, topicID);
        int b = getRandomByLevel(rand, level, false, topicID);
        String text = a + " + " + b + " = ?";
        int answer = a + b;
        System.out.println(topic);
        QuestionEntity existing = persist.isQuestionExist(text);
        if (existing != null) {
            return existing;
        } else {
            QuestionEntity question = new QuestionEntity(text, answer, level, topic);
            persist.save(question);
            return question;
        }
    }
    public static int getRandomByLevel(Random rand, int level, boolean isFirstNumber , int topic) {
        switch (level) {
            case 1:
                return isFirstNumber ? rand.nextInt(10) + 1 : rand.nextInt(90) + 10; // 1-10 אם ראשון, אחרת 10-90
            case 2:
                return isFirstNumber ? rand.nextInt(90) + 10 : rand.nextInt(90) + 1;  // 10-99
            case 3:
                switch (topic)
                {
                    case 1:
                        return  isFirstNumber ? rand.nextInt(900) + 100 : rand.nextInt(100) + 1; // 100-999
                    case 2:
                        return  isFirstNumber ? rand.nextInt(900) + 100 : rand.nextInt(90) + 1; // 100-999
                    case 3:
                        return  isFirstNumber ? rand.nextInt(99) + 1 : rand.nextInt(99) + 1; // 100-999
                    case 4:
                        return  isFirstNumber ? rand.nextInt(99) + 1 : rand.nextInt(90) + 1; // 100-999
                }

            default:
                return rand.nextInt(10) + 1; // ברירת מחדל: 1-10
        }
    }

    public static QuestionEntity generateSubtraction(int level , String topic, int topicID ,Persist persist) {
        Random rand = new Random();
        int a = getRandomByLevel(rand, level, true, topicID);
        int b = getRandomByLevel(rand, level , false, topicID);
        if(level==1 || level ==2)
        {
            if(a<b)
            {
                int temp = a; a = b; b = temp;
            }

        }
        String text = a + " - " + b + " = ?";
        int answer = a - b;
        QuestionEntity existing = persist.isQuestionExist(text);
        if (existing != null) {
            return existing;
        } else {
            QuestionEntity question = new QuestionEntity(text, answer, level, topic);
            persist.save(question);
            return question;
        }

    }

    public static QuestionEntity generateMultiplication(int level , String topic , int topicID , Persist persist) {
        Random rand = new Random();
        int a = getRandomByLevel(rand, level, true, topicID);
        int b = getRandomByLevel(rand, level , false, topicID);
        String text = a + " × " + b + " = ?";
        int answer = a * b;
        QuestionEntity existing = persist.isQuestionExist(text);
        if (existing != null) {
            return existing;
        } else {
            QuestionEntity question = new QuestionEntity(text, answer, level, topic);
            persist.save(question);
            return question;
        }
    }

    public static QuestionEntity generateDivision(int level , String topic ,int topicID ,Persist persist) {
        Random rand = new Random();
        int b = getRandomByLevel(rand, level, true, topicID);
        int answer = getRandomByLevel(rand, level, false, topicID);
        int a = b * answer;
        String text = a + " ÷ " + b + " = ?";
        QuestionEntity existing = persist.isQuestionExist(text);
        if (existing != null) {
            return existing;
        } else {
            QuestionEntity question = new QuestionEntity(text, answer, level, topic);
            persist.save(question);
            return question;
        }

    }

}

