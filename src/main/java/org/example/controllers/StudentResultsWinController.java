package org.example.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.example.App;
import org.example.models.Result;
import org.example.models.User;
import org.example.utils.DataBaseConnect;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class StudentResultsWinController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableView<Result> dataTable;

    @FXML
    private TableColumn<Result, String> testNameColumn;

    @FXML
    private TableColumn<Result, Integer> markColumn;

    @FXML
    private Label avgMarkLbl;

    @FXML
    private Label fioLbl;

    @FXML
    private Label groupLbl;

    @FXML
    private Label markALbl;

    @FXML
    private Label markBLbl;

    @FXML
    private Label markCLbl;

    @FXML
    private Label markDLbl;

    @FXML
    private Label markELbl;

    @FXML
    private Label markFXLbl;

    @FXML
    private Label markFLbl;

    @FXML
    private Button backBtn;

    ObservableList<Result> results = FXCollections.observableArrayList();


    @FXML
    void initialize() {
        back();
        calculateAvgMark();
        getData();
        loadTableData();
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


    private void calculateAvgMark() {
        getStudentMarks();
        int mark = 0;
        for (int i = 0; i < User.getStudent().getMarks().size(); i++) {
            mark += User.getStudent().getMarks().get(i).getMark();
        }
        mark /= User.getStudent().getMarks().size();

        avgMarkLbl.setText(avgMarkLbl.getText() + mark);
        getMarkLabel(mark);
    }

    private void getData() {
        fioLbl.setText(fioLbl.getText() + User.getStudent().getFirstName() + "  " + User.getStudent().getSecondName() + "  " + User.getStudent().getSurname());
        groupLbl.setText(groupLbl.getText() + User.getStudent().getGroup());
    }

    private void getStudentMarks() {
        User.selectMarksForStudent();
    }

    private void getMarkLabel(int mark) {
        if (mark >= 90) {
            markALbl.setVisible(true);
        } else if (mark > 81) {
            markBLbl.setVisible(true);
        } else if (mark > 74) {
            markCLbl.setVisible(true);
        } else if (mark > 63) {
            markDLbl.setVisible(true);
        } else if (mark > 59) {
            markELbl.setVisible(true);
        } else if (mark > 34) {
            markFXLbl.setVisible(true);
        } else if (mark > 0) {
            markFLbl.setVisible(true);
        }
    }

    private void loadTableData() {
        getMarks();
//        markColumn.setCellValueFactory(new PropertyValueFactory<Result,Integer>("markColumn"));
//        testNameColumn.setCellValueFactory(new PropertyValueFactory<Result,String>("testNameColumn"));

        markColumn.setCellValueFactory(cellData -> cellData.getValue().getMarkProperty().asObject());
        testNameColumn.setCellValueFactory(cellData -> cellData.getValue().getTestNameProperty());

        dataTable.setItems(results);

    }

    private void getMarks() {
        for (int i = 0; i < User.getStudent().getMarks().size(); i++) {
            results.add(new Result(User.getStudent().getMarks().get(i).getMark(), selectTestNameById(User.getStudent().getMarks().get(i).getTestId())));
        }
    }

    private String selectTestNameById(Integer testId) {
      String testName = null;
        ResultSet resultSet = null;
        try {
            resultSet = DataBaseConnect.getConnection().createStatement().executeQuery(String
                    .format("SELECT DISTINCT test_name FROM tests WHERE id = %d", testId));

            resultSet.next();
            testName = resultSet.getString("test_name");
        } catch (SQLException | IOException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }

       return testName;
    }
}
