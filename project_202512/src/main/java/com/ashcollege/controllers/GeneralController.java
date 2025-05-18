package com.ashcollege.controllers;

import com.ashcollege.entities.*;
import com.ashcollege.jobs.inactiveUsersJob;
import com.ashcollege.responses.*;
import com.ashcollege.service.Persist;
import com.ashcollege.utils.ApiUtils;
import com.ashcollege.utils.Constants;
import com.ashcollege.utils.GeneralUtils;
import com.ashcollege.utils.QuestionGenerator;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.xml.crypto.Data;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static com.ashcollege.utils.Constants.ATTEMPTS;
import static com.ashcollege.utils.Constants.ZERO;

@RestController
public class GeneralController {
    private HashMap<String, UserEntity> tempUsers = new HashMap<>();
    @Autowired
    private Persist persist;
    @Autowired
    private inactiveUsersJob inactiveUsersJob;


    @Autowired
    private StreamingController streamingController;

    @PostConstruct
    public void init() {

        createMissingFolders();


    }


    private String getMaterialsFolder() {
        String userHome = System.getProperty("user.home");
        String materialsFolder = String.format("%s%s%s", userHome, File.separator, "materiels");
        return materialsFolder;
    }

    private void createMissingFolders() {
        File file = new File(getMaterialsFolder());
        if (!file.exists()) {
            try {
                file.mkdir();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @RequestMapping("/get-user-daily-challenge-success")
    public DailyUserSuccessResponse dailyUserParams(String token)
    {
        UserEntity user = persist.getUserByToken(token);
        DailyUserSuccess dailyUserSuccess = persist.getDailyUserParamsByUserID(user.getId());
        if(dailyUserSuccess == null)
        {
            dailyUserSuccess = new DailyUserSuccess();
            dailyUserSuccess.setUser(user);
            persist.save(dailyUserSuccess);
        }
        DailyUserSuccessResponse dailyUserSuccessResponse = new DailyUserSuccessResponse();
        dailyUserSuccessResponse.setDailyUserSuccess(dailyUserSuccess);
        dailyUserSuccessResponse.setErrorCode(Constants.SUCCESS);
        dailyUserSuccessResponse.setSuccess(true);
        return dailyUserSuccessResponse;

    }
    @RequestMapping("/updating-daily-question-status")
    public DailyUserSuccessResponse updateDailyChange(String token , int day , boolean result)
    {
        DailyUserSuccessResponse dailyUserSuccessResponse = new DailyUserSuccessResponse();
        System.out.println("the day is"+day);
        System.out.println(result);
        UserEntity user = persist.getUserByToken(token);
        DailyUserSuccess dailyUserSuccess = persist.getDailyUserParamsByUserID(user.getId());
        if (dailyUserSuccess == null) {
            dailyUserSuccess = new DailyUserSuccess();
            dailyUserSuccess.setUser(user);
        }
        switch (day)
        {
            case 0:
                dailyUserSuccess.setSunday(result ? 1 : 2);
                break;
            case 1:
                dailyUserSuccess.setMonday(result ? 1 : 2);
                break;
            case 2:
                dailyUserSuccess.setTuesday(result ? 1 : 2);
                break;
            case 3:
                dailyUserSuccess.setWednesday(result ? 1 : 2);
                break;
            case 4:
                dailyUserSuccess.setThursday(result ? 1 : 2);
                break;
            case 5:
                dailyUserSuccess.setFriday(result ? 1 : 2);
                break;
            case 6:
                dailyUserSuccess.setSaturday(result ? 1 : 2);
                break;
            default:
                // אפשרות ל-log או טיפול בשגיאה עבור ערך לא תקין
                System.out.println("Invalid day: " + day);
        }
        persist.save(dailyUserSuccess);
        dailyUserSuccessResponse.setDailyUserSuccess(dailyUserSuccess);
        dailyUserSuccessResponse.setErrorCode(Constants.SUCCESS);
        dailyUserSuccessResponse.setSuccess(true);

        return dailyUserSuccessResponse;
    }
    @RequestMapping("/register")
    public ValidationResponse register(String userName, String name, String lastName,
                                       String email) {
        System.out.println("YYYYyy" + email);

        boolean isValid = true;
        ValidationResponse validationResponse = new ValidationResponse();
        try {
            if (!isValid(userName, email, validationResponse)) {
                isValid = false;
            } else {
                String hashed = GeneralUtils.hashMd5(userName, email);
                System.out.println(hashed);
                UserEntity user = new UserEntity(userName, name, lastName, email, hashed);
                //String otp = GeneralUtils.generateOtp();
                //user.setOtp(otp);
                persist.save(user);
                this.tempUsers.put(user.getUsername(), user);
                //ApiUtils.sendSms(user.getOtp(), List.of(user.getPhoneNumber()));

            }
            System.out.println(userName);


        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        validationResponse.setSuccess(isValid);
        return validationResponse;
    }

    private boolean isValid(String userName, String email, ValidationResponse validationResponse) {
        boolean isValid = true;
        if (!isUsernameExists(userName)) {
            validationResponse.setUsernameTaken(Constants.USERNAME_TAKEN);
            isValid = false;
        }

        if (!isEmailExists(email)) {
            validationResponse.setEmailTaken(Constants.EMAIL_TAKEN);
            isValid = false;
        }
        return isValid;
    }

    public BasicResponse getUserQuestionsStatus(int user_id, int question_id,
                                                int topic_id) {
        BasicResponse response = new BasicResponse();
        UserQuestionStatusEntity userQuestionStatusEntity = persist.getUserQuestionsStatus(user_id, question_id, topic_id);
        if (userQuestionStatusEntity != null) {
            userQuestionStatusEntity.setNumberOfResponseAttempts(ATTEMPTS);
            persist.save(userQuestionStatusEntity);
            response.setSuccess(true);
            response.setErrorCode(Constants.SUCCESS);
        } else {
            response.setErrorCode(Constants.USER_NOT_EXIST);
        }
        return response;
    }

    public boolean isUsernameExists(String username) {
        List<UserEntity> users = persist.loadList(UserEntity.class);
        List<UserEntity> temp = users.stream().filter(user -> user.getUsername().equals(username)).toList();
        return temp.isEmpty();
    }

    public boolean isEmailExists(String email) {
        List<UserEntity> users = persist.loadList(UserEntity.class);
        List<UserEntity> temp = users.stream().filter(user -> user.getEmail().equals(email)).toList();
        return temp.isEmpty();
    }

    @RequestMapping(value = "/get-user", method = {RequestMethod.GET, RequestMethod.POST})
    public UserEntity getUsername(String token)
    {
        UserEntity user = new UserEntity();
        if(token.equals(Constants.MANAGER_TOKEN))
        {
          user.setUsername(Constants.MANAGER_USER_NAME);
        }
        else
        {
          user = persist.getUserByToken(token);
        }
        return user;
    }

    @RequestMapping(value = "/login", method = {RequestMethod.GET, RequestMethod.POST})
    public LoginResponse login(String username, String email)
    {


        System.out.println("kk" + username);
        LoginResponse response = new LoginResponse();
        String token ;
        if(username.equals(Constants.MANAGER_USER_NAME)&& email.equals(Constants.MANAGER_EMAIL))
        {
           token = Constants.MANAGER_TOKEN;
           response.setSuccess(true);
           response.setErrorCode(Constants.SUCCESS);
           response.setToken(token);
           response.setPermission(Constants.MANAGER_PERMISSION);
        }
        else
        {
            token = GeneralUtils.hashMd5(username, email);
            System.out.println(token);
            UserEntity user = persist.getUserByUsernameAndPass(username, token);

            if (user != null)
            {
                persist.save(user);
                response.setSuccess(true);
                response.setErrorCode(Constants.SUCCESS);
                response.setToken(user.getToken());
                response.setPermission(Constants.STUDENT_PERMISSION);
                QuestionGenerator.generateQuestions(persist);
                inactiveUsersJob.generateQuestionsScheduled();
            } else {
                response.setErrorCode(Constants.USER_NOT_EXIST);
            }
        }

        return response;
    }
    @RequestMapping()
    public String generateDailyEquation()
    {
        Random rand = new Random();
        String[] operators = {"+", "-", "*", "/"};
        int numOperators = 2 + rand.nextInt(2);
        StringBuilder equation = new StringBuilder();

        int currentValue = rand.nextInt(10) + 1;
        equation.append(currentValue);

        for (int i = 0; i < numOperators; i++) {
            String op = operators[rand.nextInt(operators.length)];
            int nextValue = rand.nextInt(9) + 1;

            // כדי למנוע חילוק לא תקני
            if (op.equals("/") && currentValue % nextValue != 0) {
                nextValue = 1;
            }

            equation.append(" ").append(op).append(" ").append(nextValue);

        }

        return equation.toString();
    }
    @RequestMapping("/get-all-users")
    List<UserEntity> getAllUsers() {
        return this.persist.loadList(UserEntity.class);
    }

    @RequestMapping("/get-daily-challenge")
    public DailyChallengeQuestionEntity getDailyChallenge(
            @RequestParam("currentDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date currentDate)
    {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = sdf.format(currentDate);
        System.out.println("the date is"+ formattedDate);
        String time = Constants.MIDNIGHT;
        String date = formattedDate;
        String dateTimeString = date +" "+ time;
        System.out.println("נכנסת לפונקציה");

        // פורמט התאריך והשעה
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // המרת ה-String ל-LocalDateTime
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeString, formatter);
        DailyChallengeQuestionEntity dailyChallengeQuestion = new DailyChallengeQuestionEntity();
        dailyChallengeQuestion = persist.getDailyChallengeByDate(dateTime);
        return dailyChallengeQuestion;
    }


    @RequestMapping("/get-all-courses")
    public List<EducationLevelEntity> getAllEducationLevels() {
        return this.persist.loadList(EducationLevelEntity.class);
    }


    @RequestMapping("get-all-education-level")
    List<EducationLevelEntity> getAllLevels() {
        return this.persist.loadList(EducationLevelEntity.class);
    }

    @RequestMapping("/get-all-topic")
    List<TopicEntity> getAllTopic() {
        return this.persist.loadList(TopicEntity.class);
    }

    @RequestMapping("/get-question-text")
    List<QuestionEntity> getQuestionText() {
        return this.persist.loadList(QuestionEntity.class);
    }

    @RequestMapping("/get-question-by-topic-and-level")
    List<QuestionEntity> getQuestionByTopicAndLevel(String token ,int topicID, int level)
    {
        TopicEntity topic = persist.getTopicByID(topicID);
        String topicName = topic.getName();
        System.out.println("the topic is :" + topicName + "and the level is :" + level);
        List<QuestionEntity> questionEntities = persist.loadList(QuestionEntity.class);
        List<QuestionEntity> temp = questionEntities.stream().filter(questionEntity -> questionEntity.getSubject().equals(topicName)).toList();
        List<QuestionEntity> result = temp.stream().filter(questionEntity -> questionEntity.getDifficultyLevel() == level).toList();
        if(token.equals(Constants.MANAGER_TOKEN) )
        {
            return result;
        }
        else
        {
            UserEntity user = persist.getUserByToken(token);
            System.out.println(user.getFirstName());
            List<UserQuestionStatusEntity> userQuestionStatusEntityList = persist.getQuestionByUserIDAndTopicID(user.getId() , topicID);
            System.out.println(userQuestionStatusEntityList.size());
            System.out.println(result.size());
            result = getQuestionByAnswered(result ,userQuestionStatusEntityList);
            return result;

        }

    }
    List<QuestionEntity> getQuestionByAnswered(List<QuestionEntity> result , List<UserQuestionStatusEntity> userQuestionStatusEntityList)
    {
        List<Integer> answeredQuestionIds = userQuestionStatusEntityList.stream()
                .map(userQuestionStatus -> userQuestionStatus.getQuestionEntity().getId())
                .collect(Collectors.toList());

// הפרדה לשאלות שלא נענו ונענו
        List<QuestionEntity> unanswered = result.stream()
                .filter(q -> !answeredQuestionIds.contains(q.getId()))
                .toList();

        List<QuestionEntity> answered = result.stream()
                .filter(q -> answeredQuestionIds.contains(q.getId()))
                .toList();

// איחוד: לא נענו קודם, ואז נענו
        List<QuestionEntity> orderedQuestions = new ArrayList<>();
        orderedQuestions.addAll(unanswered);
        orderedQuestions.addAll(answered);

        return orderedQuestions;

    }
    @RequestMapping("get-user-statistic-by-user-id")
    public StatisticResponse getUserStatisticByUserId(String username)
    {
        UserEntity user = persist.getUserByUsername(username);
        StatisticResponse response = new StatisticResponse();
        response = getUserStatistic(user.getToken());
        return response;
    }
    @RequestMapping("/search-user")
    public List<UserEntity> getUserSearch (String username)
    {
        List<UserEntity> temp = persist.loadList(UserEntity.class);
        List<UserEntity> usersList = new ArrayList<>();
        for(UserEntity user: temp)
        {
            if(user.getUsername().contains(username))
            {
                usersList.add(user);
            }
        }
        System.out.println(usersList.size());
        return usersList;

    }
    @RequestMapping("/get-statistic")
    public StatisticResponse getStatistic()
    {
        StatisticResponse response = new StatisticResponse();
        UserStatistics userStatistics = new UserStatistics();
        int totalResponses = 0;
        int totalSuccess = 0;
        List<UserQuestionStatusEntity> questionEntities = persist.loadList(UserQuestionStatusEntity.class);
        for (UserQuestionStatusEntity g : questionEntities) {
            totalResponses += g.getNumberOfResponseAttempts();
            totalSuccess += g.getNumberOfAttemptsUntilSuccess();
        }
        double overallStatistic = calculateSuccessRate(totalResponses, totalSuccess);
        userStatistics.setSuccessStatistic(overallStatistic);
        // סטטיסטיקה לפי נושא (1 עד 4)
        for (int i = 1; i <= 4; i++) {
            int topicResponses = 0;
            int topicSuccess = 0;

            List<UserQuestionStatusEntity> userQuestionStatusEntities = persist.getUserQuestionByUserID(ZERO, i);
            for (UserQuestionStatusEntity u : userQuestionStatusEntities)
            {
                topicResponses += u.getNumberOfResponseAttempts();
                topicSuccess += u.getNumberOfAttemptsUntilSuccess();
            }

            double topicStatistic = calculateSuccessRate(topicResponses, topicSuccess);

            switch (i) {
                case 1:
                    userStatistics.setAdditionStatistic(topicStatistic);
                    break;
                case 2:
                    userStatistics.setSubtractionStatistic(topicStatistic);
                    break;
                case 3:
                    userStatistics.setMultiplicationStatistic(topicStatistic);
                    break;
                case 4:
                    userStatistics.setDivisionStatistic(topicStatistic);
                    break;
            }
        }
        String topic = topicToPractice(userStatistics);
        response.setRecommendedSubjectForPractice(topic);
        response.setUser(userStatistics);
        response.setSuccess(true);
        response.setErrorCode(Constants.SUCCESS);

        return response;


    }

    @RequestMapping("/get-user-statistic")
    public StatisticResponse getUserStatistic(String token)
    {
        StatisticResponse response = new StatisticResponse();
        UserStatistics userStatistics = new UserStatistics();
        UserEntity user = persist.getUserByToken(token);
        userStatistics.setUserEntity(user);

        // סטטיסטיקה כללית (topicId = 0)
        int totalResponses = 0;
        int totalSuccess = 0;
        List<UserQuestionStatusEntity> generalQuestions = persist.getUserQuestionByUserID(user.getId(), 0);
        for (UserQuestionStatusEntity g : generalQuestions) {
            totalResponses += g.getNumberOfResponseAttempts();
            totalSuccess += g.getNumberOfAttemptsUntilSuccess();
        }
        double overallStatistic = calculateSuccessRate(totalResponses, totalSuccess);
        userStatistics.setSuccessStatistic(overallStatistic);

        // סטטיסטיקה לפי נושא (1 עד 4)
        for (int i = 1; i <= 4; i++) {
            int topicResponses = 0;
            int topicSuccess = 0;

            List<UserQuestionStatusEntity> userQuestionStatusEntities = persist.getUserQuestionByUserID(user.getId(), i);
            for (UserQuestionStatusEntity u : userQuestionStatusEntities) {
                topicResponses += u.getNumberOfResponseAttempts();
                topicSuccess += u.getNumberOfAttemptsUntilSuccess();
            }

            double topicStatistic = calculateSuccessRate(topicResponses, topicSuccess);

            switch (i) {
                case 1:
                    userStatistics.setAdditionStatistic(topicStatistic);
                    break;
                case 2:
                    userStatistics.setSubtractionStatistic(topicStatistic);
                    break;
                case 3:
                    userStatistics.setMultiplicationStatistic(topicStatistic);
                    break;
                case 4:
                    userStatistics.setDivisionStatistic(topicStatistic);
                    break;
            }
        }
        String topic = topicToPractice(userStatistics);
        response.setRecommendedSubjectForPractice(topic);
        response.setUser(userStatistics);
        response.setSuccess(true);
        response.setErrorCode(Constants.SUCCESS);

        return response;
    }
    public String topicToPractice(UserStatistics userStatistics)
    {
        String topic = null;
        if(userStatistics.getAdditionStatistic() < userStatistics.getSubtractionStatistic())
        {
            if(userStatistics.getAdditionStatistic() < userStatistics.getMultiplicationStatistic())
            {
                if(userStatistics.getAdditionStatistic() < userStatistics.getDivisionStatistic())
                {
                    topic = "חיבור";
                }
                else
                {
                    topic = "חילוק";
                }
            }
            else
            {
                if(userStatistics.getMultiplicationStatistic() < userStatistics.getDivisionStatistic()) {
                    topic = "כפל";
                }

                else
                    {
                        topic = "חילוק";
                    }
                }

        }
        else
        {
            if(userStatistics.getSubtractionStatistic() < userStatistics.getMultiplicationStatistic())
            {
                if(userStatistics.getSubtractionStatistic() < userStatistics.getDivisionStatistic())
                {
                    topic = "חיסור";
                }
                else
                {
                    topic = "חילוק";
                }
            }
            else
            {
                if(userStatistics.getMultiplicationStatistic() < userStatistics.getDivisionStatistic()) {
                    topic = "כפל";
                }

                else
                {
                    topic = "חילוק";
                }
            }

        }
        return topic;
    }

    public double calculateSuccessRate(int totalResponses, int totalSuccessAttempts) {
        if (totalResponses == 0) return 0;
        return (double) (totalResponses - totalSuccessAttempts) / totalResponses;
    }
    @RequestMapping("/updating-questions-status")
    public BasicResponse updateQuestionStatus(String token, int questionID, int topicID, boolean result)
    {

        System.out.println(result);
        BasicResponse response = new BasicResponse();
        try {
            UserEntity user = persist.getUserByToken(token);
            QuestionEntity question = persist.getQuestionByID(questionID);
            TopicEntity topic = persist.getTopicByID(topicID);
            if (user == null) {
                response.setErrorCode(Constants.USER_NOT_EXIST);
                return response;
            }

            UserQuestionStatusEntity userQuestionStatusEntity = persist.getUserQuestionsStatus(user.getId(), questionID, topicID);

            if (userQuestionStatusEntity == null) {
                userQuestionStatusEntity = new UserQuestionStatusEntity();
                userQuestionStatusEntity.setUserEntity(user);
                userQuestionStatusEntity.setQuestionEntity(question);
                userQuestionStatusEntity.setTopicEntity(topic);
                userQuestionStatusEntity.setIs_answered(true);
            }

            userQuestionStatusEntity.setIs_correct(result);
            if(result)
            {
                userQuestionStatusEntity.setNumberOfAttemptsUntilSuccess(0);
            }
            else
            {
                userQuestionStatusEntity.setNumberOfAttemptsUntilSuccess(ATTEMPTS);
            }
            userQuestionStatusEntity.setNumberOfResponseAttempts(ATTEMPTS);

            persist.save(userQuestionStatusEntity);

            System.out.println(userQuestionStatusEntity); // עדיף מ־toString לבד

            response.setSuccess(true);
            response.setErrorCode(Constants.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace(); // רושם את השגיאה בקונסול
            response.setSuccess(false);
            response.setErrorCode(Constants.FAIL);
        }
        return response;
    }


    @RequestMapping("/get-materials")
    public List<QuestionEntity> getMaterials() {
        return this.persist.loadList(QuestionEntity.class);
    }

    @RequestMapping("/get-course")
    public EducationLevelEntity getCourse(int id) {
        return this.persist.loadObject(EducationLevelEntity.class, id);
    }

    @RequestMapping("/get-questions-by-education-level-id")
    public List<QuestionEntity> getQuestionsByCourseId(int educationLevelId) {
        System.out.println("הבקשה עברה");
        return persist.getQuestionsByEducationLevel(educationLevelId);
    }

    @RequestMapping("/add-question")
    public void addQuestions(String questionText, double solution, int difficultyLevel, String tags) {
        QuestionEntity question = new QuestionEntity(questionText, solution, difficultyLevel, tags);
        this.persist.save(question);
    }
}

