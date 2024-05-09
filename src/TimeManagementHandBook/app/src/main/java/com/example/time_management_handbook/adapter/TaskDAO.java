package com.example.time_management_handbook.adapter;

public class TaskDAO {

    public static TaskDAO instance;
    private TaskDAO() {}
    public static TaskDAO getInstance() {
        if (instance == null) {
            instance = new TaskDAO();
        }
        return instance;
    }
}
