package cz.uhk.dbsproject.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "log")
public class Log {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private MovieUser movieUser;
    private String action;
    private LocalDateTime timestamp;

    public Log() {
    }

    public int getId() {
        return id;
    }

    public MovieUser getUser() {
        return movieUser;
    }

    public void setMovieUser(MovieUser movieUser) {
        this.movieUser = movieUser;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
