package com.example.time_management_handbook.model;

import java.time.LocalDate;

public class CalendarModel {
    public CalendarModel(int day, LocalDate date, int state)
    {
        this.day = day;
        this.date = date;
        this.state = state;
    }
    private int day;
    private LocalDate date;
    private int state;
    public int getDay() {return day;}
    public LocalDate getDate(){return date;}
    public int getState(){return state;}

}
