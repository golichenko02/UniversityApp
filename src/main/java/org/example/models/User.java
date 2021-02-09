package org.example.models;


import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.example.utils.DataBaseConnect;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class User {

    @Getter
    private static Teacher teacher;
    @Getter
    private static Student student;
    private String status;
    private String password;
    private String login;
    private String helpStudent;


    public boolean checkForCorrectData() throws SQLException, IOException, ClassNotFoundException {
        Statement statement = DataBaseConnect.getConnection().createStatement();
        String sql = String.format(
                "SELECT DISTINCT id, login, user_password %s FROM education_app.%ss WHERE login LIKE '%s' AND user_password LIKE '%s' limit 1",
                helpStudent, status, login, password);
        ResultSet resultSet = statement.executeQuery(sql);
        //statement.close();
        if (resultSet.next()) {
            if (status.equals("student")) {
                student = new Student(String.valueOf(resultSet.getInt("id")), String.valueOf(resultSet.getInt("group_id")));
            } else {
                teacher = new Teacher(String.valueOf(resultSet.getInt("id")));
            }
            return true;
        }
        return false;
    }

    public static ResultSet selectGroups() throws SQLException, IOException, ClassNotFoundException {
        return DataBaseConnect.getConnection().createStatement().executeQuery("SELECT  DISTINCT id, group_name FROM student_groups");
    }

    public static ResultSet selectDisciplines() throws SQLException, IOException, ClassNotFoundException {
        return DataBaseConnect.getConnection().createStatement().executeQuery(
                String.format("SELECT  DISTINCT discipline_name FROM disciplines d, teachers t WHERE %d = d.teacher_id", Integer.valueOf(teacher.getId())));
    }

    public static ResultSet selectDisciplinesForStudent() throws SQLException, IOException, ClassNotFoundException {
        return DataBaseConnect.getConnection().createStatement().executeQuery(
                String.format("SELECT DISTINCT discipline_name " +
                        "FROM disciplines d " +
                        "WHERE d.id in (SELECT discipline_id FROM  disciplines_groups_link l " +
                        "WHERE l.group_id = %d)", Integer.valueOf(student.getGroupId())));
    }

    public static void selectMarksForStudent() {
        ResultSet resultSet = null;
        try {
            resultSet = DataBaseConnect.getConnection().createStatement().executeQuery(
                    String.format("SELECT DISTINCT id, mark, test_id, student_id " +
                            "FROM marks m " +
                            "WHERE student_id = '%s' ", student.getId()));

            student.getMarks().clear();
            while (resultSet.next()) {

                student.getMarks().add(new Mark(resultSet.getInt("id"),
                        resultSet.getInt("mark"),
                        resultSet.getInt("test_id"),
                        resultSet.getInt("student_id")));
            }
        } catch (SQLException | IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void loadStudentData() {
        ResultSet resultSet = null;
        try {
            resultSet = DataBaseConnect.getConnection().createStatement().executeQuery(
                    String.format("SELECT DISTINCT first_name, second_name, surname, group_name " +
                            "FROM students s , student_groups g " +
                            "WHERE s.id = %s " +
                            "AND s.group_id = g.id " +
                            "limit 1", student.getId()));

            while (resultSet.next()) {
                student.setFirstName(resultSet.getString("first_name"));
                student.setSecondName(resultSet.getString("second_name"));
                student.setSurname(resultSet.getString("surname"));
                student.setGroup(resultSet.getString("group_name"));
            }

        } catch (SQLException | IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void loadTeacherData() {
        ResultSet resultSet = null;
        try {
            resultSet = DataBaseConnect.getConnection().createStatement().executeQuery(
                    String.format("SELECT DISTINCT first_name, second_name, surname " +
                            "FROM teachers t " +
                            "WHERE t.id = %s " +
                            "limit 1", teacher.getId()));

            while (resultSet.next()) {
                teacher.setFirstName(resultSet.getString("first_name"));
                teacher.setSecondName(resultSet.getString("second_name"));
                teacher.setSurname(resultSet.getString("surname"));
            }

        } catch (SQLException | IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
