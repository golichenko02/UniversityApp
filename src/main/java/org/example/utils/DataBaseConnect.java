package org.example.utils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseConnect {

    public static Connection getConnection() throws SQLException, IOException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        String url = "jdbc:mysql://localhost/education_app?serverTimezone=UTC&useSSL=false";
        String username = "root";
        String password = "Holichenko_02";
        return DriverManager.getConnection(url, username, password);
    }
}
