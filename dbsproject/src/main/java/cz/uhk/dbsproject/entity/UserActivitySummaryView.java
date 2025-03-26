package cz.uhk.dbsproject.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_activity_summary")
public class UserActivitySummaryView {
    @Id
    @Column(name = "user_id")
    private int userId;
    private String name;
    private int ratingCount;
    private int recommendationCount;

    public int getUserId() { return userId; }
    public String getName() { return name; }
    public int getRatingCount() { return ratingCount; }
    public int getRecommendationCount() { return recommendationCount; }
}
