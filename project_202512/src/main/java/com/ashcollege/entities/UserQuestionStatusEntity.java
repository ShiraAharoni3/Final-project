package com.ashcollege.entities;

import static com.ashcollege.utils.Constants.ATTEMPTS;

public class UserQuestionStatusEntity extends BaseEntity
{
    private UserEntity userEntity ;
    private QuestionEntity questionEntity ;
    private TopicEntity topicEntity ;
    private boolean is_answered ;
    private boolean is_correct ;
    private int numberOfResponseAttempts ;
    private int numberOfAttemptsUntilSuccess ;

    public UserQuestionStatusEntity(UserEntity userEntity, QuestionEntity questionEntity, TopicEntity topicEntity,
                                    boolean is_answered, boolean is_correct )
    {
        this.userEntity = userEntity;
        this.questionEntity = questionEntity;
        this.topicEntity = topicEntity;
        this.is_answered = is_answered;
        this.is_correct = is_correct;
        setNumberOfResponseAttempts(ATTEMPTS);
        setNumberOfAttemptsUntilSuccess(ATTEMPTS);
    }
    public UserQuestionStatusEntity()
    {

    }

    public int getNumberOfAttemptsUntilSuccess() {
        return numberOfAttemptsUntilSuccess;
    }

    public void setNumberOfAttemptsUntilSuccess(int numberOfAttemptsUntilSuccess)
    {
        if(is_correct)
        {
            this.numberOfAttemptsUntilSuccess += 0;
        }
        else
        {
            this.numberOfAttemptsUntilSuccess += numberOfAttemptsUntilSuccess;
        }

    }

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    public QuestionEntity getQuestionEntity() {
        return questionEntity;
    }

    public void setQuestionEntity(QuestionEntity questionEntity) {
        this.questionEntity = questionEntity;
    }

    public TopicEntity getTopicEntity() {
        return topicEntity;
    }

    public void setTopicEntity(TopicEntity topic_id) {
        this.topicEntity = topic_id;
    }

    public boolean isIs_answered() {
        return is_answered;
    }

    public void setIs_answered(boolean is_answered) {
        this.is_answered = is_answered;
    }

    public boolean isIs_correct() {
        return is_correct;
    }

    public void setIs_correct(boolean is_correct) {
        this.is_correct = is_correct;
    }

    public int getNumberOfResponseAttempts() {
        return numberOfResponseAttempts;
    }

    public void setNumberOfResponseAttempts(int numberOfResponseAttempts )
    {
        this.numberOfResponseAttempts += numberOfResponseAttempts;
    }

    @Override
    public String toString() {
        return "UserQuestionStatusEntity{" +
                "user_id=" + userEntity +
                ", question_id=" + questionEntity +
                ", topic_id=" + topicEntity +
                ", is_answered=" + is_answered +
                ", is_correct=" + is_correct +
                ", numberOfResponseAttempts=" + numberOfResponseAttempts +
                '}';
    }
}
