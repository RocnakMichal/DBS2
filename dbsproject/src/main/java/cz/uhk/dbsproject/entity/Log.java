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
    private MovieUser user;
    private String action;
    private LocalDateTime timestamp;

    public Log() {
        this.timestamp = LocalDateTime.now();
    }

    public int getId() {
        return id;
    }

    public MovieUser getUser() {
        return user;
    }

    public void setUser(MovieUser movieUser) {
        this.user = movieUser;
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
