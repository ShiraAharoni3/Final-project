package com.ashcollege.entities;
import java.time.LocalDate;
import java.time.LocalDateTime;


public class QuestionEntity extends BaseEntity
{
    private String questionText;
    private double solution ;
    private int difficultyLevel; // רמת קושי (1-5)
    private String subject; // תגיות או נושאים
    private LocalDateTime createdAt;


    public QuestionEntity() {

    }

    public QuestionEntity(String questionText, double solution, int difficultyLevel,  String subject )
    {
        this.questionText = questionText;
        this.solution = solution;
        this.difficultyLevel = difficultyLevel;
        this.subject = subject;
        this.createdAt = LocalDateTime.now();
    }

    public double getSolution()
    {
        return solution;
    }

    public void setSolution(double solution)
    {
        this.solution = solution;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public int getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(int difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "QuestionEntity{" +
                "questionText='" + questionText + '\'' +
                ", solution=" + solution + '\'' +
                ", difficultyLevel=" + difficultyLevel +
                ", subject='" + subject + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}

/*public class QuestionEntity extends BaseEntity
{
    private EducationLevelEntity educationLevelEntity;

    public EducationLevelEntity getEducationLevelEntity() {
        return educationLevelEntity;
    }

    public void setEducationLevelEntity(EducationLevelEntity educationLevelEntity) {
        this.educationLevelEntity = educationLevelEntity;
    }

    private TopicEntity topic ;
    private String Subtopic;
    private int DifficultyLevel ;
    private String QuestionText ;
    private String SolutionSteps ;
    private String Tags;
    private LocalDate CreatedAt ;

    public QuestionEntity()
    {


    }

    public QuestionEntity(TopicEntity topic, String subtopic, int difficultyLevel, String questionText, String solutionSteps, String tags , int idLevel)
    {
        this.topic = topic;
        Subtopic = subtopic;
        DifficultyLevel = difficultyLevel;
        QuestionText = questionText;
        SolutionSteps = solutionSteps;
        Tags = tags;
        CreatedAt = LocalDate.now();
        this.educationLevelEntity = new EducationLevelEntity();
        educationLevelEntity.setId(idLevel);
    }


    @Override
    public String toString() {
        return "QuestionEntity{" +
                "topic=" + topic +
                ", Subtopic='" + Subtopic + '\'' +
                ", DifficultyLevel=" + DifficultyLevel +
                ", QuestionText='" + QuestionText + '\'' +
                ", SolutionSteps='" + SolutionSteps + '\'' +
                ", Tags='" + Tags + '\'' +
                ", CreatedAt=" + CreatedAt +
                '}';
    }

    public TopicEntity getTopic() {
        return topic;
    }

    public void setTopic(TopicEntity topic) {
        this.topic = topic;
    }

    public String getSubtopic() {
        return Subtopic;
    }

    public void setSubtopic(String subtopic) {
        Subtopic = subtopic;
    }

    public int getDifficultyLevel() {
        return DifficultyLevel;
    }

    public void setDifficultyLevel(int difficultyLevel) {
        DifficultyLevel = difficultyLevel;
    }

    public String getQuestionText() {
        return QuestionText;
    }

    public void setQuestionText(String questionText) {
        QuestionText = questionText;
    }

    public String getSolutionSteps() {
        return SolutionSteps;
    }

    public void setSolutionSteps(String solutionSteps) {
        SolutionSteps = solutionSteps;
    }

    public String getTags() {
        return Tags;
    }

    public void setTags(String tags) {
        Tags = tags;
    }

    public LocalDate getCreatedAt() {
        return CreatedAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        CreatedAt = createdAt;
    }
}*/
