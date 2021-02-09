package org.example.controllers;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.example.App;
import org.example.models.Student;
import org.example.models.User;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class RegistrationController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button signUpBtn;

    @FXML
    private TextField fName;

    @FXML
    private TextField sName;

    @FXML
    private TextField surname;

    @FXML
    private ComboBox<String> groupComboBox;

    @FXML
    private TextField email;

    @FXML
    private Label warningLabel;
    @FXML
    private Label warningSignUPLabel;

    @FXML
    private Label warningUserAlreadyExistLabel;
    @FXML
    private Button backBtn;

    private Student student;


    @FXML
    public void back() {
        backBtn.setOnMouseClicked(mouseEvent -> {
            try {
                App.setRoot("main_win");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @FXML
    public void actionOnSignUpBtn() {
        signUpBtn.setOnMouseClicked(actionEvent -> {
            if (checkForEmptyField(fName.getText())
                    && checkForEmptyField(sName.getText())
                    && checkForEmptyField(surname.getText())
                    && checkForEmptyField(groupComboBox.getValue().toString())
                    && checkForEmptyField(email.getText())) {
                dropping();
                verifyStudent();
            } else {
                actionOnEmptyField();
            }
        });
    }


    @FXML
    void initialize() throws SQLException, IOException, ClassNotFoundException {
        ResultSet resultSet = User.selectGroups();
        while (resultSet.next())
            groupComboBox.getItems().add(resultSet.getString("group_name"));
    }

    private void verifyStudent() {
        Boolean b = signUpUser();
        if (b == null)
            actionOnUserAlreadyExist();
        else if (b) {
            try {
                student.insertLoginData();
            } catch (SQLException | IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            returnLoginAndPassword();
        } else actionOnFatalSignUp();
    }

    private boolean checkForEmptyField(String s) {
        return !s.trim().isEmpty();
    }


    private Boolean signUpUser() {
        try {
            student = new Student(fName.getText().trim(), sName.getText().trim(), surname.getText().trim(),
                    groupComboBox.getValue().toString(), email.getText().trim());
            return student.checkStudentInDB();
        } catch (SQLException | ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
        return true;
    }


    private void actionOnEmptyField() {
        dropping();
        warningLabel.setVisible(true);
    }

    private void actionOnFatalSignUp() {
        dropping();
        warningSignUPLabel.setVisible(true);
    }

    private void actionOnUserAlreadyExist() {
        dropping();
        warningUserAlreadyExistLabel.setVisible(true);
    }

    private void dropping() {
        warningLabel.setVisible(false);
        warningSignUPLabel.setVisible(false);
        warningUserAlreadyExistLabel.setVisible(false);
    }


    private void returnLoginAndPassword() {
        Label secondLabel = new Label("Не давайте никому эти данные! \nPassword: " + student.getPassword() + "\nLogin: " + student.getLogin());

        StackPane secondaryLayout = new StackPane();
        secondaryLayout.getChildren().add(secondLabel);
        Scene secondScene = new Scene(secondaryLayout, 400, 300);

        // New window (Stage)
        Stage newWindow = new Stage();
        newWindow.setResizable(false);
        newWindow.setTitle("Данные для входа");
        newWindow.setScene(secondScene);

        // Specifies the modality for new window.
        newWindow.initModality(Modality.WINDOW_MODAL);

        // Specifies the owner Window (parent) for new window

        Window primaryStage = App.getScene().getWindow();
        newWindow.initOwner(primaryStage);
//
//        // Set position of second window, related to primary window.
        newWindow.setX(primaryStage.getX() + 200);
        newWindow.setY(primaryStage.getY() + 100);

        Button button = new Button("OK");
        button.setPrefSize(50,50);
        AnchorPane pane = new AnchorPane();
        pane.getChildren().add(button);
        pane.setBottomAnchor(button,50d);
        pane.setRightAnchor(button,120d);
        pane.setLeftAnchor(button,120d);
        button.setOnAction(actionEvent -> {
            try {
                App.setRoot("main_win");
                newWindow.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
         secondaryLayout.getChildren().add(pane);
        newWindow.show();
    }


//    private void callViewWithData(String password, String login) {
//
//    }
}
