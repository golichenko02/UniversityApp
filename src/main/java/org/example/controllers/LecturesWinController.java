package org.example.controllers;

import com.jfoenix.controls.JFXComboBox;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.example.App;
import org.example.models.Lecture;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LecturesWinController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;


    @FXML
    private JFXComboBox<String> lecturesComboBox;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private WebView webView;

    @FXML
    private Button backBtn;

    private Lecture lecture;



    @FXML
    void initialize() {
        back();
        getLectures();
        getLectureContent();

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

    private void getLectures() {
        lecture = new Lecture();
        ResultSet resultSet = null;

        try {
            resultSet = lecture.selectLectures();
            while (resultSet.next()) {
                lecture.getLectures().put(resultSet.getString("lecture_name"), resultSet.getString("content"));
                lecturesComboBox.getItems().add(resultSet.getString("lecture_name"));
            }
        } catch (SQLException | ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }

    private void getLectureContent() {
        webView = new WebView();
        final WebEngine webEngine = webView.getEngine();
        lecturesComboBox.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent arg0) {
                        webEngine.loadContent(lecture.getLectures().get(lecturesComboBox.getValue()));
                    }
                }
        );
        scrollPane.setContent(webView);

    }
}
