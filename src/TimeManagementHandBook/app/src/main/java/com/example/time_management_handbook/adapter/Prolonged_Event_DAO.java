package com.example.time_management_handbook.adapter;

public class Prolonged_Event_DAO {

    public static Prolonged_Event_DAO instance;
    private Prolonged_Event_DAO() {}
    public static Prolonged_Event_DAO getInstance() {
        if (instance == null) {
            instance = new Prolonged_Event_DAO();
        }
        return instance;
    }
}
