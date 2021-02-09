package org.example.controllers;

import com.jfoenix.controls.JFXComboBox;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.web.HTMLEditor;
import org.example.App;
import org.example.animations.Shake;
import org.example.models.Lecture;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;


public class LessonsWinController {


    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private HTMLEditor htmlEditor;

    @FXML
    private JFXComboBox<String> lectureComboBox;

    @FXML
    private Button saveContentButton;

    @FXML
    private Button removeLectureButton;

    @FXML
    private TextField lectureName;

    @FXML
    private Button exitBtn;

    private Lecture lecture;

    private boolean isLectureNameChanged;


    @FXML
    void initialize() {
        back();
        try {
            getLectures();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        getLectureContent();
        deleteLecture();
        setOnLectureNameChanging();
        setOnLectureContentChanging();
        saveLecture();
    }

    private void back() {
        exitBtn.setOnMouseClicked(mouseEvent -> {
            try {
                App.setRoot("teacher_win");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void getLectures() throws SQLException {
        lecture = new Lecture();
        ResultSet resultSet = null;

        lectureComboBox.getItems().add("Новая лекция");
        try {
            resultSet = lecture.selectLectures();
        } catch (SQLException | ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }

        while (resultSet.next()) {
            lecture.getLectures().put(resultSet.getString("lecture_name"), resultSet.getString("content"));
            lectureComboBox.getItems().add(resultSet.getString("lecture_name"));
        }
    }

    private void getLectureContent() {
        lectureComboBox.setOnAction(actionEvent -> {
            htmlEditor.setHtmlText(lecture.getLectures().get(lectureComboBox.getValue()));
            lectureName.setText(lectureComboBox.getValue());
            saveContentButton.setDisable(true);

            if (lectureComboBox.getSelectionModel().getSelectedItem() != null
                    && !lectureComboBox.getSelectionModel().getSelectedItem().trim().isEmpty())
                removeLectureButton.setDisable(false);
            else removeLectureButton.setDisable(true);
            if (lectureComboBox.getValue().equals("Новая лекция")) {
                removeLectureButton.setDisable(true);
                htmlEditor.setHtmlText("");
            }
            if (lectureComboBox.getValue().equals("Новая лекция"))
                saveContentButton.setDisable(false);
        });
    }

    private void deleteLecture() {
        removeLectureButton.setOnAction(actionEvent -> {
            if (lectureName.getText().equals(lectureComboBox.getValue())) {
                lecture.removeLecture(lectureComboBox.getValue());
                lectureComboBox.getItems().remove(lectureComboBox.getValue());
                lectureName.setText("");
                htmlEditor.setHtmlText("");
                removeLectureButton.setDisable(true);
            } else Shake.shake(lectureComboBox, lectureName);

        });
    }

    private void setOnLectureNameChanging() {

        lectureName.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!lectureName.getText().equals(lectureComboBox.getValue())) {
                    saveContentButton.setDisable(false);
                    isLectureNameChanged = true;
                } else {
                    saveContentButton.setDisable(true);
                    isLectureNameChanged = false;

                }

            }

        });
    }

    private void setOnLectureContentChanging() {
        htmlEditor.setOnMouseExited(mouseEvent -> {
            if (!isLectureNameChanged &&
                    (lectureComboBox.getSelectionModel().getSelectedItem() == null
                            || htmlEditor.getHtmlText().equals(lecture.getLectures().get(lectureComboBox.getValue())))) {  // || Jsoup.parse(htmlEditor.getHtmlText()).text().equals(lecture.getLectures().get(lectureComboBox.getValue()))))
                saveContentButton.setDisable(true);
            } else {
                saveContentButton.setDisable(false);
            }
        });
    }

    private void saveLecture() {

        saveContentButton.setOnAction(actionEvent -> {
            if (lectureName.getText().trim().isEmpty() || htmlEditor.getHtmlText().trim().isEmpty()) { //|| Jsoup.parse(htmlEditor.getHtmlText()).text().trim().isEmpty())
                Shake.shake(lectureName, htmlEditor);
                return;
            }

            if (lectureComboBox.getSelectionModel().getSelectedItem() != null
                    && !lectureComboBox.getSelectionModel().getSelectedItem().equals("Новая лекция")) {
                lecture.updateLecture(lectureComboBox.getValue(), lectureName.getText().trim(), htmlEditor.getHtmlText().trim()); // , Jsoup.parse(htmlEditor.getHtmlText()).text().trim());
                saveContentButton.setDisable(true);
                try {
                    App.setRoot("lessons_win");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                lecture.insertLecture(lectureName.getText().trim(), htmlEditor.getHtmlText().trim()); //, Jsoup.parse(htmlEditor.getHtmlText()).text().trim());
                saveContentButton.setDisable(true);
                try {
                    App.setRoot("lessons_win");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}


