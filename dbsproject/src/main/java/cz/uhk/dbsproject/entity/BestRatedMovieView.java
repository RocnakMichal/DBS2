package cz.uhk.dbsproject.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "best_rated_movies")
public class BestRatedMovieView {
    @Id
    @Column(name = "movie_id")
    private int movieId;
    private String title;
    private double avgRating;
    private int totalRatings;

    public int getMovieId() { return movieId; }
    public String getTitle() { return title; }
    public double getAvgRating() { return avgRating; }
    public int getTotalRatings() { return totalRatings; }
}