package org.example.models;

import lombok.*;
import org.example.utils.DataBaseConnect;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Group {

    private int id;
    private String name;

    @Getter
    @Setter
    private static String disciplineName;

    public static ResultSet selectGroupsThatAlreadyStudyTheDiscipline() throws SQLException, IOException, ClassNotFoundException {
        return DataBaseConnect.getConnection().createStatement().executeQuery(String
                .format("SELECT  group_name FROM student_groups g WHERE g.id IN (SELECT l.group_id " +
                        "FROM disciplines_groups_link l " +
                        "WHERE l.discipline_id IN (SELECT id FROM disciplines WHERE discipline_name LIKE '%s'))", disciplineName));
    }

    public static void deleteRecord(int disciplineId, int groupId) {
        try {
            DataBaseConnect.getConnection().createStatement().execute(String.
                    format("DELETE  FROM disciplines_groups_link WHERE discipline_id = %d AND group_id = %d", disciplineId, groupId));
        } catch (SQLException | IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static int selectDisciplineId() throws SQLException, IOException, ClassNotFoundException {
        ResultSet resultSet = DataBaseConnect.getConnection().createStatement().executeQuery(String.
                format("SELECT id FROM disciplines " +
                        "WHERE discipline_name like '%s'", disciplineName));

        resultSet.next();
        return resultSet.getInt("id");
    }

    public static void insertRecord(int disciplineId, int groupId) throws SQLException, IOException, ClassNotFoundException {
        DataBaseConnect.getConnection().createStatement().execute(String
                .format("INSERT disciplines_groups_link (discipline_id, group_id) VALUES (%d, %d)", disciplineId, groupId));
    }
}
