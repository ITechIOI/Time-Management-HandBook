package com.example.time_management_handbook.adapter;

import android.util.Log;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountDAO {
    public static AccountDAO instance;

    private AccountDAO() {}

    public static AccountDAO getInstance() {

        if (instance == null) {
            instance = new AccountDAO();
        }
        return instance;

    }

    public int InsertNewAccount(String email) {
        int rowEffects = 0;
        Log.d("Account exist: ", String.valueOf(CheckExistEmail(email)));
        if (CheckExistEmail(email) == false) {
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

            if (resultSet!= null) {
                while (resultSet.next()) {
                    String emailFromDB = resultSet.getString("EMAIL");
                    Log.d("Execute query Insert: ", emailFromDB);
                    // So sánh email từ cơ sở dữ liệu với email được cung cấp
                    if (emailFromDB!= null && emailFromDB.equals(email)) {
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            Log.d("Check email exist: ", e.getMessage());
        };

        return false;
    }

    public String getUsername(String email) {
        String query = "SELECT * FROM _USER WHERE EMAIL = '" + email + "'";
        String username = "";
        try {
            ResultSet resultSet = DataProvider.getInstance().executeQuery(query);
            if (resultSet!= null && resultSet.next()) {
                username = resultSet.getString("FULLNAME");
            }
        } catch (SQLException e) {
            Log.d("Get username by email: ", e.getMessage());
        };

        return  username;
    }

}
