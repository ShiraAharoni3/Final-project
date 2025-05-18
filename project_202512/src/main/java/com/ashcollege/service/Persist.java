package com.ashcollege.service;


import com.ashcollege.entities.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import static com.ashcollege.utils.Constants.ZERO;


@Transactional
@Component
@SuppressWarnings("unchecked")
public class Persist {

    private static final Logger LOGGER = LoggerFactory.getLogger(Persist.class);

    public final SessionFactory sessionFactory;


    @Autowired
    public Persist(SessionFactory sf) {
        this.sessionFactory = sf;
    }
    public <T> void saveAll(List<T> objects) {
        for (T object : objects) {
            sessionFactory.getCurrentSession().saveOrUpdate(object);
        }
    }
    public <T> void remove(Object o){
        sessionFactory.getCurrentSession().remove(o);
    }


    public Session getQuerySession() {
        return sessionFactory.getCurrentSession();
    }

    public void save(Object object) {
        this.sessionFactory.getCurrentSession().saveOrUpdate(object);
    }

    public <T> T loadObject(Class<T> clazz, int oid) {
        return this.getQuerySession().get(clazz, oid);
    }

    public <T> List<T> loadList(Class<T> clazz)
    {
        return this.sessionFactory.getCurrentSession()
                .createQuery("FROM " + clazz.getSimpleName()).list();
    }
    public TopicEntity getTopicByID(int id) {
        try {
            return this.sessionFactory.getCurrentSession()
                    .createQuery("FROM TopicEntity WHERE id = :id ", TopicEntity.class)
                    .setParameter("id", id)
                    .uniqueResult();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public List<UserQuestionStatusEntity> getUserQuestionByUserID(int userID , int topicID)
    {
        try {
            if(userID == ZERO)
            {
                return this.sessionFactory.getCurrentSession()
                        .createQuery("FROM UserQuestionStatusEntity WHERE topic_id = :topic_id", UserQuestionStatusEntity.class)
                        .setParameter("topic_id", topicID)
                        .list();
            }
            else
            {
                if (topicID == ZERO)
                {
                    return this.sessionFactory.getCurrentSession()
                            .createQuery("FROM UserQuestionStatusEntity WHERE user_id = :user_id", UserQuestionStatusEntity.class)
                            .setParameter("user_id", userID)
                            .list();
                }
                else
                {
                    return this.sessionFactory.getCurrentSession()
                            .createQuery("FROM UserQuestionStatusEntity WHERE user_id = :user_id" +
                                    " and topic_id = :topic_id ", UserQuestionStatusEntity.class)
                            .setParameter("user_id", userID)
                            .setParameter("topic_id", topicID)
                            .list();
                }
            }


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public UserEntity getUserByToken (String token )
    {
        try {
            return this.sessionFactory.getCurrentSession()
                    .createQuery("FROM UserEntity WHERE token = :token " ,UserEntity.class)
                    .setParameter("token", token)
                    .uniqueResult();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public QuestionEntity getQuestionByID (int questionID )
    {
        try {
            return this.sessionFactory.getCurrentSession()
                    .createQuery("FROM QuestionEntity WHERE id = :id " ,QuestionEntity.class)
                    .setParameter("id", questionID)
                    .uniqueResult();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public List<UserQuestionStatusEntity> getQuestionByUserIDAndTopicID (int userId, int topicId )
    {
        try {
            return this.sessionFactory.getCurrentSession()
                    .createQuery("FROM UserQuestionStatusEntity WHERE user_id = :user_id" +
                            " and topic_id = :topic_id and is_correct = :is_correct" ,UserQuestionStatusEntity.class)
                    .setParameter("user_id", userId)
                    .setParameter("topic_id", topicId)
                    .setParameter("is_correct", true)
                    .list();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public UserQuestionStatusEntity getUserQuestionsStatus (int user_id , int question_id ,int topic_id)
    {
        try {
            return this.sessionFactory.getCurrentSession()
                    .createQuery("FROM UserQuestionStatusEntity WHERE user_id = :user_id " +
                            "and question_id = :question_id and topic_id = :topic_id", UserQuestionStatusEntity.class)
                    .setParameter("user_id", user_id)
                    .setParameter("question_id", question_id)
                    .setParameter("topic_id", topic_id)
                    .uniqueResult();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public UserEntity getUserByUsername(String username) {
        try {
            return this.sessionFactory.getCurrentSession()
                    .createQuery("FROM UserEntity WHERE username = :username ", UserEntity.class)
                    .setParameter("username", username)
                    .uniqueResult();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public UserStatistics getUserQuestionStatusByUserId(int UserId)
    {
        try {
            return this.sessionFactory.getCurrentSession()
                    .createQuery("FROM UserStatistics WHERE user_id = :user_id and token = :token", UserStatistics.class)
                    .setParameter("user_id", UserId)
                    .uniqueResult();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
    public UserEntity getUserByUsernameAndPass(String username, String token) {
        try {
            return this.sessionFactory.getCurrentSession()
                    .createQuery("FROM UserEntity WHERE username = :username and token = :token", UserEntity.class)
                    .setParameter("username", username)
                    .setParameter("token",token)
                    .uniqueResult();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public DailyChallengeQuestionEntity getDailyChallengeByDate(LocalDateTime date) {
        try {
            return this.sessionFactory.getCurrentSession()
                    .createQuery("FROM DailyChallengeQuestionEntity WHERE createdAt = :createdAt", DailyChallengeQuestionEntity.class)
                    .setParameter("createdAt", date)
                    .uniqueResult();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public DailyUserSuccess getDailyUserParamsByUserID(int userId)
    {
        try {
            return this.sessionFactory.getCurrentSession()
                    .createQuery("FROM DailyUserSuccess WHERE user_id = :user_id", DailyUserSuccess.class)
                    .setParameter("user_id", userId)
                    .uniqueResult();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


    /*public List<QuestionEntity> getMaterialByCourseId(int courseId) {
        return this.getQuerySession()
                .createQuery("FROM QuestionEntity WHERE course_id = :course_id")
                .setParameter("course_id",courseId)
                .list();
    }
    public List<QuestionEntity> getQuestionsByDifficultyLevel(int courseId) {
        return this.getQuerySession()
                .createQuery("FROM QuestionEntity WHERE DifficultyLevel = :courseId", QuestionEntity.class)
                .setParameter("difficultyLevel", courseId)
                .getResultList();
    }*/
    public List<QuestionEntity> getQuestionsByEducationLevel(int educationLevelId) {
        List<QuestionEntity> d = this.getQuerySession()
                .createQuery("FROM QuestionEntity WHERE educationLevelEntity.id = :educationLevelId", QuestionEntity.class)
                .setParameter("educationLevelId", educationLevelId)
                .list();
        System.out.println(d.size());
        return d;


        /*return this.getQuerySession()
                .createQuery("FROM QuestionEntity WHERE educationLevelEntity.id = :educationLevelId", QuestionEntity.class)
                .setParameter("educationLevelId", educationLevelId)
                .list();*/


    }
    public UserEntity getUserByPass(String password) {
        try {
            return this.sessionFactory.getCurrentSession()
                    .createQuery("FROM UserEntity WHERE password = :password", UserEntity.class)
                    .setParameter("password", password)
                    .uniqueResult();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public DailyChallengeQuestionEntity isDailyQuestionExist(String questionText)
    {
        try {
            return this.sessionFactory.getCurrentSession()
                    .createQuery("FROM DailyChallengeQuestionEntity WHERE questionText = :questionText ", DailyChallengeQuestionEntity.class)
                    .setParameter("questionText", questionText)
                    .uniqueResult();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public QuestionEntity isQuestionExist(String textQuestion)
    {
        try {
            return this.sessionFactory.getCurrentSession()
                    .createQuery("FROM QuestionEntity WHERE questionText = :textQuestion ", QuestionEntity.class)
                    .setParameter("textQuestion", textQuestion)
                    .uniqueResult();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        }
    }
    /*public QuestionStatisticsEntity getQuestionStats(int userId, int questionId) {
        return this.getQuerySession()
                .createQuery("SELECT q FROM QuestionStatisticsEntity q WHERE q.userId = :userId AND q.questionId = :questionId", QuestionStatisticsEntity.class)
                .setParameter("userId", userId)
                .setParameter("questionId", questionId)
                .getResultStream()
                .findFirst()
                .orElse(null);
    }*/

    /*public List<MaterialEntity> getMaterialByTitle(String title){
        return this.getQuerySession()
                .createQuery("FROM MaterialEntity WHERE title = :title")
                .setParameter("title",title)
                .list();
    }

    public UserEntity getUserByUsernameAndPass(String username, String password) {
        try {
            return this.sessionFactory.getCurrentSession()
                    .createQuery("FROM UserEntity WHERE username = :name and password = :password", UserEntity.class)
                    .setParameter("name", username)
                    .setParameter("password",password)
                    .uniqueResult();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<MaterialEntity> getMaterialByCourseId(int courseId) {
        return this.getQuerySession()
                .createQuery("FROM MaterialEntity WHERE course_id = :course_id")
                .setParameter("course_id",courseId)
                .list();
    }

    public UserEntity getUserByEmail(String email) {
        return this.sessionFactory.getCurrentSession()
                .createQuery("FROM UserEntity WHERE email = :email ", UserEntity.class)
                .setParameter("email", email)
                .uniqueResult();
    }

    public UserEntity getUserByEmailAndPasswordRecovery(String email, String pass_recovery) {
        return this.sessionFactory.getCurrentSession()
                .createQuery("FROM UserEntity WHERE email = :email and pass_recovery = :pass_recovery", UserEntity.class)
                .setParameter("email", email)
                .setParameter("pass_recovery" ,pass_recovery)
                .uniqueResult();
    }

    public UserEntity getUserByUsername(String username) {
        return this.sessionFactory.getCurrentSession()
                .createQuery("FROM UserEntity WHERE username = :name", UserEntity.class)
                .setParameter("name", username)
                .uniqueResult();
    }

    public UserEntity getUserByPass(String password) {
        try {
            return this.sessionFactory.getCurrentSession()
                    .createQuery("FROM UserEntity WHERE password = :password", UserEntity.class)
                    .setParameter("password", password)
                    .uniqueResult();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public List<MaterialHistoryEntity> getMaterialHistoryByUserId(int user_id) {
        return this.getQuerySession()
                .createQuery(" FROM MaterialHistoryEntity mh WHERE mh.user.id = :user_id", MaterialHistoryEntity.class)
                .setParameter("user_id", user_id)
                .list();
    }
    public MaterialEntity getMaterialById(int id) {
        return this.sessionFactory.getCurrentSession()
                .createQuery("FROM MaterialEntity WHERE id= :id" , MaterialEntity.class)
                .setParameter("id",id)
                .uniqueResult();
    }

    public List<MaterialHistoryEntity> getMaterialHistoryByUserIdAndMaterialId(int materialId,int user_id) {
        return this.getQuerySession()
                .createQuery(" FROM MaterialHistoryEntity mh WHERE mh.user.id = :user_id and material_id = : material_id", MaterialHistoryEntity.class)
                .setParameter("user_id", user_id)
                .setParameter("material_id", materialId)
                .list();
    }

    public List<CourseEntity> getCoursesByLecturerId(int lecturerId) {
        return this.getQuerySession()
                .createQuery(" FROM CourseEntity  WHERE lecturer_id = :lecturer_id ", CourseEntity.class)
                .setParameter("lecturer_id", lecturerId)
                .list();
    }

    public LecturerEntity getLIdByUserId(int userId) {
        return this.sessionFactory.getCurrentSession()
                .createQuery("FROM LecturerEntity  WHERE user_id= :user_id" , LecturerEntity.class)
                .setParameter("user_id",userId)
                .uniqueResult();
    } */


//
//    public List<MaterialEntity> getMaterialByTag(String tag){
//        return this.sessionFactory.getCurrentSession()
//                .createQuery("FROM MaterialEntity WHERE MaterialEntity.TagEntity.name = :name")
//                .setParameter("name",tag)
//                .list();
//    }

  /// שאילתא לפי כותרת של חומר, לפי תגית ,ולפי טקסט חופשי

