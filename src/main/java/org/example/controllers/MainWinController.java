package org.example.controllers;

import com.jfoenix.controls.JFXToggleButton;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.example.App;
import org.example.animations.Shake;
import org.example.models.User;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class MainWinController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField loginField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button signInBtn;

    @FXML
    private Button signUpBtn;

    @FXML
    private JFXToggleButton toogleBtn;

    @FXML
    private Label falseLogOrPass;

    @FXML
    private Label emptyFields;

    private User user;


    @FXML
    void initialize() {
        signUpBtn.setOnAction(actionEvent -> {
            try {
                App.setRoot("registration");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }

    @FXML
    void openWin() {
        signInBtn.setOnMouseClicked(actionEvent -> {
            if (!loginField.getText().isEmpty() && !passwordField.getText().isEmpty()) {
                try {
                    if (checkAccess()) {
                        dropping();
                        changeWin();
                    } else wrongData();
                } catch (SQLException | IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                emptyFields();
            }
        });
    }

    @FXML
    void setToogleBtn() {
        toogleBtn.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                user = new User();
                if (toogleBtn.isSelected()) {
                    toogleBtn.setText("Cтудент");
                    user.setStatus("student");
                    user.setHelpStudent(", group_id");

                } else {
                    toogleBtn.setText("Преподаватель");
                    user.setStatus("teacher");
                    user.setHelpStudent("");
                }
            }
        });

    }


    private void changeWin() {
        try {
            App.setRoot(user.getStatus() + "_win");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean checkAccess() throws SQLException, IOException, ClassNotFoundException {
        user.setPassword(passwordField.getText()).setLogin(loginField.getText());
        return user.checkForCorrectData();
    }

    private void wrongData() {
        dropping();
        Shake.shake(passwordField, loginField);
        falseLogOrPass.setVisible(true);
    }

    private void emptyFields() {
        dropping();
        Shake.shake(passwordField, loginField);
        emptyFields.setVisible(true);
    }

    private void dropping() {
        falseLogOrPass.setVisible(false);
        emptyFields.setVisible(false);
    }

}

