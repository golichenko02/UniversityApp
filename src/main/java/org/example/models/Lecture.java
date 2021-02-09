package org.example.models;

import lombok.Data;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.example.utils.DataBaseConnect;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

@Data
@Accessors(chain = true)
public class Lecture {

    private HashMap<String, String> lectures;

    @Setter
    private static String disciplineName;

    public Lecture() {
        this.lectures = new HashMap<>();
    }

    public ResultSet selectLectures() throws SQLException, IOException, ClassNotFoundException {
        return DataBaseConnect.getConnection().createStatement().executeQuery(String.format
                ("SELECT lecture_name, content FROM lectures l " +
                        "WHERE l.discipline_id IN (SELECT id from  disciplines where discipline_name like '%s')", disciplineName));
    }

    public void removeLecture(String lectureName) {
        this.lectures.remove(lectureName);
        System.out.println(deleteLectureInDB(lectureName));
    }

    public void updateLecture(String oldLectureName, String newLectureName, String content) {
        try {
            DataBaseConnect.getConnection().createStatement().executeUpdate(String.format("UPDATE lectures " +
                    "SET lecture_name = '%s', content = '%s' " +
                    "WHERE lecture_name = '%s'", newLectureName, content, oldLectureName));
        } catch (SQLException | IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void insertLecture(String lectureName, String content) {
        try {
            ResultSet resultSet = selectDisciplineId();
            if (resultSet.next()) {
                DataBaseConnect.getConnection().createStatement().execute(String.
                        format("INSERT lectures (lecture_name, content, discipline_id) " +
                                "VALUES ('%s', '%s', '%s')", lectureName, content, resultSet.getInt("id")));
            }
        } catch (SQLException | IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private ResultSet selectDisciplineId() throws SQLException, IOException, ClassNotFoundException {
        return DataBaseConnect.getConnection().createStatement().executeQuery(String.
                format("SELECT id FROM disciplines " +
                        "WHERE discipline_name like '%s'", disciplineName));
    }

    private boolean deleteLectureInDB(String lectureName) {
        try {
          return   DataBaseConnect.getConnection().createStatement().execute(String.
                    format("DELETE  FROM lectures WHERE lecture_name LIKE '%s'", lectureName));
        } catch (SQLException | ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
        return false;
    }


}
