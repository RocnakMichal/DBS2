package cz.uhk.dbsproject.repository;

import cz.uhk.dbsproject.entity.Recommendation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecommendationRepository extends JpaRepository<Recommendation, Integer> {
}
