package com.example.time_management_handbook.model;

import com.google.api.client.util.DateTime;

import javax.security.auth.Subject;

public class TaskDTO {
    String idTask;
    String idUser;
    String name;
    DateTime creatingTime;
    DateTime endTime;
    String description;
    DateTime finishedTime;
    boolean isDeleted;


    public TaskDTO(String idTask, String idUser, String name, DateTime creatingTime, DateTime endTime, String description, DateTime finishedTime, boolean isDeleted) {
        this.idTask = idTask;
        this.idUser = idUser;
        this.name = name;
        this.creatingTime = creatingTime;
        this.endTime = endTime;
        this.description = description;
        this.finishedTime = finishedTime;
        this.isDeleted = isDeleted;
    }

    public String getIdTask() {
        return idTask;
    }

    public String getIdUser() {
        return idUser;
    }

    public String getName() {
        return name;
    }

    public DateTime getCreatingTime() {
        return creatingTime;
    }

    public DateTime getEndTime() {
        return endTime;
    }

    public String getDescription() {
        return description;
    }

    public DateTime getFinishedTime() {
        return finishedTime;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setIdTask(String idTask) {
        this.idTask = idTask;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCreatingTime(DateTime creatingTime) {
        this.creatingTime = creatingTime;
    }

    public void setEndTime(DateTime endTime) {
        this.endTime = endTime;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setFinishedTime(DateTime finishedTime) {
        this.finishedTime = finishedTime;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}
