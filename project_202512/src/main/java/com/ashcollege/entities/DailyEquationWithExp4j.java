package com.ashcollege.entities;

import com.ashcollege.service.Persist;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


import java.util.Random;
@Component
@Configuration
@EnableScheduling
public class DailyEquationWithExp4j
{
    @Autowired
    public Persist persist;

    @Scheduled(cron = "0 0 0 * * *") // כל יום בחצות
    public void generateDailyEquation() {
        DailyChallengeQuestionEntity dailyChallengeQuestion = generateEquationAndEvaluate(persist);
        persist.save(dailyChallengeQuestion);
        System.out.println("הודפס");

    }

    public static DailyChallengeQuestionEntity generateEquationAndEvaluate(Persist persist)
    {
        String expr;
        DailyChallengeQuestionEntity existingQuestion = null;

        // ממשיך לנסות עד שמוצאים שאלה שלא קיימת
        do {
            Random rand = new Random();
            String[] operators = {"+", "-", "*", "/"};
            int numOperators = 2 + rand.nextInt(2); // 2 או 3 אופרטורים

            StringBuilder equation = new StringBuilder();
            equation.append(rand.nextInt(10) + 1); // מספר ראשון

            for (int i = 0; i < numOperators; i++) {
                String op = operators[rand.nextInt(operators.length)];
                int nextValue = rand.nextInt(9) + 1;

                // חילוק תקני
                if (op.equals("/")) {
                    int lastNum = getLastNumber(equation.toString());
                    while (nextValue == 0 || lastNum % nextValue != 0) {
                        nextValue = rand.nextInt(9) + 1;
                    }
                }

                equation.append(" ").append(op).append(" ").append(nextValue);
            }

            expr = equation.toString();
            existingQuestion = persist.isDailyQuestionExist(expr);

        } while (existingQuestion != null);

        // עכשיו ניצור את השאלה כי היא לא קיימת
        DailyChallengeQuestionEntity newQuestion = new DailyChallengeQuestionEntity();
        newQuestion.setQuestionText(expr);
        try {
            Expression expression = new ExpressionBuilder(expr).build();
            double result = expression.evaluate();
            newQuestion.setAnswer(result);
            System.out.println(expr + " = " + result);
            return newQuestion;
        } catch (Exception e) {
            return null;
        }
    }
    private static int getLastNumber(String expr) {
        String[] tokens = expr.trim().split(" ");
        return Integer.parseInt(tokens[tokens.length - 1]);
    }

}
