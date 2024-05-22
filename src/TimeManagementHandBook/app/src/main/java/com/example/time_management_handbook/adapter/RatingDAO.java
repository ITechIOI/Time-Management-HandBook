package com.example.time_management_handbook.adapter;

import android.util.Log;

import com.example.time_management_handbook.activity.Home_Activity;

public class RatingDAO {
    public static RatingDAO instance;
    private RatingDAO() {}
    public static RatingDAO getInstance() {
        if (instance == null) {
            instance = new RatingDAO();
        }
        return instance;
    }

    public int InsertNewRating(int rate) {
        int rowEffect = -1;
        String user = Home_Activity.acc.getEmail().toString();

        String query = "EXEC INSERT_RATING '" + user + "'," + rate;
        try {
            rowEffect = DataProvider.getInstance().executeNonQuery(query);
            Log.d("Insert new prolonged event: ", String.valueOf(rowEffect));
            Log.d("Insert new prolonged event: ", query);
        } catch(Exception e) {
            Log.d("Insert new rating: ", e.getMessage());
        }

        return rowEffect;
    }

}
