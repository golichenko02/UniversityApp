package org.example.controllers;

import com.jfoenix.controls.JFXComboBox;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.example.App;
import org.example.animations.Shake;
import org.example.models.Group;
import org.example.models.Lecture;
import org.example.models.Test;
import org.example.models.User;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class TeacherWinController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label nameLbl;

    @FXML
    private Label secondNameLbl;

    @FXML
    private Label surnameLbl;

    @FXML
    private Button lessonBtn;

    @FXML
    private Button testsBtn;

    @FXML
    private Button resultsBtn;

    @FXML
    private Button dataBtn;

    @FXML
    private JFXComboBox<String> disciplineComboBox;

    @FXML
    private Button exitBtn;

    @FXML
    void initialize() {
        back();
        try {
            getDisciplines();
        } catch (SQLException | IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        openLectures();
        openTests();
        openInfo();
        openResults();
        loadUserData();
    }

    private void getDisciplines() throws SQLException, IOException, ClassNotFoundException {
        ResultSet resultSet = User.selectDisciplines();
        while (resultSet.next())
            disciplineComboBox.getItems().add(resultSet.getString("discipline_name"));


    }

    private boolean checkIsDisciplineSelected() {
        return !disciplineComboBox.getSelectionModel().isEmpty();
    }

    private void openLectures() {
        lessonBtn.setOnAction(mouseEvent -> {
            if (checkIsDisciplineSelected()) {
                Lecture.setDisciplineName(disciplineComboBox.getValue());
                try {
                    App.setRoot("lessons_win");

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
                    App.setRoot("tests_win");

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else Shake.shake(disciplineComboBox);
        });
    }

    private void openResults() {
        resultsBtn.setOnAction(mouseEvent -> {
            if (checkIsDisciplineSelected()) {
                Test.setDisciplineName(disciplineComboBox.getValue());
                try {
                    App.setRoot("testResults_win");

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else Shake.shake(disciplineComboBox);
        });
    }

    private void openInfo() {
        dataBtn.setOnAction(mouseEvent -> {
            if (checkIsDisciplineSelected()) {
                Group.setDisciplineName(disciplineComboBox.getValue());
                try {
                    App.setRoot("info_win");

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else Shake.shake(disciplineComboBox);
        });
    }

    private void back() {
        exitBtn.setOnMouseClicked(mouseEvent -> {
            try {
                App.setRoot("main_win");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void loadUserData(){
        User.loadTeacherData();
        nameLbl.setText(nameLbl.getText() + User.getTeacher().getFirstName());
        secondNameLbl.setText(secondNameLbl.getText() + User.getTeacher().getSecondName());
        surnameLbl.setText(surnameLbl.getText() + User.getTeacher().getSurname());
    }
}


