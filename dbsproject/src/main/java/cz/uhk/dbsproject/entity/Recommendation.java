package cz.uhk.dbsproject.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "recommendation")
public class Recommendation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private MovieUser movieUser;
    @ManyToOne
    @JoinColumn(name = "recommended_movie_id")
    private Movie recommendedMovie;

    private LocalDateTime createdAt;

    public Recommendation() {
    }

    public int getId() {
        return id;
    }

    public MovieUser getUser() {
        return movieUser;
    }

    public void setUser(MovieUser movieUser) {
        this.movieUser = movieUser;
    }

    public Movie getRecommendedMovie() {
        return recommendedMovie;
    }

    public void setRecommendedMovie(Movie recommendedMovie) {
        this.recommendedMovie = recommendedMovie;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
