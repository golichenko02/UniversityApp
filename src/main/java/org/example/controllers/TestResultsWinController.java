package org.example.controllers;

import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.example.App;
import org.example.models.Info;
import org.example.models.Test;
import org.example.utils.DataBaseConnect;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class TestResultsWinController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXComboBox<String> testComboBox;

    @FXML
    private TableView<Info> dataTable;

    @FXML
    private TableColumn<Info, String> studentColumn;

    @FXML
    private TableColumn<Info, String> groupColumn;

    @FXML
    private TableColumn<Info, Integer> markColumn;

    @FXML
    private Button backBtn;

    private Test test;

    ObservableList<Info> results = FXCollections.observableArrayList();

    @FXML
    void initialize() {
        back();
        getTests();
        setOnComboBoxSelection();
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

    private void getTests() {
        test = new Test();
        ResultSet resultSet = null;

        try {
            resultSet = test.selectTests();
            while (resultSet.next()) {
                test.getTests().put(resultSet.getString("test_name"), resultSet.getInt("id"));
            }
        } catch (SQLException | ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
        testComboBox.getItems().addAll(test.getTests().keySet());
    }

    private void setOnComboBoxSelection() {

        testComboBox.setOnAction(actionEvent -> {
            dataTable.getItems().clear();
            loadTableData(testComboBox.getValue());
        });
    }

    private void loadTableData(String testName) {
        markColumn.setCellValueFactory(cellData -> cellData.getValue().getStudentMarkProperty().asObject());
        studentColumn.setCellValueFactory(cellData -> cellData.getValue().getStudentFioProperty());
        groupColumn.setCellValueFactory(cellData -> cellData.getValue().getStudentGroupProperty());

        try {
            getResults(testName);
        } catch (SQLException | IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        dataTable.setItems(results);
    }

    private void getResults(String testName) throws SQLException, IOException, ClassNotFoundException {
        ResultSet resultSet = selectMarksAndStudentIds(testName);
        while (resultSet.next()) {
            ResultSet rs = selectFioAndGroupBySId(resultSet.getInt("student_id"));
            rs.next();
            results.add(new Info(rs.getString("surname") + "  " + rs.getString("first_name") + "  " + rs.getString("second_name"),
                    rs.getString("group_name"), resultSet.getInt("mark")));
        }
    }

    private ResultSet selectMarksAndStudentIds(String testName) throws SQLException, IOException, ClassNotFoundException {
        return DataBaseConnect.getConnection().createStatement().executeQuery(String
                .format("SELECT student_id, mark FROM marks m WHERE m.test_id = %d", test.getTests().get(testName)));
    }

    private ResultSet selectFioAndGroupBySId(int sId) throws SQLException, IOException, ClassNotFoundException {
       return DataBaseConnect.getConnection().createStatement().executeQuery(String
                .format("SELECT first_name, second_name, surname, group_name FROM students s join student_groups " +
                        "WHERE s.id = %d AND  s.group_id  = student_groups.id", sId));
    }
}
