package cz.uhk.dbsproject.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "group_member")
public class GroupMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "group_id")
    private UserGroup group;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private MovieUser movieUser;
    private LocalDateTime joinedAt;

    public GroupMember() {
    }

    public int getId() {
        return id;
    }

    public UserGroup getGroup() {
        return group;
    }

    public void setGroup(UserGroup group) {
        this.group = group;
    }

    public MovieUser getUser() {
        return movieUser;
    }

    public void setUser(MovieUser movieUser) {
        this.movieUser = movieUser;
    }

    public LocalDateTime getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(LocalDateTime joinedAt) {
        this.joinedAt = joinedAt;
    }
}
