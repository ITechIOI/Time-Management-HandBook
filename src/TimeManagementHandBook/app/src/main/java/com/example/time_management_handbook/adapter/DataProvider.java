package com.example.time_management_handbook.adapter;

import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DataProvider {
    public static DataProvider instance;
    private static final String DATABASE_URL = "jdbc:jtds:sqlserver://192.168.1.12;databaseName=TIME_MANAGEMENT_HANDBOOK;user=sa;password=10051991bphuong";
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

    public List<String> getListUser()  {

        List<String>teachers = new ArrayList<>();
        try (
                Connection connection = DataProvider.getInstance().getConnection();
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

    public ResultSet executeQuery(String query) {
        ResultSet resultSet = null;
        Connection connection = DataProvider.getInstance().getConnection();

        if (connection!= null) {
            try {
                Statement statement = connection.createStatement();
                resultSet = statement.executeQuery(query);
                Log.d("ResultSet: ", resultSet.toString());
                // statement.close();
                //resultSet.close();
            } catch (SQLException e) {
                Log.d("Execute query: ", e.getMessage());
            }
        } else {
            Log.d("Connection: ", "is null");
        }

        return resultSet;
    }

    public int executeNonQuery(String sql) {
        int rowsAffected = -1;
        try (Connection connection = DataProvider.getInstance().getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            rowsAffected = pstmt.executeUpdate();
        } catch (SQLException e) {
            Log.d("Execute non query: ", e.getMessage());
        }
        return rowsAffected;
    }

    public Object executeScalar(String sql) {
        Object result = null;
        try (Connection connection = DataProvider.getInstance().getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                result = rs.getObject(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

}
