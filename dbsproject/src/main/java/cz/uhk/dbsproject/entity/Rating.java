package cz.uhk.dbsproject.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "rating")
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private MovieUser movieUser;

    @ManyToOne
    @JoinColumn(name = "movie_id")

    private Movie movie;

    private double rating;
    private String comment;
    private LocalDateTime createdAt;

    public Rating() {
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

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
