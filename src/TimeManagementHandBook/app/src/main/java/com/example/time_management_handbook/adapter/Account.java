package com.example.time_management_handbook.adapter;

import android.util.Log;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Account {
    public static Account instance;

    private Account() {}

    public static Account getInstance() {

        if (instance == null) {
            instance = new Account();
        }
        return instance;

    }

    public int InsertNewAccount(String email) {
        int rowEffects = 0;
        Log.d("Account exist: ", String.valueOf(CheckExistEmail(email)));
        if (!CheckExistEmail(email)) {
            String insertNewAccount = "EXEC USP_INSERT_NEW_USER '"+ email + "'";
            rowEffects = DataProvider.getInstance().executeNonQuery(insertNewAccount);
            Log.d("Insert new google account: ", String.valueOf(rowEffects));

        }
        return rowEffects;

    }

    public boolean CheckExistEmail(String email) {

        String query = "SELECT * FROM _USER WHERE EMAIL='" + email + "'";

        try {
            ResultSet resultSet = DataProvider.getInstance().executeQuery(query);
            Log.d("Execute query Insert: ", resultSet.toString());
            if (resultSet!= null) {
                while (resultSet.next()) {
                    String emailFromDB = resultSet.getString("EMAIL");
                    // So sánh email từ cơ sở dữ liệu với email được cung cấp
                    if (emailFromDB!= null && emailFromDB.equals(email)) {
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        };

        return false;

    }

}
