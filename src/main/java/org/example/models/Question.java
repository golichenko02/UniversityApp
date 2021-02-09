package org.example.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Question {

    private int id;
    private String question;
    private String correctAnswer;
    private String firstFalseAnswer;
    private String secondFalseAnswer;
    private String thirdFalseAnswer;

    public Question(int id, String question, String correctAnswer) {
        this.id = id;
        this.question = question;
        this.correctAnswer = correctAnswer;
    }
}
