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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DataProvider {
    public static DataProvider instance;

   // private static final String DATABASE_URL = "jdbc:jtds:sqlserver://itechioitimemanagement.database.windows.net;databaseName=ITechIOI;user=ITechIOI;password=Loantuyetcute123;protocol=TLSv1.2";
   private static final String DATABASE_URL = "jdbc:sqlserver://itechioitimemanagement.database.windows.net:1433;database=ITechIOI;user=ITechIOI@itechioitimemanagement;password=Loantuyetcute123;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30";
   private Connection connection;
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

         connection = null;
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            connection = DriverManager.getConnection(DATABASE_URL);
            //Log.d("Connect to database: ","success " + "    " + connection.toString());
           Log.d("In get connection method: ", "success");
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
            Log.d("Connection String: ", connection.toString());
            while (resultSet.next()) {
                teachers.add(resultSet.getString("FULLNAME"));
            }
        } catch (SQLException e) {
            Log.d("Get list user error: ", e.getMessage());
        };
        return teachers;
    }

    /*public ResultSet executeQuery(String query) {
        ResultSet resultSet = null;

        ExecutorService executorConnection = Executors.newSingleThreadExecutor();
        executorConnection

        Connection connection = DataProvider.getInstance().getConnection();

        if (connection!= null) {
            try {
                Statement statement = connection.createStatement();
                resultSet = statement.executeQuery(query);
                Log.d("ResultSet: ", resultSet.toString());
            } catch (SQLException e) {
                Log.d("Execute query: ", e.getMessage());
            }
        } else {
            Log.d("Connection: ", "is null");
        }

        return resultSet;
    }*/

    public ResultSet executeQuery(String query) {
        ResultSet resultSet = null;

        if (connection!= null) {
            try {
                Statement statement = connection.createStatement();
                resultSet = statement.executeQuery(query);
                Log.d("ResultSet: ", resultSet.toString());
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
        try (
             PreparedStatement pstmt = connection.prepareStatement(sql)
        ) {

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
