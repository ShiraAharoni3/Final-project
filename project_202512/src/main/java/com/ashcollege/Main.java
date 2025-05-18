package com.ashcollege;

import com.ashcollege.entities.*;
import com.ashcollege.responses.BasicResponse;
import com.ashcollege.service.Persist;
import com.ashcollege.utils.Constants;
import com.ashcollege.utils.GeneralUtils;
import com.ashcollege.utils.QuestionGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@SpringBootApplication(exclude = HibernateJpaAutoConfiguration.class)
@EnableScheduling
public class Main
{
    @Autowired
    public Persist persist;

    public static boolean applicationStarted = false;
    private static final Logger LOGGER = LoggerFactory.getLogger(Persist.class);

    public static long startTime;

    public static void main(String[] args)
    {
        ConfigurableApplicationContext context = SpringApplication.run(Main.class, args);
        LOGGER.info("Application started.");
        applicationStarted = true;
        startTime = System.currentTimeMillis();
        System.out.println("hello");
        generateQuestions(context.getBean(Persist.class));
        String time = Constants.MIDNIGHT;
        String date = "2025-05-13";
        String dateTimeString = date +" "+ time;
        List<QuestionEntity> questionEntities = getQuestionByTopicAndLevel("215946CCD7C6224027870A76E74A4133" , 4, 3, context.getBean(Persist.class));
        for(QuestionEntity q : questionEntities)
        {
            System.out.println(q.getQuestionText());
        }

    }




    static UserEntity user(String username ,String email,Persist persist)
    {
        UserEntity user = persist.getUserByUsername(username );
        String token = GeneralUtils.hashMd5(username,email);
        user.setToken(token);
        persist.save(user);
        return user;
    }
    static List<QuestionEntity> getQuestionByTopicAndLevel(String token ,int topicID, int level , Persist persist) {
        TopicEntity topic = persist.getTopicByID(topicID);
        String topicName = topic.getName();
        UserEntity user = persist.getUserByToken(token);
        System.out.println(user.getFirstName());
        System.out.println("the topic is :" + topicName + "and the level is :" + level);
        List<QuestionEntity> questionEntities = persist.loadList(QuestionEntity.class);
        List<QuestionEntity> temp = questionEntities.stream().filter(questionEntity -> questionEntity.getSubject().equals(topicName)).toList();
        List<QuestionEntity> result = temp.stream().filter(questionEntity -> questionEntity.getDifficultyLevel() == level).toList();
        List<UserQuestionStatusEntity> userQuestionStatusEntityList = persist.getQuestionByUserIDAndTopicID(user.getId() , topicID);
        System.out.println(userQuestionStatusEntityList.size());
        System.out.println(result.size());
        for(QuestionEntity q: result)
        {
            System.out.println(q.getQuestionText());
        }
        return result;
    }


    static List<QuestionEntity> getQuestionByTopicAndLevel1(int id , int level , Persist persist)
    {
        TopicEntity topic1 = persist.getTopicByID(id);
        String topicName = topic1.getName();
        System.out.println("the topic is :" +topicName + "and the level is :" + level);
        List<QuestionEntity> questionEntities = persist.loadList(QuestionEntity.class);
        List<QuestionEntity> temp = questionEntities.stream().filter(questionEntity -> questionEntity.getSubject().equals(topicName)).toList();
        List<QuestionEntity> result = temp.stream().filter(questionEntity -> questionEntity.getDifficultyLevel() == level).toList();
        System.out.println(result.size());
        return result;
    }

    public static void generateQuestions(Persist persist) {
        for (int level = 1; level <= 3; level++)
        {
            QuestionGenerator.generateAddition(level, "חיבור" , 1,persist);
            QuestionGenerator.generateSubtraction(level, "חיסור", 2,persist);
            QuestionGenerator.generateMultiplication(level, "כפל", 3,persist);
            QuestionGenerator.generateDivision(level, "חילוק",4 ,persist);
        }
        System.out.println("✅ שאלות נוצרו ונשמרו למסד.");
    }

    public static QuestionEntity generateAddition(int level , String topic ,int topicID, Persist persist) {
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
    public static int getRandomByLevel(Random rand, int level, boolean isFirstNumber , int topicID) {
        switch (level) {
            case 1:
                return isFirstNumber ? rand.nextInt(10) + 1 : rand.nextInt(90) + 10; // 1-10 אם ראשון, אחרת 10-90
            case 2:
                return isFirstNumber ? rand.nextInt(90) + 10 : rand.nextInt(90) + 1;  // 10-99
            case 3:
                switch (topicID)
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
        if(level==1)
        {
            int temp = a; a = b; b = temp;
        }// שלא יהיו שליליים ברמות נמוכות
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

    public static QuestionEntity generateMultiplication(int level , String topic ,int topicID , Persist persist) {
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

    public static QuestionEntity generateDivision(int level , String topic ,int topicID , Persist persist) {
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


