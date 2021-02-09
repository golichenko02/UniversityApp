package org.example.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.example.utils.DataBaseConnect;
import org.example.utils.Password;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class Student {

    private String id;
    private String firstName;
    private String secondName;
    private String surname;
    private String password;
    private String login;
    private String group;
    private String groupId;
    private String email;
    private ArrayList<Mark> marks;

    public Student(String firstName, String secondName, String surname, String group, String email) {
        this.firstName = firstName;
        this.secondName = secondName;
        this.surname = surname;
        this.group = group;
        this.email = email;
    }

    public Student(String id, String groupId) {
        this.id = id;
        this.groupId = groupId;
        marks = new ArrayList<>();
    }

    public Boolean checkStudentInDB() throws SQLException, IOException, ClassNotFoundException {
        Statement statement = DataBaseConnect.getConnection().createStatement();
        String sql = String.format("SELECT distinct s.id, s.user_password, login  FROM education_app.students s, education_app.student_groups g " +
                        "WHERE concat(first_name, second_name, surname, group_name, email)  LIKE '%s' AND s.group_id = g.id",
                firstName + secondName + surname + group + email);

        ResultSet resultSet = statement.executeQuery(sql);

        if (resultSet.next()) {
            if (!resultSet.getString("s.user_password").trim().isEmpty())
                return null;
            this.id = String.valueOf(resultSet.getInt("s.id"));
            statement.close();
            this.password = Password.showPassword();
            this.login = email;
            return true;
        }
        return false;
    }

    public void insertLoginData() throws SQLException, IOException, ClassNotFoundException {
        String sql = "UPDATE education_app.students t SET t.login = ?, t.user_password = ?  WHERE t.id = " + Integer.valueOf(id);

        PreparedStatement preparedStatement = DataBaseConnect.getConnection().prepareStatement(sql);
        preparedStatement.setString(1, login);
        preparedStatement.setString(2, password);

        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

}
