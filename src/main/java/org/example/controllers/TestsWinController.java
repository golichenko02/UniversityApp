package org.example.controllers;

import com.jfoenix.controls.JFXComboBox;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.example.App;
import org.example.animations.Shake;
import org.example.models.Test;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;


public class TestsWinController {

    @FXML
    private JFXComboBox<String> testComboBox;

    @FXML
    private TextField question;

    @FXML
    private TextField correctAnswer;

    @FXML
    private TextField firstFalseAnswer;

    @FXML
    private TextField secondFalseAnswer;

    @FXML
    private TextField thirdFalseAnswer;

    @FXML
    private Button deleteTestBtn;

    @FXML
    private Button rmvQuestionBtn;

    @FXML
    private Button addQuestionBtn;

    @FXML
    private TextArea textArea;

    @FXML
    private JFXComboBox<String> questionComboBox;

    @FXML
    private TextField testName;

    @FXML
    private Button createTestBtn;

    @FXML
    private Button saveChangesBtn;

    @FXML
    private Button backBtn;

    private Test test;

    private boolean addQuestionBtnIsPressed;

    private boolean createTestBtnIsPressed;

    @FXML
    void initialize() {
        back();

        try {
            getTests();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        setOnTestComboBoxSelection();
        setOnQuestionComboBoxSelection();

        setOnTextFieldsChanging();

        addNewQuestion();
        rmvQuestion();

        addNewTest();

        deleteTest();
    }

    private void back() {
        backBtn.setOnMouseClicked(mouseEvent -> {
            try {
                App.setRoot("teacher_win");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void addNewTest() {
        createTestBtn.setOnAction(actionEvent -> {
            createTestBtnIsPressed = true;
            testName.setDisable(false);
            question.setDisable(true);
            correctAnswer.setDisable(true);
            firstFalseAnswer.setDisable(true);
            secondFalseAnswer.setDisable(true);
            thirdFalseAnswer.setDisable(true);
            questionComboBox.setDisable(true);
            textArea.setText("");
            questionComboBox.setValue("");
            testComboBox.setDisable(true);
            testComboBox.setValue("");
            testName.setText("");
            addQuestionBtn.setDisable(true);
            saveChangesBtn.setDisable(false);
            deleteTestBtn.setDisable(true);
        });
    }

    private void deleteTest(){
        deleteTestBtn.setOnAction(actionEvent -> {
            test.deleteTest(testComboBox.getValue());
            try {
                App.setRoot("tests_win");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void addNewQuestion() {
        addQuestionBtn.setOnAction(actionEvent -> {
            addQuestionBtnIsPressed = true;
            questionComboBox.setValue("");
            question.setText("");
            correctAnswer.setText("");
            firstFalseAnswer.setText("");
            secondFalseAnswer.setText("");
            thirdFalseAnswer.setText("");
            question.setDisable(false);
            testName.setDisable(true);
            testName.setText(testComboBox.getValue().trim());
            questionComboBox.setDisable(true);
            correctAnswer.setDisable(false);
            firstFalseAnswer.setDisable(false);
            secondFalseAnswer.setDisable(false);
            thirdFalseAnswer.setDisable(false);
            saveChangesBtn.setDisable(false);
            rmvQuestionBtn.setDisable(true);
        });
    }

    private void rmvQuestion() {
        rmvQuestionBtn.setOnAction(actionEvent -> {
            test.removeQuestion(questionComboBox.getValue());
            try {
                App.setRoot("tests_win");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void getTests() throws SQLException {
        test = new Test();
        ResultSet resultSet = null;

        try {
            resultSet = test.selectTests();
        } catch (SQLException | ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }

        while (resultSet.next()) {
            test.getTests().put(resultSet.getString("test_name"), resultSet.getInt("id"));
        }
        testComboBox.getItems().addAll(test.getTests().keySet());
    }

    private void setOnTestComboBoxSelection() {
        testComboBox.setOnAction(actionEvent -> {
            if (testComboBox.getSelectionModel().isEmpty()) {
                questionComboBox.setDisable(true);
            } else {
                addQuestionBtn.setDisable(false);
                testName.setDisable(false);
                testName.setText(testComboBox.getValue());
                deleteTestBtn.setDisable(false);
                questionComboBox.setDisable(false);
                questionComboBox.getItems().removeAll(questionComboBox.getItems());
                textArea.setText("");
                getQuestions(testComboBox.getValue());
                loadDataToTextArea();
            }
        });

    }

    private void setOnQuestionComboBoxSelection() {
        questionComboBox.setOnAction(actionEvent -> {
            if (questionComboBox.getSelectionModel().isEmpty()) {
                question.setDisable(true);
                correctAnswer.setDisable(true);
                firstFalseAnswer.setDisable(true);
                secondFalseAnswer.setDisable(true);
                thirdFalseAnswer.setDisable(true);
                rmvQuestionBtn.setDisable(true);
            } else {
                saveChangesBtn.setDisable(false);
                question.setDisable(false);
                correctAnswer.setDisable(false);
                firstFalseAnswer.setDisable(false);
                secondFalseAnswer.setDisable(false);
                thirdFalseAnswer.setDisable(false);
                rmvQuestionBtn.setDisable(false);
                question.setText(String.valueOf(test.getQuestions().get(questionComboBox.getSelectionModel().getSelectedIndex()).getQuestion()));
                correctAnswer.setText(String.valueOf(test.getQuestions().get(questionComboBox.getSelectionModel().getSelectedIndex()).getCorrectAnswer()));
                firstFalseAnswer.setText(String.valueOf(test.getQuestions().get(questionComboBox.getSelectionModel().getSelectedIndex()).getFirstFalseAnswer()));
                secondFalseAnswer.setText(String.valueOf(test.getQuestions().get(questionComboBox.getSelectionModel().getSelectedIndex()).getSecondFalseAnswer()));
                thirdFalseAnswer.setText(String.valueOf(test.getQuestions().get(questionComboBox.getSelectionModel().getSelectedIndex()).getThirdFalseAnswer()));
            }
        });
    }

    private void getQuestions(String testName) {
        try {
            test.selectQuestions(testName);
        } catch (SQLException | ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < test.getQuestions().size(); i++) {
            questionComboBox.getItems().add(test.getQuestions().get(i).getQuestion());
        }
    }

    private void loadDataToTextArea() {
        for (int i = 0; i < test.getQuestions().size(); i++) {
            textArea.setText(textArea.getText() + i + ". " + test.getQuestions().get(i).getQuestion() +
                    "\n Правильный ответ: " + test.getQuestions().get(i).getCorrectAnswer() +
                    "\n Варианты ответов: \n -" + test.getQuestions().get(i).getFirstFalseAnswer() + "\n -" +
                    test.getQuestions().get(i).getSecondFalseAnswer() + "\n -" +
                    test.getQuestions().get(i).getThirdFalseAnswer() + "\n -" +
                    test.getQuestions().get(i).getCorrectAnswer() +
                    "\n ----------------------------------------------------------------------------------------------\n "
            );

        }
    }

    private void setOnTextFieldsChanging() {
        saveChangesBtn.setOnAction(actionEvent -> {
            if (createTestBtnIsPressed){
                if (!testName.getText().trim().isEmpty()){
                    // insert new Test
                    test.insertNewTest(testName.getText().trim());
                    createTestBtnIsPressed = false;
                    saveChangesBtn.setDisable(true);
                    try {
                        App.setRoot("tests_win");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return;
                }else{
                    Shake.shake(saveChangesBtn, testName);
                    return;
                }
            }

            if (addQuestionBtnIsPressed) {
                if (!question.getText().trim().isEmpty()
                        && !testName.getText().trim().isEmpty()
                        && !correctAnswer.getText().trim().isEmpty()
                        && !firstFalseAnswer.getText().trim().isEmpty()
                        && !secondFalseAnswer.getText().trim().isEmpty()
                        && !thirdFalseAnswer.getText().trim().isEmpty()) {

                    // new insert
                    try {
                        test.insertNewQuestion(testName.getText().trim(), question.getText().trim(), correctAnswer.getText().trim(),
                                firstFalseAnswer.getText().trim(), secondFalseAnswer.getText().trim(), thirdFalseAnswer.getText().trim());
                        App.setRoot("tests_win");
                    } catch (SQLException | IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    addQuestionBtnIsPressed = false;
                    saveChangesBtn.setDisable(true);
                    return;
                } else {
                    testName.setDisable(false);
                    questionComboBox.setDisable(false);
                    Shake.shake(saveChangesBtn);
                    addQuestionBtnIsPressed = false;
                    saveChangesBtn.setDisable(true);
                }
                return;
            }

            if (textFieldsListener(testName, testComboBox)
                    && textFieldsListener(question, questionComboBox)
                    && correctAnswerListener()
                    && firstFalseAnswerListener()
                    && secondFalseAnswerListener()
                    && thirdFalseAnswerListener()) {
                Shake.shake(saveChangesBtn);
            } else {
                // update
                test.updateQuestionAndTestName(testComboBox.getValue().trim(), questionComboBox.getValue().trim(), questionComboBox.getSelectionModel().getSelectedIndex(),
                        testName.getText().trim(), question.getText().trim(), correctAnswer.getText().trim(),
                        firstFalseAnswer.getText().trim(), secondFalseAnswer.getText().trim(), thirdFalseAnswer.getText().trim());
                try {
                    App.setRoot("tests_win");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        });
    }

    private boolean textFieldsListener(TextField textField, JFXComboBox<String> comboBox) {
        final boolean[] flag = new boolean[1];
        if (!textField.getText().equals(comboBox.getValue())) {
            flag[0] = false;
        } else {
            flag[0] = true;
        }
        return flag[0];
    }

    private boolean correctAnswerListener() {
        final boolean[] flag = new boolean[1];
        if (!correctAnswer.getText().equals(test.getQuestions().get(questionComboBox.getSelectionModel().getSelectedIndex()).getCorrectAnswer())) {
            flag[0] = false;
        } else {
            flag[0] = true;
        }
        return flag[0];
    }

    private boolean firstFalseAnswerListener() {
        final boolean[] flag = new boolean[1];
        if (!firstFalseAnswer.getText().equals(test.getQuestions().get(questionComboBox.getSelectionModel().getSelectedIndex()).getFirstFalseAnswer())) {
            flag[0] = false;
        } else {
            flag[0] = true;
        }
        return flag[0];
    }

    private boolean secondFalseAnswerListener() {
        final boolean[] flag = new boolean[1];
        if (!secondFalseAnswer.getText().equals(test.getQuestions().get(questionComboBox.getSelectionModel().getSelectedIndex()).getSecondFalseAnswer())) {
            flag[0] = false;
        } else {
            flag[0] = true;
        }
        return flag[0];
    }

    private boolean thirdFalseAnswerListener() {
        final boolean[] flag = new boolean[1];
        if (!thirdFalseAnswer.getText().equals(test.getQuestions().get(questionComboBox.getSelectionModel().getSelectedIndex()).getThirdFalseAnswer())) {
            flag[0] = false;
        } else {
            flag[0] = true;
        }
        return flag[0];
    }
}
