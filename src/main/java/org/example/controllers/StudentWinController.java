package org.example.controllers;

import com.jfoenix.controls.JFXComboBox;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.example.App;
import org.example.animations.Shake;
import org.example.models.Lecture;
import org.example.models.Test;
import org.example.models.User;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentWinController {

    @FXML
    private Button lectureBtn;

    @FXML
    private Button testsBtn;

    @FXML
    private Label nameLbl;

    @FXML
    private Label secondNameLbl;

    @FXML
    private Label surnameLbl;

    @FXML
    private Label groupLbl;

    @FXML
    private Button resultsBtn;

    @FXML
    private JFXComboBox<String> disciplineComboBox;

    @FXML
    private Button exitBtn;

    @FXML
    void initialize() {
        exit();
        try {
            getDisciplines();
        } catch (SQLException | ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }

        openLectures();
        openTests();
        openResults();
        loadUserData();
    }

    private void exit() {
        exitBtn.setOnMouseClicked(mouseEvent -> {
            try {
                App.setRoot("main_win");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void getDisciplines() throws SQLException, IOException, ClassNotFoundException {
        ResultSet resultSet = User.selectDisciplinesForStudent();
        while (resultSet.next())
            disciplineComboBox.getItems().add(resultSet.getString("discipline_name"));
    }

    private boolean checkIsDisciplineSelected() {
        return !disciplineComboBox.getSelectionModel().isEmpty();
    }

    private void openLectures() {
        lectureBtn.setOnAction(mouseEvent -> {
            if (checkIsDisciplineSelected()) {
                Lecture.setDisciplineName(disciplineComboBox.getValue());
                try {
                    App.setRoot("lectures_win");

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else Shake.shake(disciplineComboBox);
        });
    }

    private void openTests() {
        testsBtn.setOnAction(mouseEvent -> {
            if (checkIsDisciplineSelected()) {
                Test.setDisciplineName(disciplineComboBox.getValue());
                try {
                    App.setRoot("studentTests_win");

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else Shake.shake(disciplineComboBox);
        });
    }

    private void openResults() {
        resultsBtn.setOnAction(mouseEvent -> {
            try {
                App.setRoot("studentResults_win");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void loadUserData() {
        User.loadStudentData();
        nameLbl.setText(nameLbl.getText() + User.getStudent().getFirstName());
        secondNameLbl.setText(secondNameLbl.getText() + User.getStudent().getSecondName());
        surnameLbl.setText(surnameLbl.getText() + User.getStudent().getSurname());
        groupLbl.setText(groupLbl.getText() + User.getStudent().getGroup());
    }
}



