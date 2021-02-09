package org.example.controllers;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXRadioButton;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import org.example.App;
import org.example.animations.Shake;
import org.example.models.Mark;
import org.example.models.Question;
import org.example.models.Test;
import org.example.models.User;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class StudentTestsWinController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXComboBox<String> testComboBox;

    @FXML
    private Button takeTestBtn;

    @FXML
    private Button answerBtn;

    @FXML
    private JFXRadioButton fourthAnswer;

    @FXML
    private ToggleGroup answers;

    @FXML
    private Label questionLabel;

    @FXML
    private Button questionCounterBtn;

    @FXML
    private JFXRadioButton firstAnswer;

    @FXML
    private JFXRadioButton thirdAnswer;

    @FXML
    private JFXRadioButton secondAnswer;


    @FXML
    private Label markLabel;

    @FXML
    private Label attemptsNumber;

    @FXML
    private Label warningLbl;


    @FXML
    private Button backBtn;

    private Test test;

    private boolean isAnswered;

    private int counter;

    private ArrayList<Question> questions;

    private ArrayList<String> answersForQuestion;
    private String correctAnswer;
    private int countOfCorrectAnswers;
//    private static ArrayList<Mark> marks = new ArrayList<>();


    @FXML
    void initialize() {
        back();
        getTests();
        setOnTestComboBoxSelection();
        setOnTakeTestBtn();
        setOnAnswerBtn();
        getStudentMarks();
    }

    private void back() {
        backBtn.setOnMouseClicked(mouseEvent -> {
            try {
                App.setRoot("student_win");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void getStudentMarks() {
        User.selectMarksForStudent();
    }

    private void getTests() {
        test = new Test();
        ResultSet resultSet = null;

        try {
            resultSet = test.selectTests();

            while (resultSet.next()) {
                test.getTests().put(resultSet.getString("test_name"), resultSet.getInt("id"));
            }
            testComboBox.getItems().addAll(test.getTests().keySet());
        } catch (SQLException | ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }


    }

    private void setOnTestComboBoxSelection() {
        testComboBox.setOnAction(actionEvent -> {
            if (!testComboBox.getSelectionModel().isEmpty()) {
                if (isMarkPresent(test.getTests().get(testComboBox.getValue()))) {
                    takeTestBtn.setDisable(true);
                    warningLbl.setVisible(false);
                    attemptsNumber.setVisible(false);
                    markLabel.setVisible(true);
                } else {
                    takeTestBtn.setDisable(false);
                    warningLbl.setVisible(true);
                    attemptsNumber.setVisible(true);
                    markLabel.setVisible(false);
                }
            }
        });

    }

    private void setOnTakeTestBtn() {
        takeTestBtn.setOnAction(actionEvent -> {
            testComboBox.setDisable(true);
            takeTestBtn.setDisable(true);
//            backBtn.setDisable(true);
            warningLbl.setVisible(false);
            attemptsNumber.setVisible(false);

            questionCounterBtn.setVisible(true);
            questionLabel.setVisible(true);
            firstAnswer.setVisible(true);
            secondAnswer.setVisible(true);
            thirdAnswer.setVisible(true);
            fourthAnswer.setVisible(true);
            answerBtn.setVisible(true);
            isAnswered = true;
            counter = 0;
            questions = null;
            showTest();

            // show test

        });

    }

    private void showTest() {


        if (questions == null) {
            try {
                test.selectQuestions(testComboBox.getValue().trim());
            } catch (SQLException | IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            questions = test.getQuestions();
            answersForQuestion = new ArrayList<>();
        }
        if (isAnswered && counter < questions.size()) {
            correctAnswer = questions.get(counter).getCorrectAnswer();
            answersForQuestion.clear();
            answersForQuestion.addAll(Arrays.asList(questions.get(counter).getCorrectAnswer(),
                    questions.get(counter).getFirstFalseAnswer(),
                    questions.get(counter).getSecondFalseAnswer(),
                    questions.get(counter).getThirdFalseAnswer()));
            shuffleAnswers(answersForQuestion);
            questionLabel.setText(questions.get(counter).getQuestion());
            firstAnswer.setText(answersForQuestion.get(0));
            secondAnswer.setText(answersForQuestion.get(1));
            thirdAnswer.setText(answersForQuestion.get(2));
            fourthAnswer.setText(answersForQuestion.get(3));
            questionCounterBtn.setText(counter + 1 + "/" + questions.size());
            if (counter != 0)
                answers.getSelectedToggle().setSelected(false);
            isAnswered = false;
        }

    }

    private int increment() {
        return counter++;
    }


    private void setOnAnswerBtn() {
        answerBtn.setOnAction(actionEvent -> {
            if (answers.getSelectedToggle() != null) {
                isAnswered = true;
                RadioButton answer = (RadioButton) answers.getSelectedToggle();
                if (correctAnswer.equals(answer.getText().trim()))
                    countOfCorrectAnswers++;
                increment();
                showTest();
            } else Shake.shake(answerBtn);

            if (counter == questions.size()) {
                showMark();
                answerBtn.setDisable(true);
                getStudentMarks();
                testComboBox.setDisable(false);
            }
        });
    }

    private void showMark() {
        test.insertMark((int) calculateMark(), test.getTests().get(testComboBox.getValue().trim()), User.getStudent().getId());
        markLabel.setText("Оценка: " + calculateMark());
        markLabel.setVisible(true);
    }

    private float calculateMark() {
        return Math.round(((float) countOfCorrectAnswers / (float) questions.size()) * 100);
    }

    private void shuffleAnswers(List answers) {
        Collections.shuffle(answers);
    }

    private boolean isMarkPresent(int testId) {
        for (Mark mark : User.getStudent().getMarks()) {
            if (mark.getTestId() == testId) {
                markLabel.setText(String.valueOf(mark.getMark()));
                return true;
            }
        }
        return false;
    }

}

