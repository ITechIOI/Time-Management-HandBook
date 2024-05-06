package com.example.time_management_handbook.model;

public class AccountDTO {
    private String id;
    private String fullName;
    private String email;
    private int status;

    public AccountDTO(String id, String fullName, String email, int status) {
        this.setId(id);
        this.setFullName(fullName);
        this.setEmail(email);
        this.setStatus(status);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
