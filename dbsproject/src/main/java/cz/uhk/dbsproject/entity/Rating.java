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

    @Column(name = "rating", nullable = false)
    private double value;
    private String comment;
    private LocalDateTime createdAt;

    public Rating() {
    }

    public int getId() {
        return id;
    }

    public MovieUser getMovieUser() {
        return movieUser;
    }

    public void setMovieUser(MovieUser movieUser) {
        this.movieUser = movieUser;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
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
