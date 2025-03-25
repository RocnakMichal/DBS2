package cz.uhk.dbsproject.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "most_recommended_movies")
public class MostRecommendedMovieView {
    @Id
    @Column(name = "movie_id")
    private int movieId;
    private String title;
    private int totalRecommendations;

    public int getMovieId() { return movieId; }
    public String getTitle() { return title; }
    public int getTotalRecommendations() { return totalRecommendations; }
}