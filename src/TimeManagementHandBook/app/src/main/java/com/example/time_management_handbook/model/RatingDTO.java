package com.example.time_management_handbook.model;

public class RatingDTO {
    private String user;
    private int rating;

    public RatingDTO(String user, int rating) {
        this.user = user;
        this.rating = rating;
    }

    public String getUser() {
        return user;
    }

    public int getRating() {
        return rating;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "RatingDTO{" +
                "user='" + user + '\'' +
                ", rating=" + rating +
                '}';
    }
}
