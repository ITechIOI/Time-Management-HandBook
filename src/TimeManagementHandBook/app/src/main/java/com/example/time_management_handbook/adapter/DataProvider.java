package com.example.time_management_handbook.adapter;

import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DataProvider {
    private static final String DATABASE_URL = "jdbc:jtds:sqlserver://192.168.1.193:1433;databaseName=TIME_MANAGEMENT_HANDBOOK;user=sa;password=Loantuyetcute123;";
    public DataProvider() {}
    public static Connection getConnection() {
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

    public String getListTeacher()  {
        String dataUsingLabel = "";
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM _USER")) {

            while (resultSet.next()) {
                // Sử dụng columnIndex
                String dataUsingIndex = resultSet.getString(1); // Giả sử cột đầu tiên là kiểu chuỗi

                //Sử dụng columnLabel
                dataUsingLabel = resultSet.getString("FULLNAME"); // Giả sử "column_name" là tên của cột

            }
            return dataUsingLabel;
        } catch (SQLException e) {
            e.printStackTrace();
        };
        return null;
    }

}
