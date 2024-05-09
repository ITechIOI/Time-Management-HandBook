package com.example.time_management_handbook.adapter;

import com.example.time_management_handbook.model.Event_Of_The_Day_DTO;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class Event_Of_The_Day_DAO {

    public static Event_Of_The_Day_DAO instance;
    private Event_Of_The_Day_DAO() {}
    public static Event_Of_The_Day_DAO getInstance() {
        if (instance == null) {
            instance = new Event_Of_The_Day_DAO();
        }
        return instance;
    }

    public List<Event_Of_The_Day_DTO> getListEventOfTheDay(String email) {
        List<Event_Of_The_Day_DTO> events = new ArrayList<>();

        

        return events;
    }
}
