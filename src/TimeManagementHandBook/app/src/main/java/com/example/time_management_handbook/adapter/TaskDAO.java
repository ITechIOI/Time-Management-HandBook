package com.example.time_management_handbook.adapter;

import com.example.time_management_handbook.model.TaskDTO;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TaskDAO {

    public static TaskDAO instance;
    private TaskDAO() {}
    public static TaskDAO getInstance() {
        if (instance == null) {
            instance = new TaskDAO();
        }
        return instance;
    }
    public List<TaskDTO> getListTask(String email, LocalDateTime dateTimeNow) {
        List<TaskDTO> listTasks = new ArrayList<>();

        

        return  listTasks;
    }
}
