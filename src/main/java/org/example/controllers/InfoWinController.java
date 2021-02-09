package org.example.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.controlsfx.control.CheckComboBox;
import org.example.App;
import org.example.models.Group;
import org.example.models.SessionsInfo;
import org.example.models.User;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

public class InfoWinController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private CheckComboBox<String> groupsCheckComboBox;

    @FXML
    private TableView<SessionsInfo> dataTable;

    @FXML
    private TableColumn<SessionsInfo, String> studentColumn;

    @FXML
    private TableColumn<SessionsInfo, String> groupColumn;

    @FXML
    private TableColumn<SessionsInfo, Integer> sessionsNumberColumn;

    @FXML
    private Button backBtn;

    private HashMap<String, Integer> groups;

    private ArrayList<String> checkedGroups;

    ObservableList<SessionsInfo> results = FXCollections.observableArrayList(
            new SessionsInfo("Петров Петр Петрович", "АС-203", 5),
            new SessionsInfo("Голиченко Владимир Сергеевич", "АС-192", 8),
            new SessionsInfo("Сергеев Сергей Сергеевич", "АС-204", 9)

    );


    @FXML
    void initialize() {
        getGroups();
        back();
        loadTableData();
    }

    private void loadTableData() {
        sessionsNumberColumn.setCellValueFactory(cellData -> cellData.getValue().getSessionsNumberProperty().asObject());
        studentColumn.setCellValueFactory(cellData -> cellData.getValue().getStudentFioProperty());
        groupColumn.setCellValueFactory(cellData -> cellData.getValue().getStudentGroupProperty());


       dataTable.setItems(results);
    }

    private void back() {
        backBtn.setOnAction(mouseEvent -> {
            try {
                App.setRoot("teacher_win");
            } catch (IOException e) {
                e.printStackTrace();
            }
            insertGroups();
        });
    }

    private void getGroups() {
        getUncheckedGroups();
        getCheckedGroups();
    }

    private void insertGroups() {
        try {
            deleteUnchecked();
            insertNewChecked();
        } catch (SQLException | ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }


    private void getCheckedGroups() {
        try {
            ResultSet resultSet = Group.selectGroupsThatAlreadyStudyTheDiscipline();
            checkedGroups = new ArrayList<>();
            while (resultSet.next()) {
                groupsCheckComboBox.getCheckModel().check((resultSet.getString("group_name")));
                checkedGroups.add(resultSet.getString("group_name"));
            }
        } catch (SQLException | IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void getUncheckedGroups() {
        try {
            ResultSet resultSet = User.selectGroups();
            if (groups == null)
                groups = new HashMap<>();
            while (resultSet.next()) {
                groups.put(resultSet.getString("group_name"), resultSet.getInt("id"));
                groupsCheckComboBox.getItems().add(resultSet.getString("group_name"));
            }
        } catch (SQLException | IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void deleteUnchecked() throws SQLException, IOException, ClassNotFoundException {
        for (int i = 0; i < groupsCheckComboBox.getItems().size(); i++) {
            if (!groupsCheckComboBox.getCheckModel().isChecked(i)) {
                Group.deleteRecord(Group.selectDisciplineId(), groups.get(groupsCheckComboBox.getItems().get(i)));
            }
        }
    }

    private void insertNewChecked() throws SQLException, IOException, ClassNotFoundException {
        for (int i = 0; i < groupsCheckComboBox.getItems().size(); i++) {
            if (groupsCheckComboBox.getCheckModel().isChecked(i)) {
                if (isNew(groupsCheckComboBox.getItems().get(i)))
                    Group.insertRecord(Group.selectDisciplineId(), groups.get(groupsCheckComboBox.getItems().get(i)));
            }
        }
    }

    private boolean isNew(String element) {
        return !checkedGroups.contains(element);
    }
}