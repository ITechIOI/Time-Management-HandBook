package com.example.time_management_handbook.adapter;

import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DataProvider {
    public static DataProvider instance;
    private static final String DATABASE_URL = "jdbc:jtds:sqlserver://192.168.1.9:1433;databaseName=TIME_MANAGEMENT_HANDBOOK;user=sa;password=Loantuyetcute123;";
    private DataProvider() {}

    public static DataProvider getInstance() {
        if (instance == null) {
            instance = new DataProvider();
        }
        return instance;
    }

    public Connection getConnection() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Connection connection = null;
        try {
            //Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            connection = DriverManager.getConnection(DATABASE_URL);
            Log.e("Connect to database: ","success");
        } catch (ClassNotFoundException | SQLException e) {
            Log.e("Connect to database: ","success" + e.getMessage());
        }
        return connection;
    }

    public List<String> getListTeacher(Connection connection)  {

        List<String>teachers = new ArrayList<>();
        try (
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM _USER")) {

            while (resultSet.next()) {
                teachers.add(resultSet.getString("FULLNAME"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        };
        return teachers;
    }

}
