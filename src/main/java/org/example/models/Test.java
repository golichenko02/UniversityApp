package org.example.models;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.example.utils.DataBaseConnect;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

@Data
public class Test {

    private HashMap<String, Integer> tests;

    private ArrayList<Question> questions;

    @Getter
    @Setter
    private static String disciplineName;

    public Test() {
        tests = new HashMap<>();
        questions = new ArrayList<>();
    }

    public ResultSet selectTests() throws SQLException, IOException, ClassNotFoundException {
        return DataBaseConnect.getConnection().createStatement().executeQuery(String.format
                ("SELECT test_name, id FROM tests t " +
                        "WHERE t.discipline_id IN (SELECT id from  disciplines where discipline_name like '%s')", disciplineName));
    }

    public ResultSet selectTestsForStudents() throws SQLException, IOException, ClassNotFoundException {
        return DataBaseConnect.getConnection().createStatement().executeQuery(String.format
                ("SELECT test_name, id FROM tests t " +
                        "WHERE t.discipline_id IN (SELECT id from  disciplines where discipline_name like '%s')", disciplineName));
    }


    public void selectQuestions(String testName) throws SQLException, IOException, ClassNotFoundException {
        ResultSet resultSet = DataBaseConnect.getConnection().createStatement().executeQuery(String.format
                ("SELECT id, question, correct_answer  FROM questions q WHERE q.test_id = %s ", tests.get(testName)));


        questions.clear();
        while (resultSet.next()) {
            questions.add(new Question(resultSet.getInt("id"), resultSet.getString("question"), resultSet.getString("correct_answer")));
        }

        for (int i = 0; i < questions.size(); i++) {
            ResultSet rs = selectFalseAnswersForQuestion(questions.get(i).getId());

            ArrayList<String> temp = new ArrayList<>();
            while (rs.next()) {
                temp.add(rs.getString("answer"));
            }
            questions.get(i).setFirstFalseAnswer(temp.get(0)).setSecondFalseAnswer(temp.get(1)).setThirdFalseAnswer(temp.get(2));
        }
    }


    private ResultSet selectFalseAnswersForQuestion(int questionId) throws SQLException, IOException, ClassNotFoundException {
        return DataBaseConnect.getConnection().createStatement().executeQuery(String.
                format("SELECT answer FROM answers a WHERE a.question_id = %s ", String.valueOf(questionId)));
    }


    public void insertNewQuestion(String testName, String question, String correctAnswer, String firstFalse,
                                  String secondFalse, String thirdFalse) throws SQLException, IOException, ClassNotFoundException {
        DataBaseConnect.getConnection().createStatement().execute(String.
                format("INSERT questions (question, correct_answer, test_id) VALUES ('%s', '%s', '%s')", question, correctAnswer, tests.get(testName)));

        Statement statement = DataBaseConnect.getConnection().createStatement();
        int questionId = selectQuestionId(question, correctAnswer);
        statement.execute(String.format("INSERT answers (answer, question_id) VALUES ('%s', '%s')", firstFalse, questionId));
        statement.execute(String.format("INSERT answers (answer, question_id) VALUES ('%s', '%s')", secondFalse, questionId));
        statement.execute(String.format("INSERT answers (answer, question_id) VALUES ('%s', '%s')", thirdFalse, questionId));
        statement.close();
    }

    private int selectQuestionId(String question, String answer) throws SQLException {
        ResultSet resultSet = null;
        try {
            resultSet = DataBaseConnect.getConnection().createStatement().executeQuery(String.
                    format("SELECT id FROM questions q WHERE q.question LIKE '%s' " +
                            "AND  q.correct_answer LIKE '%s'", question, answer));
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        int id = 0;
        while (resultSet.next()) {
            return id = resultSet.getInt("id");
        }
        return id;
    }

    public void removeQuestion(String question) {
        try {
            DataBaseConnect.getConnection().createStatement().execute(String
                    .format("DELETE  FROM questions WHERE question LIKE '%s'", question));
        } catch (SQLException | ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }

    public void updateQuestionAndTestName(String oldTestName, String oldQuestionName, int oldQuestionIndex,
                                          String newTestName, String newQuestionName, String correctAnswer,
                                          String firstFalse, String secondFalse, String thirdFalse) {
        Statement statement = null;
        try {
            statement = DataBaseConnect.getConnection().createStatement();
            statement.executeUpdate(String.format("UPDATE tests SET test_name = '%s' WHERE id = '%s'", newTestName, tests.get(oldTestName)));
            statement.executeUpdate(String.format("UPDATE questions SET question = '%s', correct_answer = '%s' " +
                    "WHERE question LIKE '%s'", newQuestionName, correctAnswer, oldQuestionName));

            int id = selectQuestionId(newQuestionName, correctAnswer);

            statement.executeUpdate(String.format("UPDATE answers SET answer = '%s', question_id = '%s' " +
                    "WHERE answer LIKE '%s'", firstFalse, id, questions.get(oldQuestionIndex).getFirstFalseAnswer()));

            statement.executeUpdate(String.format("UPDATE answers SET answer = '%s', question_id = '%s' " +
                    "WHERE answer LIKE '%s'", secondFalse, id, questions.get(oldQuestionIndex).getSecondFalseAnswer()));

            statement.executeUpdate(String.format("UPDATE answers SET answer = '%s', question_id = '%s' " +
                    "WHERE answer LIKE '%s'", thirdFalse, id, questions.get(oldQuestionIndex).getThirdFalseAnswer()));
        } catch (SQLException | IOException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }

    }


    public void insertNewTest(String testName) {
        ResultSet resultSet = null;
        int disciplineId = 0;
        try {
            resultSet = selectDisciplineId();
            while (resultSet.next())
                disciplineId = resultSet.getInt("id");


            DataBaseConnect.getConnection().createStatement().execute(String.
                    format("INSERT tests (test_name, discipline_id) VALUES ('%s', '%s')", testName, disciplineId));
        } catch (SQLException | IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    private ResultSet selectDisciplineId() throws SQLException, IOException, ClassNotFoundException {
        return DataBaseConnect.getConnection().createStatement().executeQuery(String.
                format("SELECT id FROM disciplines " +
                        "WHERE discipline_name like '%s'", disciplineName));
    }

    public void deleteTest(String testName) {
        int id = 0;
        ResultSet resultSet = null;
        try {
            resultSet = selectDisciplineId();
            resultSet.next();
            id = resultSet.getInt("id");

            DataBaseConnect.getConnection().createStatement().executeUpdate(String
                    .format("DELETE  FROM tests WHERE test_name LIKE '%s' AND discipline_id = %s ", testName, id));
        } catch (SQLException | IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        tests.remove(testName.trim());
    }

    public void insertMark(int mark, int testId, String studentId) {
        try {
            DataBaseConnect.getConnection().createStatement().execute(String.
                    format("INSERT marks (mark, test_id, student_id) VALUES ('%s', '%s', '%s')", mark, testId, studentId));
        } catch (SQLException | IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
